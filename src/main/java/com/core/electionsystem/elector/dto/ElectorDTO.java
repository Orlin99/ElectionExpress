package com.core.electionsystem.elector.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.model.properties.ElectorCredentials;
import com.core.electionsystem.elector.model.properties.ElectorMetadata;
import com.core.electionsystem.elector.model.properties.ElectorName;
import com.core.electionsystem.elector.model.properties.ElectorResidence;
import com.core.electionsystem.elector.model.properties.Sex;

public class ElectorDTO {

  private String electorId;
  private ElectorName electorName;
  private Sex sex;
  private LocalDate dateOfBirth;
  private String documentId;
  private ElectorResidence electorResidence;
  private ElectorCredentials electorCredentials;
  private ElectorMetadata electorMetadata;
  private List<ElectorBearerToken> tokens;

  public ElectorDTO() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public ElectorDTO(String electorId, ElectorName electorName, Sex sex, LocalDate dateOfBirth, String documentId, ElectorResidence electorResidence,
      ElectorCredentials electorCredentials, ElectorMetadata electorMetadata, List<ElectorBearerToken> tokens) {
    this.electorId = electorId;
    this.electorName = electorName;
    this.sex = sex;
    this.dateOfBirth = dateOfBirth;
    this.documentId = documentId;
    this.electorResidence = electorResidence;
    this.electorCredentials = electorCredentials;
    this.electorMetadata = electorMetadata;
    this.tokens = tokens;
  }

  public String getElectorId() {
    return electorId;
  }

  public void setElectorId(String electorId) {
    this.electorId = electorId;
  }

  public ElectorName getElectorName() {
    return electorName;
  }

  public void setElectorName(ElectorName electorName) {
    this.electorName = electorName;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public ElectorResidence getElectorResidence() {
    return electorResidence;
  }

  public void setElectorResidence(ElectorResidence electorResidence) {
    this.electorResidence = electorResidence;
  }

  public ElectorCredentials getElectorCredentials() {
    return electorCredentials;
  }

  public void setElectorCredentials(ElectorCredentials electorCredentials) {
    this.electorCredentials = electorCredentials;
  }

  public ElectorMetadata getElectorMetadata() {
    return electorMetadata;
  }

  public void setElectorMetadata(ElectorMetadata electorMetadata) {
    this.electorMetadata = electorMetadata;
  }

  public List<ElectorBearerToken> getTokens() {
    return tokens;
  }

  public void setTokens(List<ElectorBearerToken> tokens) {
    this.tokens = tokens;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorId, documentId);
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
    ElectorDTO other = (ElectorDTO) object;
    return Objects.equals(electorId, other.electorId) && Objects.equals(documentId, other.documentId);
  }

  @Override
  public String toString() {
    return "ElectorDTO [electorId=" + electorId + ", electorName=" + electorName + ", sex=" + sex + ", dateOfBirth=" + dateOfBirth + ", documentId="
        + documentId + ", electorResidence=" + electorResidence + ", electorCredentials=" + electorCredentials + ", electorMetadata="
        + electorMetadata + ", tokens=" + tokens.size() + "]";
  }
}
