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

package io.cdap.plugin.marketo.common.api.entities.activities;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Activity type response.
 */
public class ActivityTypeResponse extends BaseResponse {
  /**
   * Activity type attribute.
   */
  public static class ActivityTypeAttribute {
    private String apiName;
    private String dataType;
    private String name;

    public String getApiName() {
      return apiName;
    }

    public String getDataType() {
      return dataType;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return String.format("%s(%s, %s)", getApiName(), getDataType(), getName());
    }
  }

  /**
   * Attribute type.
   */
  public static class ActivityType {
    private String apiName;
    private List<ActivityTypeAttribute> attributes;
    private String description;
    private Integer id;
    private String name;
    private ActivityTypeAttribute primaryAttribute;

    public String getApiName() {
      return apiName;
    }

    public List<ActivityTypeAttribute> getAttributes() {
      return attributes;
    }

    public String getDescription() {
      return description;
    }

    public Integer getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public ActivityTypeAttribute getPrimaryAttribute() {
      return primaryAttribute;
    }
  }

  List<ActivityType> result = Collections.emptyList();

  public List<ActivityType> getResult() {
    return result;
  }
}
