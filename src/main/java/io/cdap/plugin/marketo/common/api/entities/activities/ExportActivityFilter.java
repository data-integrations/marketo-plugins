package io.cdap.plugin.marketo.common.api.entities.activities;

import io.cdap.plugin.marketo.common.api.entities.DateRange;

import java.util.List;

/**
 * Represents request filter.
 */
public class ExportActivityFilter {
  private List<Integer> activityTypeIds;
  private DateRange createdAt;

  /**
   * Builder.
   */
  public static class Builder {

    private List<Integer> activityTypeIds = null;
    private DateRange createdAt = null;

    public Builder() {
    }

    Builder(List<Integer> activityTypeIds, DateRange createdAt) {
      this.activityTypeIds = activityTypeIds;
      this.createdAt = createdAt;
    }

    public Builder activityTypeIds(List<Integer> activityTypeIds) {
      this.activityTypeIds = activityTypeIds;
      return Builder.this;
    }

    public Builder addActivityTypeIds(Integer activityTypeIds) {
      this.activityTypeIds.add(activityTypeIds);
      return Builder.this;
    }

    public Builder createdAt(DateRange createdAt) {
      this.createdAt = createdAt;
      return Builder.this;
    }

    public ExportActivityFilter build() {

      return new ExportActivityFilter(this);
    }
  }

  private ExportActivityFilter(Builder builder) {
    this.activityTypeIds = builder.activityTypeIds;
    this.createdAt = builder.createdAt;
  }

  public static Builder builder() {
    return new Builder();
  }
}
