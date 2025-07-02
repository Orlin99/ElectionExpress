package com.core.electionsystem.exception;

public class SecretKeyDecryptionException extends RuntimeException {

  public SecretKeyDecryptionException(String message) {
    super(message);
  }

  public SecretKeyDecryptionException(String message, Throwable cause) {
    super(message, cause);
  }
}
