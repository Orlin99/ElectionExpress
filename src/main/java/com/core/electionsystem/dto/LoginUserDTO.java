package com.core.electionsystem.dto;

import java.util.Arrays;
import java.util.Objects;

public class LoginUserDTO {

  private String email;
  private char[] password;

  public LoginUserDTO() {
    // Default Empty Constructor
  }

  public LoginUserDTO(String email, char[] password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public char[] getPassword() {
    return password;
  }

  public void setPassword(char[] password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Objects.hash(email);
    result = (prime * result) + Arrays.hashCode(password);
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
    LoginUserDTO other = (LoginUserDTO) object;
    return Objects.equals(email, other.email) && Arrays.equals(password, other.password);
  }

  @Override
  public String toString() {
    return "LoginUserDTO [email=" + email + ", password=" + Arrays.toString(password) + "]";
  }
}
