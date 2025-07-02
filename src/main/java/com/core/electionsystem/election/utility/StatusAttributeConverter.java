package com.core.electionsystem.election.utility;

import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.exception.InvalidValueForStatusAttributeException;
import com.core.electionsystem.utility.ElectionSystemUtility;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusAttributeConverter implements AttributeConverter<Status, String> {

  @Override
  public String convertToDatabaseColumn(Status attribute) {
    if (attribute == null) {
      throw new InvalidValueForStatusAttributeException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_STATUS_ATTRIBUTE);
    }
    return attribute.getValue();
  }

  @Override
  public Status convertToEntityAttribute(String databaseData) {
    if (databaseData == null) {
      throw new InvalidValueForStatusAttributeException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_STATUS_ATTRIBUTE);
    }
    return Status.fromValue(databaseData);
  }
}
