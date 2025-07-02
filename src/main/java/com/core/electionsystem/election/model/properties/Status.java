package com.core.electionsystem.election.model.properties;

import com.core.electionsystem.election.utility.StatusAttributeConverter;

public enum Status {
  INACTIVE("inactive"), ACTIVE("active"), COMPLETED("completed");

  private static final String MESSAGE_FOR_UNKNOWN_STATUS_VALUE = "Unknown Status Value: ";

  private String value;

  Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Status fromValue(String value) {
    for (Status status : Status.values()) {
      if (status.value.equals(value)) {
        return status;
      }
    }
    throw new IllegalArgumentException(MESSAGE_FOR_UNKNOWN_STATUS_VALUE + value);
  }

  public static Status fromString(String value) {
    String statusToUpperCase = value.toLowerCase();
    return new StatusAttributeConverter().convertToEntityAttribute(statusToUpperCase);
  }

  @Override
  public String toString() {
    return "Status [value=" + value + "]";
  }
}
