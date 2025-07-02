package com.core.electionsystem.exception;

public class InvalidValueForNameException extends RuntimeException {

  public InvalidValueForNameException(String message) {
    super(message);
  }

  public InvalidValueForNameException(String message, Throwable cause) {
    super(message, cause);
  }
}
