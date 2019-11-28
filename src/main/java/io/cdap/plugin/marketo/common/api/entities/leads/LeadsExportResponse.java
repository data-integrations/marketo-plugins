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

package io.cdap.plugin.marketo.common.api.entities.leads;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Collections;
import java.util.List;

/**
 * Represents leads bulk export response.
 */
public class LeadsExportResponse extends BaseResponse {

  List<LeadsExport> result = Collections.emptyList();

  public LeadsExport singleExport() {
    if (result.size() != 1) {
      throw new IllegalStateException("Expected single export job result.");
    }
    return result.get(0);
  }

  public List<LeadsExport> getResult() {
    return result;
  }

}
