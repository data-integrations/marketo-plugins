package io.cdap.plugin.marketo.common.api.entities.activities;

import java.util.List;
import java.util.Map;

/**
 * Represents activities bulk export request.
 */
public class ActivitiesExportRequest {

  Map<String, String> columnHeaderNames = null;
  List<String> fields = null;
  ExportActivityFilter filter = null;
  String format = "CSV";

  public ActivitiesExportRequest(List<String> fields, ExportActivityFilter filter) {
    this.fields = fields;
    this.filter = filter;
  }
}
