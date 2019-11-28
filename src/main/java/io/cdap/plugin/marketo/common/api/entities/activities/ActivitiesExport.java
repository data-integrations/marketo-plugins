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

package io.cdap.plugin.marketo.common.api.entities.activities;

/**
 * Represents export response item.
 */
public class ActivitiesExport {
  String createdAt;
  String errorMsg;
  String exportId;
  int fileSize;
  String fileChecksum;
  String finishedAt;
  String format;
  int numberOfRecords;
  String queuedAt;
  String startedAt;
  String status;

  public String getCreatedAt() {
    return createdAt;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public String getExportId() {
    return exportId;
  }

  public int getFileSize() {
    return fileSize;
  }

  public String getFileChecksum() {
    return fileChecksum;
  }

  public String getFinishedAt() {
    return finishedAt;
  }

  public String getFormat() {
    return format;
  }

  public int getNumberOfRecords() {
    return numberOfRecords;
  }

  public String getQueuedAt() {
    return queuedAt;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public String getStatus() {
    return status;
  }
}
