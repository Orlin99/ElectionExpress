package com.core.electionsystem.exception;

public class IncorrectSecretAnswerException extends RuntimeException {

  public IncorrectSecretAnswerException(String message) {
    super(message);
  }

  public IncorrectSecretAnswerException(String message, Throwable cause) {
    super(message, cause);
  }
}
