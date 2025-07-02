package com.core.electionsystem.exception;

public class InvalidArgumentsForElectorException extends RuntimeException {

  public InvalidArgumentsForElectorException(String message) {
    super(message);
  }

  public InvalidArgumentsForElectorException(String message, Throwable cause) {
    super(message, cause);
  }
}
