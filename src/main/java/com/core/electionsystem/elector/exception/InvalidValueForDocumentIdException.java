package com.core.electionsystem.elector.exception;

public class InvalidValueForDocumentIdException extends RuntimeException {

  public InvalidValueForDocumentIdException(String message) {
    super(message);
  }

  public InvalidValueForDocumentIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
