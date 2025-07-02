package com.core.electionsystem.elector.exception;

public class IllegalElectorPropertyException extends RuntimeException {

  public IllegalElectorPropertyException(String message) {
    super(message);
  }

  public IllegalElectorPropertyException(String message, Throwable cause) {
    super(message, cause);
  }
}
