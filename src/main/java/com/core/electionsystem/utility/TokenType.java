package com.core.electionsystem.utility;

public enum TokenType {
  BEARER("Bearer");

  private static final String MESSAGE_FOR_UNKNOWN_TOKEN_TYPE = "Unknown Token Type: ";

  private String typeOfToken;

  TokenType(String typeOfToken) {
    this.typeOfToken = typeOfToken;
  }

  public String getTypeOfToken() {
    return typeOfToken;
  }

  public static TokenType fromTypeOfToken(String typeOfToken) {
    for (TokenType tokenType : TokenType.values()) {
      if (tokenType.typeOfToken.equals(typeOfToken)) {
        return tokenType;
      }
    }
    throw new IllegalArgumentException(MESSAGE_FOR_UNKNOWN_TOKEN_TYPE + typeOfToken);
  }

  public static TokenType fromString(String tokenType) {
    String tokenTypeToUpperCase = tokenType.toUpperCase();
    return new TokenTypeAttributeConverter().convertToEntityAttribute(tokenTypeToUpperCase);
  }

  @Override
  public String toString() {
    return "TokenType [typeOfToken=" + typeOfToken + "]";
  }
}
