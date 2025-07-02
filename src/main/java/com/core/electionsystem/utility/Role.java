package com.core.electionsystem.utility;

public enum Role {
  SUPERVISOR("supervisor"), ELECTOR("elector");

  private static final String MESSAGE_FOR_UNKNOWN_ROLE_VALUE = "Unknown Role Value: ";

  private String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Role fromValue(String value) {
    for (Role role : Role.values()) {
      if (role.value.equals(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException(MESSAGE_FOR_UNKNOWN_ROLE_VALUE + value);
  }
}
