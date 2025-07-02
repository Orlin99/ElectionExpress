package com.core.electionsystem.utility;

import com.core.electionsystem.exception.InvalidValueForTokenTypeAttributeException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TokenTypeAttributeConverter implements AttributeConverter<TokenType, String> {

  @Override
  public String convertToDatabaseColumn(TokenType attribute) {
    if (attribute == null) {
      throw new InvalidValueForTokenTypeAttributeException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_TOKEN_TYPE_ATTRIBUTE);
    }
    return attribute.getTypeOfToken();
  }

  @Override
  public TokenType convertToEntityAttribute(String databaseData) {
    if (databaseData == null) {
      throw new InvalidValueForTokenTypeAttributeException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_TOKEN_TYPE_ATTRIBUTE);
    }
    return TokenType.fromTypeOfToken(databaseData);
  }
}
