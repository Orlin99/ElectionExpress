package com.core.electionsystem.elector.exception;

public class EmptyNameParameterException extends RuntimeException {

  public EmptyNameParameterException(String message) {
    super(message);
  }

  public EmptyNameParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}
