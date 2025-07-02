package com.core.electionsystem.elector.utility;

import com.core.electionsystem.elector.exception.InvalidValueForSexAttributeException;
import com.core.electionsystem.elector.model.properties.Sex;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexAttributeConverter implements AttributeConverter<Sex, Character> {

  @Override
  public Character convertToDatabaseColumn(Sex attribute) {
    if (attribute == null) {
      throw new InvalidValueForSexAttributeException(ElectorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SEX_ATTRIBUTE);
    }
    return attribute.getCode();
  }

  @Override
  public Sex convertToEntityAttribute(Character databaseData) {
    if (databaseData == null) {
      throw new InvalidValueForSexAttributeException(ElectorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SEX_ATTRIBUTE);
    }
    return Sex.fromCode(databaseData);
  }
}
