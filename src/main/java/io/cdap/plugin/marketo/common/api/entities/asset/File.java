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
 * File entity.
 */
@Entity(topLevel = true)
public class File {
  String createdAt;
  String description;
  FileFolder folder;
  Integer id;
  String mimeType;
  String name;
  Integer size;
  String updatedAt;
  String url;

  public String getCreatedAt() {
    return createdAt;
  }

  public String getDescription() {
    return description;
  }

  public FileFolder getFolder() {
    return folder;
  }

  public Integer getId() {
    return id;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getName() {
    return name;
  }

  public Integer getSize() {
    return size;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }
}
