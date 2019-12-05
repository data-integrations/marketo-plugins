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
 * Folder entity.
 */
public class Folder {
  Integer accessZoneId;
  String createdAt;
  String  description;
  FolderDescriptor folderId;
  String folderType;
  Integer id;
  Boolean isArchive;
  Boolean isSystem;
  String name;
  FolderDescriptor parent;
  String path;
  String updatedAt;
  String url;
  String workspace;

  public Integer getAccessZoneId() {
    return accessZoneId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getDescription() {
    return description;
  }

  public FolderDescriptor getFolderId() {
    return folderId;
  }

  public String getFolderType() {
    return folderType;
  }

  public Integer getId() {
    return id;
  }

  public Boolean getArchive() {
    return isArchive;
  }

  public Boolean getSystem() {
    return isSystem;
  }

  public String getName() {
    return name;
  }

  public FolderDescriptor getParent() {
    return parent;
  }

  public String getPath() {
    return path;
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
