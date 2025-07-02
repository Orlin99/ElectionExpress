package com.core.electionsystem.exception;

public class UnknownAuthorityException extends RuntimeException {

  public UnknownAuthorityException(String message) {
    super(message);
  }

  public UnknownAuthorityException(String message, Throwable cause) {
    super(message, cause);
  }
}
