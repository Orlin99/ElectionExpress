package com.core.electionsystem.elector.exception;

public class InvalidDocumentIdException extends RuntimeException {

  public InvalidDocumentIdException(String message) {
    super(message);
  }

  public InvalidDocumentIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
