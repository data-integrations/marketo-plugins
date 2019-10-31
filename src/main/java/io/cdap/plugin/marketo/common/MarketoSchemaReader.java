/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.marketo.common;

import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.response.describe.DescribeResponse;
import io.cdap.plugin.marketo.common.response.describe.DescribeResponseLeads;
import io.cdap.plugin.marketo.common.response.describe.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates schema for given entity.
 */
public class MarketoSchemaReader {
  // TODO fill predefined schemas
  private static final Map<MarketoEntity, Schema> PREDEFINED_SCHEMAS = new HashMap<>();

  public static Schema getSchemaForEntity(MarketoEntity entity, Marketo marketo) {
    if (entity.getDescribeEndpoint() != null) {
      List<Field> entityFields;

      if (entity == MarketoEntity.Leads) {
        DescribeResponseLeads leadsDescribe = marketo.get(entity.getDescribeEndpoint(),
                                                                          DescribeResponseLeads.class);
        entityFields = leadsDescribe.getFields();
      } else {
        DescribeResponse describe = marketo.get(entity.getDescribeEndpoint(), DescribeResponse.class);
        entityFields = describe.getFields();
      }

      List<Schema.Field> schemaFields = entityFields.stream()
        //TODO map types here
        .map(field -> Schema.Field.of(field.getName(), Schema.nullableOf(Schema.of(Schema.Type.STRING))))
        .collect(Collectors.toList());
      return Schema.recordOf(entity.getName(), schemaFields);
    } else if (PREDEFINED_SCHEMAS.containsKey(entity)) {
      return PREDEFINED_SCHEMAS.get(entity);
    }
    throw new RuntimeException(String.format("Unable to get schema for entity '%s'", entity.getName()));
  }

}
