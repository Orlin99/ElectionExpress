package com.core.electionsystem.election.model.properties.preference;

import java.io.Serializable;
import java.util.Objects;

import com.core.electionsystem.election.model.properties.candidate.CandidateCompositeId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class PreferenceCompositeId implements Serializable {

  @Column(name = "preference_id_piece", nullable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer preferenceIdPiece;

  @Embedded
  private CandidateCompositeId candidateCompositeId;

  public PreferenceCompositeId() {
    // Default Empty Constructor
  }

  public PreferenceCompositeId(Integer preferenceIdPiece, CandidateCompositeId candidateCompositeId) {
    this.preferenceIdPiece = preferenceIdPiece;
    this.candidateCompositeId = candidateCompositeId;
  }

  public Integer getPreferenceIdPiece() {
    return preferenceIdPiece;
  }

  public void setPreferenceIdPiece(Integer preferenceIdPiece) {
    this.preferenceIdPiece = preferenceIdPiece;
  }

  public CandidateCompositeId getCandidateCompositeId() {
    return candidateCompositeId;
  }

  public void setCandidateCompositeId(CandidateCompositeId candidateCompositeId) {
    this.candidateCompositeId = candidateCompositeId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferenceIdPiece, candidateCompositeId);
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
    PreferenceCompositeId other = (PreferenceCompositeId) object;
    return Objects.equals(preferenceIdPiece, other.preferenceIdPiece) && Objects.equals(candidateCompositeId, other.candidateCompositeId);
  }

  @Override
  public String toString() {
    return "PreferenceCompositeId [preferenceIdPiece=" + preferenceIdPiece + ", candidateCompositeId=(" + candidateCompositeId.getCandidateIdPiece()
        + ", " + candidateCompositeId.getElectionIdPiece() + ")]";
  }
}
