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

import java.util.List;
import java.util.Map;

/**
 * Represents activities bulk export request.
 */
public class ActivitiesExportRequest {

  Map<String, String> columnHeaderNames = null;
  List<String> fields = null;
  ExportActivityFilter filter = null;
  String format = "CSV";

  public ActivitiesExportRequest(List<String> fields, ExportActivityFilter filter) {
    this.fields = fields;
    this.filter = filter;
  }
}
