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

import java.util.Collections;
import java.util.List;

/**
 * Email entity.
 */
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
}
