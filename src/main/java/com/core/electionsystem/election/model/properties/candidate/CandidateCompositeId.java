package com.core.electionsystem.election.model.properties.candidate;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CandidateCompositeId implements Serializable {

  @Column(name = "candidate_id_piece", nullable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer candidateIdPiece;

  @Column(name = "election_id_piece", nullable = false, updatable = false, columnDefinition = "BIGINT")
  private Long electionIdPiece;

  public CandidateCompositeId() {
    // Default Empty Constructor
  }

  public CandidateCompositeId(Integer candidateIdPiece, Long electionIdPiece) {
    this.candidateIdPiece = candidateIdPiece;
    this.electionIdPiece = electionIdPiece;
  }

  public Integer getCandidateIdPiece() {
    return candidateIdPiece;
  }

  public void setCandidateIdPiece(Integer candidateIdPiece) {
    this.candidateIdPiece = candidateIdPiece;
  }

  public Long getElectionIdPiece() {
    return electionIdPiece;
  }

  public void setElectionIdPiece(Long electionIdPiece) {
    this.electionIdPiece = electionIdPiece;
  }

  @Override
  public int hashCode() {
    return Objects.hash(candidateIdPiece, electionIdPiece);
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
    CandidateCompositeId other = (CandidateCompositeId) object;
    return Objects.equals(candidateIdPiece, other.candidateIdPiece) && Objects.equals(electionIdPiece, other.electionIdPiece);
  }

  @Override
  public String toString() {
    return "CandidateCompositeId [candidateIdPiece=" + candidateIdPiece + ", electionIdPiece=" + electionIdPiece + "]";
  }
}
