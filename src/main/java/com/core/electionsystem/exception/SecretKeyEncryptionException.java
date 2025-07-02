package com.core.electionsystem.exception;

public class SecretKeyEncryptionException extends RuntimeException {

  public SecretKeyEncryptionException(String message) {
    super(message);
  }

  public SecretKeyEncryptionException(String message, Throwable cause) {
    super(message, cause);
  }
}
