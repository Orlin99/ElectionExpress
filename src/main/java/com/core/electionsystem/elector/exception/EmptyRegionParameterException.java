package com.core.electionsystem.elector.exception;

public class EmptyRegionParameterException extends RuntimeException {

  public EmptyRegionParameterException(String message) {
    super(message);
  }

  public EmptyRegionParameterException(String message, Throwable cause) {
    super(message, cause);
  }
}
