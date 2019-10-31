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

package io.cdap.plugin.marketo.common;

import java.util.Arrays;

/**
 * Marketo Entity types.
 */
public enum MarketoEntity {
  Leads("Leads", "GET /rest/v1/leads.json", "/rest/v1/leads/describe.json"),
  Campaigns("Campaigns", "/rest/v1/campaigns.json", null),
  Companies("Companies", "/rest/v1/companies.json", "/rest/v1/companies/describe.json");

  private String name;
  private String getEndpoint;
  private String describeEndpoint;

  MarketoEntity(String name, String getEndpoint, String describeEndpoint) {
    this.name = name;
    this.getEndpoint = getEndpoint;
    this.describeEndpoint = describeEndpoint;
  }

  public String getName() {
    return name;
  }

  public String getGetEndpoint() {
    return getEndpoint;
  }

  public String getDescribeEndpoint() {
    return describeEndpoint;
  }

  public static MarketoEntity fromString(String marketoEntity) {
    return Arrays.stream(MarketoEntity.values())
      .filter(entity -> entity.getName().equals(marketoEntity)).findFirst()
      .orElseThrow(() -> new RuntimeException(String.format("'%s' is not a valid Marketo entity", marketoEntity)));
  }
}
