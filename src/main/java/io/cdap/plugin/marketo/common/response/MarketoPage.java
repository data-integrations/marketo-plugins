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

package io.cdap.plugin.marketo.common.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Marketo page response.
 */
public class MarketoPage {
  /**
   * Error or warn message description.
   */
  public static class Description {
    private String code;
    private String message;

    public String getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }
  }

  private Boolean moreResult = null;
  private String nextPageToken = null;
  private String requestId = null;
  private Boolean success = null;

  private List<Description> errors = Collections.emptyList();
  private List<Description> warnings = Collections.emptyList();

  private List<Map<String, Object>> result = Collections.emptyList();

  public Boolean hasMoreResults() {
    return moreResult;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public String getRequestId() {
    return requestId;
  }

  public Boolean isSuccess() {
    return success;
  }

  public List<Description> getErrors() {
    return errors;
  }

  public List<Description> getWarnings() {
    return warnings;
  }

  public List<Map<String, Object>> getResults() {
    return result;
  }
}
