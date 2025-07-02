package com.core.electionsystem.exception;

public class NonExistentJsonWebTokenException extends RuntimeException {

  public NonExistentJsonWebTokenException(String message) {
    super(message);
  }

  public NonExistentJsonWebTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
