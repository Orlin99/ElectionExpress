package com.core.electionsystem.elector.model.properties;

import java.util.Objects;

import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.exception.IllegalStateForTokenOwnerException;
import com.core.electionsystem.exception.IllegalStateForTokenTypeException;
import com.core.electionsystem.exception.IllegalStateForTokenValueException;
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

@Entity(name = "ElectorBearerToken")
@Table(name = "elector_bearer_token", uniqueConstraints = {
    @UniqueConstraint(name = "elector_bearer_token_value_unique", columnNames = "elector_bearer_token_value") })
public class ElectorBearerToken {

  @Id
  @SequenceGenerator(name = "elector_bearer_token_sequence", sequenceName = "elector_bearer_token_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elector_bearer_token_sequence")
  private Long electorBearerTokenId;

  @Column(name = "elector_bearer_token_value", nullable = false, columnDefinition = "TEXT")
  private String electorBearerTokenValue;

  @Column(name = "token_type", nullable = false, columnDefinition = "TEXT")
  @Convert(converter = TokenTypeAttributeConverter.class)
  private TokenType tokenType;

  @Column(name = "is_expired", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isExpired;

  @Column(name = "is_revoked", nullable = false, columnDefinition = "BOOLEAN")
  private boolean isRevoked;

  @JsonBackReference
  @ManyToOne(optional = false)
  @JoinColumn(name = "elector_id", nullable = false)
  private Elector elector;

  private ElectorBearerToken(Builder builder) {
    this.electorBearerTokenId = builder.electorBearerTokenId;
    this.electorBearerTokenValue = builder.electorBearerTokenValue;
    this.tokenType = builder.tokenType;
    this.isExpired = builder.isExpired;
    this.isRevoked = builder.isRevoked;
    this.elector = builder.elector;
  }

  public ElectorBearerToken() {
    // Default Empty Constructor
  }

  public Long getElectorBearerTokenId() {
    return electorBearerTokenId;
  }

  public String getElectorBearerTokenValue() {
    return electorBearerTokenValue;
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

  public Elector getElector() {
    return elector;
  }

  public void setElector(Elector elector) {
    this.elector = elector;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorBearerTokenId, electorBearerTokenValue, tokenType, isExpired, isRevoked, elector.getElectorId());
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
    ElectorBearerToken other = (ElectorBearerToken) object;
    return Objects.equals(electorBearerTokenId, other.electorBearerTokenId) && Objects.equals(electorBearerTokenValue, other.electorBearerTokenValue)
        && (tokenType == other.tokenType) && (isExpired == other.isExpired) && (isRevoked == other.isRevoked)
        && Objects.equals(elector.getElectorId(), other.elector.getElectorId());
  }

  @Override
  public String toString() {
    return "ElectorBearerToken [electorBearerTokenId=" + electorBearerTokenId + ", electorBearerTokenValue=" + electorBearerTokenValue
        + ", tokenType=" + tokenType + ", isExpired=" + isExpired + ", isRevoked=" + isRevoked + ", elector=" + elector.getElectorId() + "]";
  }

  @SuppressWarnings("hiding")
  public static final class Builder {

    private Long electorBearerTokenId;
    private String electorBearerTokenValue;
    private TokenType tokenType;
    private boolean isExpired;
    private boolean isRevoked;
    private Elector elector;

    public Builder() {
      // Default Empty Constructor
    }

    public Builder electorBearerTokenId(Long electorBearerTokenId) {
      this.electorBearerTokenId = electorBearerTokenId;
      return this;
    }

    public Builder electorBearerTokenValue(String electorBearerTokenValue) {
      this.electorBearerTokenValue = electorBearerTokenValue;
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

    public Builder elector(Elector elector) {
      this.elector = elector;
      return this;
    }

    public ElectorBearerToken build() {
      if ((electorBearerTokenValue == null) || electorBearerTokenValue.isEmpty()) {
        throw new IllegalStateForTokenValueException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_VALUE_EXCEPTION);
      }
      if (tokenType == null) {
        throw new IllegalStateForTokenTypeException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_TYPE_EXCEPTION);
      }
      if (elector == null) {
        throw new IllegalStateForTokenOwnerException(ElectionSystemUtility.MESSAGE_FOR_ILLEGAL_TOKEN_OWNER_EXCEPTION);
      }
      return new ElectorBearerToken(this);
    }

    @Override
    public int hashCode() {
      return Objects.hash(electorBearerTokenId, electorBearerTokenValue, tokenType, isExpired, isRevoked, elector.getElectorId());
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
      return Objects.equals(electorBearerTokenId, other.electorBearerTokenId)
          && Objects.equals(electorBearerTokenValue, other.electorBearerTokenValue) && (tokenType == other.tokenType)
          && (isExpired == other.isExpired) && (isRevoked == other.isRevoked) && Objects.equals(elector.getElectorId(), other.elector.getElectorId());
    }

    @Override
    public String toString() {
      return "Builder [electorBearerTokenId=" + electorBearerTokenId + ", electorBearerTokenValue=" + electorBearerTokenValue + ", tokenType="
          + tokenType + ", isExpired=" + isExpired + ", isRevoked=" + isRevoked + ", elector=" + elector.getElectorId() + "]";
    }
  }
}
