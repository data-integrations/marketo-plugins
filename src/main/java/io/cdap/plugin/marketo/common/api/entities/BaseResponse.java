package io.cdap.plugin.marketo.common.api.entities;

import java.util.Collections;
import java.util.List;

/**
 * Represents common parts for all responses.
 */
public class BaseResponse {

  private boolean success = false;
  private List<Error> errors = Collections.emptyList();
  private List<Error> warnings = Collections.emptyList();
  private String requestId;
  private boolean moreResult = false;
  private String nextPageToken;

  public boolean isSuccess() {
    return success;
  }

  public List<Error> getErrors() {
    return errors;
  }

  public List<Error> getWarnings() {
    return warnings;
  }

  public String getRequestId() {
    return requestId;
  }

  public boolean isMoreResult() {
    return moreResult;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }
}
