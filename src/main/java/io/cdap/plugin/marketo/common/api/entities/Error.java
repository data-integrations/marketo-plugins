package io.cdap.plugin.marketo.common.api.entities;

/**
 * Represents error message.
 */
public class Error {
  private int code;
  private String message;

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return String.format("code: %d, message: %s", code, message);
  }
}
