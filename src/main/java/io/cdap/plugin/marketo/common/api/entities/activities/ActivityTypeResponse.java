package io.cdap.plugin.marketo.common.api.entities.activities;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Activity type response.
 */
public class ActivityTypeResponse extends BaseResponse {
  /**
   * Activity type attribute.
   */
  public static class ActivityTypeAttribute {
    private String apiName;
    private String dataType;
    private String name;

    public String getApiName() {
      return apiName;
    }

    public String getDataType() {
      return dataType;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return String.format("%s(%s, %s)", getApiName(), getDataType(), getName());
    }
  }

  /**
   * Attribute type.
   */
  public static class ActivityType {
    private String apiName;
    private List<ActivityTypeAttribute> attributes;
    private String description;
    private Integer id;
    private String name;
    private ActivityTypeAttribute primaryAttribute;

    public String getApiName() {
      return apiName;
    }

    public List<ActivityTypeAttribute> getAttributes() {
      return attributes;
    }

    public String getDescription() {
      return description;
    }

    public Integer getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public ActivityTypeAttribute getPrimaryAttribute() {
      return primaryAttribute;
    }
  }

  List<ActivityType> result = Collections.emptyList();

  public List<ActivityType> getResult() {
    return result;
  }
}
