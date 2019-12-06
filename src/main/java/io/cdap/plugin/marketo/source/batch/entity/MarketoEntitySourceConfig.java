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

package io.cdap.plugin.marketo.source.batch.entity;

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.plugin.common.IdUtils;
import io.cdap.plugin.common.ReferencePluginConfig;
import io.cdap.plugin.marketo.common.api.Marketo;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides all required configuration for reading Marketo entities.
 */
public class MarketoEntitySourceConfig extends ReferencePluginConfig {
  public static final String PROPERTY_CLIENT_ID = "clientId";
  public static final String PROPERTY_CLIENT_SECRET = "clientSecret";
  public static final String PROPERTY_REST_API_ENDPOINT = "restApiEndpoint";
  public static final String PROPERTY_ENTITY_TYPE = "entityType";
  public static final String PROPERTY_SPLITS_COUNT = "splitsCount";
  public static final String PROPERTY_RESULTS_PER_PAGE = "resultsPerPage";

  @Name(PROPERTY_CLIENT_ID)
  @Description("Marketo Client ID.")
  @Macro
  protected String clientId;

  @Name(PROPERTY_CLIENT_SECRET)
  @Description("Marketo Client secret.")
  @Macro
  protected String clientSecret;

  @Name(PROPERTY_REST_API_ENDPOINT)
  @Description("REST API endpoint URL.")
  @Macro
  protected String restApiEndpoint;

  @Name(PROPERTY_ENTITY_TYPE)
  @Description("Type of entity.")
  @Macro
  protected String entityType;

  @Name(PROPERTY_SPLITS_COUNT)
  @Description("Number of splits to fetch data.")
  @Macro
  protected int splitsCount;

  @Name(PROPERTY_RESULTS_PER_PAGE)
  @Description("Max results per page.")
  @Macro
  protected int resultsPerPage;

  private transient Schema schema = null;
  private transient Marketo marketo = null;

  public MarketoEntitySourceConfig(String referenceName) {
    super(referenceName);
  }

  public EntityHelper.EntityType getEntityType() {
    return EntityHelper.EntityType.fromString(entityType);
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = EntityHelper.schemaForEntityName(getEntityType().getValue());
    }
    return schema;
  }

  public Marketo getMarketo() {
    if (marketo == null) {
      marketo = new Marketo(getRestApiEndpoint(), getClientId(), getClientSecret());
    }
    return marketo;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getRestApiEndpoint() {
    return restApiEndpoint;
  }

  public int getSplitsCount() {
    return splitsCount;
  }

  public int getResultsPerPage() {
    return resultsPerPage;
  }

  void validate(FailureCollector failureCollector) {
    IdUtils.validateReferenceName(referenceName, failureCollector);
    validateEntityType(failureCollector);
    validateMarketoEndpoint(failureCollector);
    validateSecrets(failureCollector);
  }

  void validateEntityType(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_ENTITY_TYPE)) {
      try {
        getEntityType();
      } catch (IllegalArgumentException ex) {
        failureCollector.addFailure(String.format("Incorrect entity type '%s'.", entityType),
                                    "Set entity type to valid.")
          .withConfigProperty(PROPERTY_ENTITY_TYPE);
      }
    }
  }

  void validateSecrets(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_CLIENT_ID) && Strings.isNullOrEmpty(getClientId())) {
      failureCollector.addFailure("Client ID is empty.", null)
        .withConfigProperty(PROPERTY_CLIENT_ID);
    }

    if (!containsMacro(PROPERTY_CLIENT_SECRET) && Strings.isNullOrEmpty(getClientSecret())) {
      failureCollector.addFailure("Client Secret is empty.", null)
        .withConfigProperty(PROPERTY_CLIENT_SECRET);
    }
  }

  void validateMarketoEndpoint(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_REST_API_ENDPOINT)) {
      try {
        new URL(getRestApiEndpoint());
      } catch (MalformedURLException e) {
        failureCollector
          .addFailure(String.format("Malformed Marketo Rest API endpoint URL '%s'.", getRestApiEndpoint()), null)
          .withConfigProperty(PROPERTY_REST_API_ENDPOINT);
      }
    }
  }
}
