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

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Represents activities bulk export response.
 */
public class ActivitiesExportResponse extends BaseResponse {

  List<ActivitiesExport> result = Collections.emptyList();

  public ActivitiesExport singleExport() {
    if (result.size() != 1) {
      throw new IllegalStateException(
        String.format("Expected single export job result, but found '%s' results.", result.size()));
    }
    return result.get(0);
  }

  public List<ActivitiesExport> getResult() {
    return result;
  }

}
