package com.core.electionsystem.elector.exception;

public class InvalidEgnException extends RuntimeException {

  public InvalidEgnException(String message) {
    super(message);
  }

  public InvalidEgnException(String message, Throwable cause) {
    super(message, cause);
  }
}
