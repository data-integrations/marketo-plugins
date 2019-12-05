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

/**
 * Program entity.
 */
public class Program {
  String channel;
  String createdAt;
  String description;
  FolderDescriptor folder;
  Integer id;
  String name;
  String sfdcId;
  String sfdcName;
  String status;
  String type;
  String updatedAt;
  String url;
  String workspace;

  public String getChannel() {
    return channel;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getDescription() {
    return description;
  }

  public FolderDescriptor getFolder() {
    return folder;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSfdcId() {
    return sfdcId;
  }

  public String getSfdcName() {
    return sfdcName;
  }

  public String getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }

  public String getWorkspace() {
    return workspace;
  }
}
