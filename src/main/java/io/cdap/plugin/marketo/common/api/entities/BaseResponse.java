package io.cdap.plugin.marketo.common.api.entities;

import java.util.Collections;
import java.util.List;

/**
 * Represents common parts for all responses.
 */
public class BaseResponse {

  private boolean success = false;
  private List<Error> errors = Collections.emptyList();
  private List<Warning> warnings = Collections.emptyList();
  private String requestId;
  private boolean moreResult = false;
  private String nextPageToken;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<Error> getErrors() {
    return errors;
  }

  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  public List<Warning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<Warning> warnings) {
    this.warnings = warnings;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public boolean isMoreResult() {
    return moreResult;
  }

  public void setMoreResult(boolean moreResult) {
    this.moreResult = moreResult;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(String nextPageToken) {
    this.nextPageToken = nextPageToken;
  }
}
