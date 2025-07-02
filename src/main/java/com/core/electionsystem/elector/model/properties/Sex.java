package com.core.electionsystem.elector.model.properties;

import com.core.electionsystem.elector.exception.EmptySexParameterException;
import com.core.electionsystem.elector.exception.InvalidSexParameterLengthException;
import com.core.electionsystem.elector.utility.SexAttributeConverter;
import com.core.electionsystem.utility.ElectionSystemUtility;

public enum Sex {
  MALE('M'), FEMALE('F');

  public static final String MESSAGE_FOR_INVALID_SEX_PARAMETER = "Invalid Sex Parameter. It Must Be 'M' Or 'F'";

  private static final String MESSAGE_FOR_UNKNOWN_CODE = "Unknown Code: ";
  private static final int ONLY_VALID_INPUT_POSITION = 0;
  private static final String MESSAGE_FOR_EMPTY_SEX_PARAMETER = "Missing Sex Parameter. It Must Be 'M' Or 'F'";
  private static final int SEX_DEFAULT_LENGTH = 1;
  private static final String MESSAGE_FOR_INVALID_SEX_PARAMETER_LENGTH = "Sex Parameter's Length Must Be 1";

  private Character code;

  Sex(Character code) {
    this.code = code;
  }

  public Character getCode() {
    return code;
  }

  public static Sex fromCode(Character code) {
    for (Sex sex : Sex.values()) {
      if (sex.code.equals(code)) {
        return sex;
      }
    }
    throw new IllegalArgumentException(MESSAGE_FOR_UNKNOWN_CODE + code);
  }

  public static Sex fromString(String sex) {
    char sexToUpperCaseChar = Character.toUpperCase(sex.charAt(ONLY_VALID_INPUT_POSITION));
    return new SexAttributeConverter().convertToEntityAttribute(sexToUpperCaseChar);
  }

  public static void validateSex(String sex) {
    boolean sexIsEmpty = ElectionSystemUtility.isNullOrBlank(sex);
    if (sexIsEmpty) {
      throw new EmptySexParameterException(MESSAGE_FOR_EMPTY_SEX_PARAMETER);
    }
    if (sex.length() != SEX_DEFAULT_LENGTH) {
      throw new InvalidSexParameterLengthException(MESSAGE_FOR_INVALID_SEX_PARAMETER_LENGTH);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(this.code);
  }
}
