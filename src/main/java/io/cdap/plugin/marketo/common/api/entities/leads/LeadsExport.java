package io.cdap.plugin.marketo.common.api.entities.leads;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Represents leads bulk export response.
 */
public class LeadsExport extends BaseResponse {
  /**
   * Represents export response item.
   */
  public static class ExportResponse {
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

  List<ExportResponse> result = Collections.emptyList();

  public ExportResponse singleExport() {
    if (result.size() != 1) {
      throw new IllegalStateException("Expected single export job result.");
    }
    return result.get(0);
  }

}
