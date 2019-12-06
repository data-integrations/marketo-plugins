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

package io.cdap.plugin.marketo.common.api.entities.leads;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Represents leads describe response.
 */
public class LeadsDescribeResponse extends BaseResponse {
  /**
   * Represents lead field description.
   */
  public static class LeadAttribute {
    String dataType;
    String displayName;
    int id;
    int length;
    LeadMapAttribute rest;
    LeadMapAttribute soap;

    public String getDataType() {
      return dataType;
    }

    public String getDisplayName() {
      return displayName;
    }

    public int getId() {
      return id;
    }

    public int getLength() {
      return length;
    }

    public LeadMapAttribute getRest() {
      return rest;
    }

    public LeadMapAttribute getSoap() {
      return soap;
    }
  }

  /**
   * Represents leads field name.
   */
  public static class LeadMapAttribute {
    String name;
    boolean readOnly = true;

    public String getName() {
      return name;
    }

    public boolean isReadOnly() {
      return readOnly;
    }
  }

  List<LeadAttribute> result = Collections.emptyList();

  public List<LeadAttribute> getResult() {
    return result;
  }
}
