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
 * Form entity.
 */
public class Form {
  String buttonLabel;
  Integer buttonLocation;
  String createdAt;
  String description;
  FolderDescriptor folder;
  String fontFamily;
  String fontSize;
  Integer id;
  FormKnownVisitorDTO knownVisitor;
  String labelPosition;
  String language;
  String locale;
  String name;
  Boolean progressiveProfiling;
  String status;
  List<FormThankYouPageDTO> thankYouList = Collections.emptyList();
  String theme;
  String updatedAt;
  String url;
  String waitingLabel;

  public String getButtonLabel() {
    return buttonLabel;
  }

  public Integer getButtonLocation() {
    return buttonLocation;
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

  public String getFontFamily() {
    return fontFamily;
  }

  public String getFontSize() {
    return fontSize;
  }

  public Integer getId() {
    return id;
  }

  public FormKnownVisitorDTO getKnownVisitor() {
    return knownVisitor;
  }

  public String getLabelPosition() {
    return labelPosition;
  }

  public String getLanguage() {
    return language;
  }

  public String getLocale() {
    return locale;
  }

  public String getName() {
    return name;
  }

  public Boolean getProgressiveProfiling() {
    return progressiveProfiling;
  }

  public String getStatus() {
    return status;
  }

  public List<FormThankYouPageDTO> getThankYouList() {
    return thankYouList;
  }

  public String getTheme() {
    return theme;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }

  public String getWaitingLabel() {
    return waitingLabel;
  }
}
