package com.core.electionsystem.elector.exception;

public class EmptyDateParameterException extends RuntimeException {

  public EmptyDateParameterException(String message) {
    super(message);
  }

  public EmptyDateParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}
