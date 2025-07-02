package com.core.electionsystem.elector.exception;

public class NonExistentElectorException extends RuntimeException {

  public NonExistentElectorException(String message) {
    super(message);
  }

  public NonExistentElectorException(String message, Throwable cause) {
    super(message, cause);
  }
}
