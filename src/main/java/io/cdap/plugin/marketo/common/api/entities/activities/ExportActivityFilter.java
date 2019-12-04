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
