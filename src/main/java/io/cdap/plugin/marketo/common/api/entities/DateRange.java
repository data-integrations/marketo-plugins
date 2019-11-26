package io.cdap.plugin.marketo.common.api.entities;

/**
 * Represents date range.
 */
public class DateRange {
  String endAt;
  String startAt;

  public DateRange() {

  }

  public DateRange(String startAt, String endAt) {
    this.endAt = endAt;
    this.startAt = startAt;
  }

  public String getEndAt() {
    return endAt;
  }

  public String getStartAt() {
    return startAt;
  }

  @Override
  public String toString() {
    return startAt + " -- " + endAt;
  }
}
