package com.core.electionsystem.exception;

public class QrCodeGenerationFailureException extends RuntimeException {

  public QrCodeGenerationFailureException(String message) {
    super(message);
  }

  public QrCodeGenerationFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
