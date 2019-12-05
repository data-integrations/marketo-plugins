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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Email entity.
 */
@Entity
public class Email {
  String createdAt;
  String description;
  FolderDescriptor folder;
  EmailField fromEmail;
  EmailField fromName;
  Integer id;
  String name;
  Boolean operational;
  Boolean publishToMSI;
  EmailField replyEmail;
  String status;
  EmailField subject;
  Integer template;
  Boolean textOnly;
  String updatedAt;
  String url;
  Integer version;
  Boolean webView;
  String workspace;
  Boolean autoCopyToText;
  List<EmailCCField> ccFields = Collections.emptyList();

  public String getCreatedAt() {
    return createdAt;
  }

  public String getDescription() {
    return description;
  }

  public FolderDescriptor getFolder() {
    return folder;
  }

  public EmailField getFromEmail() {
    return fromEmail;
  }

  public EmailField getFromName() {
    return fromName;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Boolean getOperational() {
    return operational;
  }

  public Boolean getPublishToMSI() {
    return publishToMSI;
  }

  public EmailField getReplyEmail() {
    return replyEmail;
  }

  public String getStatus() {
    return status;
  }

  public EmailField getSubject() {
    return subject;
  }

  public Integer getTemplate() {
    return template;
  }

  public Boolean getTextOnly() {
    return textOnly;
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

  public Boolean getWebView() {
    return webView;
  }

  public String getWorkspace() {
    return workspace;
  }

  public Boolean getAutoCopyToText() {
    return autoCopyToText;
  }

  public List<EmailCCField> getCcFields() {
    return ccFields;
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for Email entity.
   */
  public static class Builder {

    private String createdAt;
    private String description;
    private FolderDescriptor folder;
    private EmailField fromEmail;
    private EmailField fromName;
    private Integer id;
    private String name;
    private Boolean operational;
    private Boolean publishToMSI;
    private EmailField replyEmail;
    private String status;
    private EmailField subject;
    private Integer template;
    private Boolean textOnly;
    private String updatedAt;
    private String url;
    private Integer version;
    private Boolean webView;
    private String workspace;
    private Boolean autoCopyToText;
    private List<EmailCCField> ccFields = new ArrayList<>();

    public Builder() {
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

    public Builder fromEmail(EmailField fromEmail) {
      this.fromEmail = fromEmail;
      return Builder.this;
    }

    public Builder fromName(EmailField fromName) {
      this.fromName = fromName;
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

    public Builder operational(Boolean operational) {
      this.operational = operational;
      return Builder.this;
    }

    public Builder publishToMSI(Boolean publishToMSI) {
      this.publishToMSI = publishToMSI;
      return Builder.this;
    }

    public Builder replyEmail(EmailField replyEmail) {
      this.replyEmail = replyEmail;
      return Builder.this;
    }

    public Builder status(String status) {
      this.status = status;
      return Builder.this;
    }

    public Builder subject(EmailField subject) {
      this.subject = subject;
      return Builder.this;
    }

    public Builder template(Integer template) {
      this.template = template;
      return Builder.this;
    }

    public Builder textOnly(Boolean textOnly) {
      this.textOnly = textOnly;
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

    public Builder version(Integer version) {
      this.version = version;
      return Builder.this;
    }

    public Builder webView(Boolean webView) {
      this.webView = webView;
      return Builder.this;
    }

    public Builder workspace(String workspace) {
      this.workspace = workspace;
      return Builder.this;
    }

    public Builder autoCopyToText(Boolean autoCopyToText) {
      this.autoCopyToText = autoCopyToText;
      return Builder.this;
    }

    public Builder ccFields(List<EmailCCField> ccFields) {
      this.ccFields = ccFields;
      return Builder.this;
    }

    public Builder addCcFields(EmailCCField ccFields) {
      this.ccFields.add(ccFields);
      return Builder.this;
    }

    public Email build() {

      return new Email(this);
    }
  }

  private Email(Builder builder) {
    this.createdAt = builder.createdAt;
    this.description = builder.description;
    this.folder = builder.folder;
    this.fromEmail = builder.fromEmail;
    this.fromName = builder.fromName;
    this.id = builder.id;
    this.name = builder.name;
    this.operational = builder.operational;
    this.publishToMSI = builder.publishToMSI;
    this.replyEmail = builder.replyEmail;
    this.status = builder.status;
    this.subject = builder.subject;
    this.template = builder.template;
    this.textOnly = builder.textOnly;
    this.updatedAt = builder.updatedAt;
    this.url = builder.url;
    this.version = builder.version;
    this.webView = builder.webView;
    this.workspace = builder.workspace;
    this.autoCopyToText = builder.autoCopyToText;
    this.ccFields = builder.ccFields;
  }
}
