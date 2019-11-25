package io.cdap.plugin.marketo.common.api.entities;

/**
 * Represents warning message.
 */
public class Warning {
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
