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
 * StaticList entity.
 */
public class StaticList {
  Integer id;
  String name;
  String description;
  String createdAt;
  String updatedAt;
  String url;
  FolderDescriptor folder;
  String workspace;
  String computedUrl;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }

  public FolderDescriptor getFolder() {
    return folder;
  }

  public String getWorkspace() {
    return workspace;
  }

  public String getComputedUrl() {
    return computedUrl;
  }
}
