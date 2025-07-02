package com.core.electionsystem.elector.exception;

public class InvalidValueForEgnException extends RuntimeException {

  public InvalidValueForEgnException(String message) {
    super(message);
  }

  public InvalidValueForEgnException(String message, Throwable cause) {
    super(message, cause);
  }
}
