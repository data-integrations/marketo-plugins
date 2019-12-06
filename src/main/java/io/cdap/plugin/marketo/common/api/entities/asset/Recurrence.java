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

import io.cdap.plugin.marketo.common.api.entities.asset.gen.Entity;

import java.util.Collections;
import java.util.List;

/**
 * Recurrence descriptor.
 */
@Entity
public class Recurrence {
  String startAt;
  String endAt;
  String intervalType;
  Integer interval;
  Boolean weekdayOnly;
  List<String> weekdayMask = Collections.emptyList();
  Integer dayOfMonth;
  Integer dayOfWeek;
  Integer weekOfMonth;

  public String getStartAt() {
    return startAt;
  }

  public String getEndAt() {
    return endAt;
  }

  public String getIntervalType() {
    return intervalType;
  }

  public Integer getInterval() {
    return interval;
  }

  public Boolean getWeekdayOnly() {
    return weekdayOnly;
  }

  public List<String> getWeekdayMask() {
    return weekdayMask;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public Integer getDayOfWeek() {
    return dayOfWeek;
  }

  public Integer getWeekOfMonth() {
    return weekOfMonth;
  }
}
