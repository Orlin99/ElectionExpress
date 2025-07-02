package com.core.electionsystem.elector.exception;

public class AlreadyUsedDocumentIdException extends RuntimeException {

  public AlreadyUsedDocumentIdException(String message) {
    super(message);
  }

  public AlreadyUsedDocumentIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
