package io.cdap.plugin.marketo.common.api.entities;

/**
 * Represents error message.
 */
public class Error {
  private int code;
  private String message;

  public Error(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public Error() {
  }

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
