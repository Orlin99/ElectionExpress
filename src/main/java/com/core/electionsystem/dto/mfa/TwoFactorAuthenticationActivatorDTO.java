package com.core.electionsystem.dto.mfa;

import java.util.Arrays;

public class TwoFactorAuthenticationActivatorDTO {

  private char[] inputPassword;
  private char[] totpCode;

  public TwoFactorAuthenticationActivatorDTO() {
    // Default Empty Constructor
  }

  public TwoFactorAuthenticationActivatorDTO(char[] inputPassword, char[] totpCode) {
    this.inputPassword = inputPassword;
    this.totpCode = totpCode;
  }

  public char[] getInputPassword() {
    return inputPassword;
  }

  public void setInputPassword(char[] inputPassword) {
    this.inputPassword = inputPassword;
  }

  public char[] getTotpCode() {
    return totpCode;
  }

  public void setTotpCode(char[] totpCode) {
    this.totpCode = totpCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(inputPassword);
    result = (prime * result) + Arrays.hashCode(totpCode);
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
    TwoFactorAuthenticationActivatorDTO other = (TwoFactorAuthenticationActivatorDTO) object;
    return Arrays.equals(inputPassword, other.inputPassword) && Arrays.equals(totpCode, other.totpCode);
  }

  @Override
  public String toString() {
    return "TwoFactorAuthenticationActivatorDTO [inputPassword=" + Arrays.toString(inputPassword) + ", totpCode=" + Arrays.toString(totpCode) + "]";
  }
}
