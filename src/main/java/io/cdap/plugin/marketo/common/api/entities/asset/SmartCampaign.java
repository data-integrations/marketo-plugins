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
 * SmartCampaign entity.
 */
@Entity(topLevel = true)
public class SmartCampaign {
  Integer id;
  String name;
  String description;
  String type;
  Boolean isSystem;
  Boolean isActive;
  Boolean isRequestable;
  Recurrence recurrence;
  String qualificationRuleType;
  Integer qualificationRuleInterval;
  String qualificationRuleUnit;
  Integer maxMembers;
  Boolean isCommunicationLimitEnabled;
  Integer smartListId;
  Integer flowId;
  FolderDescriptor folder;
  String createdAt;
  String updatedAt;
  String workspace;
  String status;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

  public Boolean getSystem() {
    return isSystem;
  }

  public Boolean getActive() {
    return isActive;
  }

  public Boolean getRequestable() {
    return isRequestable;
  }

  public Recurrence getRecurrence() {
    return recurrence;
  }

  public String getQualificationRuleType() {
    return qualificationRuleType;
  }

  public Integer getQualificationRuleInterval() {
    return qualificationRuleInterval;
  }

  public String getQualificationRuleUnit() {
    return qualificationRuleUnit;
  }

  public Integer getMaxMembers() {
    return maxMembers;
  }

  public Boolean getCommunicationLimitEnabled() {
    return isCommunicationLimitEnabled;
  }

  public Integer getSmartListId() {
    return smartListId;
  }

  public Integer getFlowId() {
    return flowId;
  }

  public FolderDescriptor getFolder() {
    return folder;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getWorkspace() {
    return workspace;
  }

  public String getStatus() {
    return status;
  }
}
