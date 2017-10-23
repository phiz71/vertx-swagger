package io.swagger.server.api.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ErrorCode
 */
public enum ErrorCode {
  
  REQUIRED_RESOURCE_ID_NOT_SET("REQUIRED_RESOURCE_ID_NOT_SET"),
  
  REQUIRED_RESOURCE_NOT_FOUND("REQUIRED_RESOURCE_NOT_FOUND"),
  
  ALREADY_EXISTS("ALREADY_EXISTS"),
  
  SOMETHING_ELSE("SOMETHING_ELSE");

  private String value;

  ErrorCode(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ErrorCode fromValue(String text) {
    for (ErrorCode b : ErrorCode.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}