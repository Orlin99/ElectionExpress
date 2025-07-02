package com.core.electionsystem.exception;

public class InvalidSecurityAlgorithmException extends RuntimeException {

  public InvalidSecurityAlgorithmException(String message) {
    super(message);
  }

  public InvalidSecurityAlgorithmException(String message, Throwable cause) {
    super(message, cause);
  }
}
