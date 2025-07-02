package com.core.electionsystem.dto;

import java.util.Arrays;
import java.util.Objects;

public class PhoneNumberHolderDTO {

  private char[] inputPassword;
  private String newPhoneNumber;

  public PhoneNumberHolderDTO() {
    // Default Empty Constructor
  }

  public PhoneNumberHolderDTO(char[] inputPassword, String newPhoneNumber) {
    this.inputPassword = inputPassword;
    this.newPhoneNumber = newPhoneNumber;
  }

  public char[] getInputPassword() {
    return inputPassword;
  }

  public void setInputPassword(char[] inputPassword) {
    this.inputPassword = inputPassword;
  }

  public String getNewPhoneNumber() {
    return newPhoneNumber;
  }

  public void setNewPhoneNumber(String newPhoneNumber) {
    this.newPhoneNumber = newPhoneNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(inputPassword);
    result = (prime * result) + Objects.hash(newPhoneNumber);
    return result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (getClass() != object.getClass()) {
      return false;
    }
    PhoneNumberHolderDTO other = (PhoneNumberHolderDTO) object;
    return Arrays.equals(inputPassword, other.inputPassword) && Objects.equals(newPhoneNumber, other.newPhoneNumber);
  }

  @Override
  public String toString() {
    return "PhoneNumberHolderDTO [inputPassword=" + Arrays.toString(inputPassword) + ", newPhoneNumber=" + newPhoneNumber + "]";
  }
}
