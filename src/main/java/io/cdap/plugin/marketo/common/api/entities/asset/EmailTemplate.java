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
 * Email template entity.
 */
@Entity
public class EmailTemplate {
  String createdAt;
  String description;
  FolderDescriptor folder;
  Integer id;
  String name;
  String status;
  String updatedAt;
  String url;
  Integer version;
  String workspace;

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

  public String getStatus() {
    return status;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }

  public Integer getVersion() {
    return version;
  }

  public String getWorkspace() {
    return workspace;
  }
}
