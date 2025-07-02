package com.core.electionsystem.exception;

public class InvalidValueForSecretAnswerException extends RuntimeException {

  public InvalidValueForSecretAnswerException(String message) {
    super(message);
  }

  public InvalidValueForSecretAnswerException(String message, Throwable cause) {
    super(message, cause);
  }
}
