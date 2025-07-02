package com.core.electionsystem.elector.exception;

public class InvalidSexParameterException extends RuntimeException {

  public InvalidSexParameterException(String message) {
    super(message);
  }

  public InvalidSexParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}
