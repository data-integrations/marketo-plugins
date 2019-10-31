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

/**
 * Marketo field description.
 */
public class Field {
  /**
   * Soap or rest descriptor.
   */
  public static class FieldDescription {
    String name;
    Boolean readOnly;
  }

  String id = null;
  private String name = null;
  String displayName = null;
  String dataType = null;
  int length = -1;
  Boolean updateable = null;
  FieldDescription rest = null;
  FieldDescription soap = null;

  public String getName() {
    if (name == null) {
      if (rest == null) {
        throw new RuntimeException("Failed to get name for field.");
      } else {
        return rest.name;
      }
    } else {
      return name;
    }
  }
}
