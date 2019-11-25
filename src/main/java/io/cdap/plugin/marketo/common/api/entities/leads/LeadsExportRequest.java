package io.cdap.plugin.marketo.common.api.entities.leads;

import java.util.List;
import java.util.Map;

/**
 * Represents leads bulk export request.
 */
public class LeadsExportRequest {
  /**
   * Represents date range.
   */
  public static class DateRange {
    String endAt = null;
    String startAt = null;

    public DateRange(String startAt, String endAt) {
      this.endAt = endAt;
      this.startAt = startAt;
    }
  }

  /**
   * Represents request filter.
   */
  public static class ExportLeadFilter {
    DateRange createdAt = null;
    Integer smartListId = null;
    String smartListName = null;
    Integer staticListId = null;
    Integer staticListName = null;
    DateRange updatedAt = null;

    /**
     * Builder for ExportLeadFilter.
     */
    public static class Builder {
      private DateRange createdAt = null;
      private Integer smartListId = null;
      private String smartListName = null;
      private Integer staticListId = null;
      private Integer staticListName = null;
      private DateRange updatedAt = null;

      public Builder() {
      }

      public Builder createdAt(DateRange createdAt) {
        this.createdAt = createdAt;
        return Builder.this;
      }

      public Builder smartListId(Integer smartListId) {
        this.smartListId = smartListId;
        return Builder.this;
      }

      public Builder smartListName(String smartListName) {
        this.smartListName = smartListName;
        return Builder.this;
      }

      public Builder staticListId(Integer staticListId) {
        this.staticListId = staticListId;
        return Builder.this;
      }

      public Builder staticListName(Integer staticListName) {
        this.staticListName = staticListName;
        return Builder.this;
      }

      public Builder updatedAt(DateRange updatedAt) {
        this.updatedAt = updatedAt;
        return Builder.this;
      }

      public ExportLeadFilter build() {

        return new ExportLeadFilter(this);
      }
    }

    private ExportLeadFilter(Builder builder) {
      this.createdAt = builder.createdAt;
      this.smartListId = builder.smartListId;
      this.smartListName = builder.smartListName;
      this.staticListId = builder.staticListId;
      this.staticListName = builder.staticListName;
      this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
      return new Builder();
    }
  }

  Map<String, String> columnHeaderNames = null;
  List<String> fields = null;
  ExportLeadFilter filter = null;
  String format = "CSV";

  public LeadsExportRequest(List<String> fields, ExportLeadFilter filter) {
    this.fields = fields;
    this.filter = filter;
  }
}
