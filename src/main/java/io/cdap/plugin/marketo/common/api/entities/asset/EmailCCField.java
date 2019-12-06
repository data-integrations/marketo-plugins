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

package io.cdap.plugin.marketo.common.api.entities.asset;

import io.cdap.plugin.marketo.common.api.entities.asset.gen.Entity;

/**
 * Email CC field.
 */
@Entity
public class EmailCCField {
  String attributeId;
  String objectName;
  String displayName;
  String apiName;

  public EmailCCField() {
  }

  public EmailCCField(String attributeId, String objectName, String displayName, String apiName) {
    this.attributeId = attributeId;
    this.objectName = objectName;
    this.displayName = displayName;
    this.apiName = apiName;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public String getObjectName() {
    return objectName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getApiName() {
    return apiName;
  }

  @Override
  public String toString() {
    return "EmailCCField{" +
      "attributeId='" + attributeId + '\'' +
      ", objectName='" + objectName + '\'' +
      ", displayName='" + displayName + '\'' +
      ", apiName='" + apiName + '\'' +
      '}';
  }
}
