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

package io.cdap.plugin.marketo.common.response.describe;

import java.util.Collections;
import java.util.List;

/**
 * Regular describe response.
 * Fields located in first object in 'result' field.
 */
public class DescribeResponse {
  /**
   * Result holder.
   */
  public static class Result {
    List<Field> fields = Collections.emptyList();
  }

  private String requestId = null;
  private Boolean success = null;

  private List<Result> result = Collections.emptyList();

  public List<Field> getFields() {
    if (result.size() != 1) {
      throw new RuntimeException("Expected to have one result.");
    }
    return result.get(0).fields;
  }
}
