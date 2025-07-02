package com.core.electionsystem.elector.exception;

public class InvalidDateParameterException extends RuntimeException {

  public InvalidDateParameterException(String message) {
    super(message);
  }

  public InvalidDateParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}
