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
 * Landing page entity.
 */
@Entity(topLevel = true)
public class LandingPage {
  String url;
  String computedUrl;
  String createdAt;
  String customHeadHTML;
  String description;
  String facebookOgTags;
  FolderDescriptor folder;
  Boolean formPrefill;
  Integer id;
  String keywords;
  Boolean mobileEnabled;
  String name;
  String robots;
  String status;
  Integer template;
  String title;
  String updatedAt;
  String workspace;

  public String getUrl() {
    return url;
  }

  public String getComputedUrl() {
    return computedUrl;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getCustomHeadHTML() {
    return customHeadHTML;
  }

  public String getDescription() {
    return description;
  }

  public String getFacebookOgTags() {
    return facebookOgTags;
  }

  public FolderDescriptor getFolder() {
    return folder;
  }

  public Boolean getFormPrefill() {
    return formPrefill;
  }

  public Integer getId() {
    return id;
  }

  public String getKeywords() {
    return keywords;
  }

  public Boolean getMobileEnabled() {
    return mobileEnabled;
  }

  public String getName() {
    return name;
  }

  public String getRobots() {
    return robots;
  }

  public String getStatus() {
    return status;
  }

  public Integer getTemplate() {
    return template;
  }

  public String getTitle() {
    return title;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getWorkspace() {
    return workspace;
  }
}
