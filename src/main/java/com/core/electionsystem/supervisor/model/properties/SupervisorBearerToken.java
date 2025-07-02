package com.core.electionsystem.supervisor.model.properties;

import java.util.Objects;

import com.core.electionsystem.exception.IllegalStateForTokenOwnerException;
import com.core.electionsystem.exception.IllegalStateForTokenTypeException;
import com.core.electionsystem.exception.IllegalStateForTokenValueException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.TokenType;
import com.core.electionsystem.utility.TokenTypeAttributeConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "SupervisorBearerToken")
@Table(name = "supervisor_bearer_token", uniqueConstraints = {
    @UniqueConstraint(name = "supervisor_bearer_token_value_unique", columnNames = "supervisor_bearer_token_value") })
public class SupervisorBearerToken {

  @Id
  @SequenceGenerator(name = "supervisor_bearer_token_sequence", sequenceName = "supervisor_bearer_token_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisor_bearer_token_sequence")
  private Long supervisorBearerTokenId;

  @Column(name = "supervisor_bearer_token_value", nullable = false, columnDefinition = "TEXT")
  private String supervisorBearerTokenValue;

  @Column(name = "token_type", nullable = false, columnDefinition = "TEXT")
  @Convert(converter = TokenTypeAttributeConverter.class)
  private TokenType tokenType;

  @Column(name = "is_expired", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isExpired;

  @Column(name = "is_revoked", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isRevoked;

  @JsonBackReference
  @ManyToOne(optional = false)
  @JoinColumn(name = "id", nullable = false)
  private Supervisor supervisor;

  private SupervisorBearerToken(Builder builder) {
    this.supervisorBearerTokenId = builder.supervisorBearerTokenId;
    this.supervisorBearerTokenValue = builder.supervisorBearerTokenValue;
    this.tokenType = builder.tokenType;
    this.isExpired = builder.isExpired;
    this.isRevoked = builder.isRevoked;
    this.supervisor = builder.supervisor;
  }

  public SupervisorBearerToken() {
    // Default Empty Constructor
  }

  public Long getSupervisorBearerTokenId() {
    return supervisorBearerTokenId;
  }

  public String getSupervisorBearerTokenValue() {
    return supervisorBearerTokenValue;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public boolean isExpired() {
    return isExpired;
  }

  public void setExpired(boolean isExpired) {
    this.isExpired = isExpired;
  }

  public boolean isRevoked() {
    return isRevoked;
  }

  public void setRevoked(boolean isRevoked) {
    this.isRevoked = isRevoked;
  }

  public Supervisor getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(Supervisor supervisor) {
    this.supervisor = supervisor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(supervisorBearerTokenId, supervisorBearerTokenValue, tokenType, isExpired, isRevoked, supervisor.getId());
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
    SupervisorBearerToken other = (SupervisorBearerToken) object;
    return Objects.equals(supervisorBearerTokenId, other.supervisorBearerTokenId)
        && Objects.equals(supervisorBearerTokenValue, other.supervisorBearerTokenValue) && (tokenType == other.tokenType)
        && (isExpired == other.isExpired) && (isRevoked == other.isRevoked) && Objects.equals(supervisor.getId(), other.supervisor.getId());
  }

  @Override
  public String toString() {
    return "SupervisorBearerToken [supervisorBearerTokenId=" + supervisorBearerTokenId + ", supervisorBearerTokenValue=" + supervisorBearerTokenValue
        + ", tokenType=" + tokenType + ", isExpired=" + isExpired + ", isRevoked=" + isRevoked + ", supervisor=" + supervisor.getId() + "]";
  }

  @SuppressWarnings("hiding")
  public static final class Builder {

    private Long supervisorBearerTokenId;
    private String supervisorBearerTokenValue;
    private TokenType tokenType;
    private boolean isExpired;
    private boolean isRevoked;
    private Supervisor supervisor;

    public Builder() {
      // Default Empty Constructor
    }

    public Builder supervisorBearerTokenId(Long supervisorBearerTokenId) {
      this.supervisorBearerTokenId = supervisorBearerTokenId;
      return this;
    }

    public Builder supervisorBearerTokenValue(String supervisorBearerTokenValue) {
      this.supervisorBearerTokenValue = supervisorBearerTokenValue;
      return this;
    }

    public Builder tokenType(TokenType tokenType) {
      this.tokenType = tokenType;
      return this;
    }

    public Builder isExpired(boolean isExpired) {
      this.isExpired = isExpired;
      return this;
    }

    public Builder isRevoked(boolean isRevoked) {
      this.isRevoked = isRevoked;
      return this;
    }

    public Builder supervisor(Supervisor supervisor) {
      this.supervisor = supervisor;
      return this;
    }

    public SupervisorBearerToken build() {
      if ((supervisorBearerTokenValue == null) || supervisorBearerTokenValue.isEmpty()) {
        throw new IllegalStateForTokenValueException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_VALUE_EXCEPTION);
      }
      if (tokenType == null) {
        throw new IllegalStateForTokenTypeException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_TYPE_EXCEPTION);
      }
      if (supervisor == null) {
        throw new IllegalStateForTokenOwnerException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_OWNER_EXCEPTION);
      }
      return new SupervisorBearerToken(this);
    }

    @Override
    public int hashCode() {
      return Objects.hash(supervisorBearerTokenId, supervisorBearerTokenValue, tokenType, isExpired, isRevoked, supervisor.getId());
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
      Builder other = (Builder) object;
      return Objects.equals(supervisorBearerTokenId, other.supervisorBearerTokenId)
          && Objects.equals(supervisorBearerTokenValue, other.supervisorBearerTokenValue) && (tokenType == other.tokenType)
          && (isExpired == other.isExpired) && (isRevoked == other.isRevoked) && Objects.equals(supervisor.getId(), other.supervisor.getId());
    }

    @Override
    public String toString() {
      return "Builder [supervisorBearerTokenId=" + supervisorBearerTokenId + ", supervisorBearerTokenValue=" + supervisorBearerTokenValue
          + ", tokenType=" + tokenType + ", isExpired=" + isExpired + ", isRevoked=" + isRevoked + ", supervisor=" + supervisor.getId() + "]";
    }
  }
}
