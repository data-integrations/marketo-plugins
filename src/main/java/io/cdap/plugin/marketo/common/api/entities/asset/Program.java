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
 * Program entity.
 */
@Entity
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

  public Program() {
  }

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

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for Program entity.
   */
  public static class Builder {

    private String channel;
    private String createdAt;
    private String description;
    private FolderDescriptor folder;
    private Integer id;
    private String name;
    private String sfdcId;
    private String sfdcName;
    private String status;
    private String type;
    private String updatedAt;
    private String url;
    private String workspace;

    private Builder() {
    }

    public Builder channel(String channel) {
      this.channel = channel;
      return Builder.this;
    }

    public Builder createdAt(String createdAt) {
      this.createdAt = createdAt;
      return Builder.this;
    }

    public Builder description(String description) {
      this.description = description;
      return Builder.this;
    }

    public Builder folder(FolderDescriptor folder) {
      this.folder = folder;
      return Builder.this;
    }

    public Builder id(Integer id) {
      this.id = id;
      return Builder.this;
    }

    public Builder name(String name) {
      this.name = name;
      return Builder.this;
    }

    public Builder sfdcId(String sfdcId) {
      this.sfdcId = sfdcId;
      return Builder.this;
    }

    public Builder sfdcName(String sfdcName) {
      this.sfdcName = sfdcName;
      return Builder.this;
    }

    public Builder status(String status) {
      this.status = status;
      return Builder.this;
    }

    public Builder type(String type) {
      this.type = type;
      return Builder.this;
    }

    public Builder updatedAt(String updatedAt) {
      this.updatedAt = updatedAt;
      return Builder.this;
    }

    public Builder url(String url) {
      this.url = url;
      return Builder.this;
    }

    public Builder workspace(String workspace) {
      this.workspace = workspace;
      return Builder.this;
    }

    public Program build() {

      return new Program(this);
    }
  }

  private Program(Builder builder) {
    this.channel = builder.channel;
    this.createdAt = builder.createdAt;
    this.description = builder.description;
    this.folder = builder.folder;
    this.id = builder.id;
    this.name = builder.name;
    this.sfdcId = builder.sfdcId;
    this.sfdcName = builder.sfdcName;
    this.status = builder.status;
    this.type = builder.type;
    this.updatedAt = builder.updatedAt;
    this.url = builder.url;
    this.workspace = builder.workspace;
  }
}
