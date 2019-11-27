package io.cdap.plugin.marketo.source.batch;

/**
 * Represents report type.
 */
public enum ReportType {
  LEADS("leads"),
  ACTIVITIES("activities");

  private String type;

  ReportType(String type) {
    this.type = type;
  }

  public static ReportType fromString(String reportType) {
    for (ReportType rt : ReportType.values()) {
      if (rt.type.equals(reportType)) {
        return rt;
      }
    }
    throw new IllegalArgumentException("unknown report type: " + reportType);
  }
}
