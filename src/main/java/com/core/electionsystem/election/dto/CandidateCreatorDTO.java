package com.core.electionsystem.election.dto;

import java.util.Objects;

public class CandidateCreatorDTO {

  private Integer candidateId;
  private String candidateName;
  private Long electionId;

  public CandidateCreatorDTO() {
    // Default Empty Constructor
  }

  public CandidateCreatorDTO(Integer candidateId, String candidateName, Long electionId) {
    this.candidateId = candidateId;
    this.candidateName = candidateName;
    this.electionId = electionId;
  }

  public Integer getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(Integer candidateId) {
    this.candidateId = candidateId;
  }

  public String getCandidateName() {
    return candidateName;
  }

  public void setCandidateName(String candidateName) {
    this.candidateName = candidateName;
  }

  public Long getElectionId() {
    return electionId;
  }

  public void setElectionId(Long electionId) {
    this.electionId = electionId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(candidateId, candidateName, electionId);
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
    CandidateCreatorDTO other = (CandidateCreatorDTO) object;
    return Objects.equals(candidateId, other.candidateId) && Objects.equals(candidateName, other.candidateName)
        && Objects.equals(electionId, other.electionId);
  }

  @Override
  public String toString() {
    return "CandidateCreatorDTO [candidateId=" + candidateId + ", candidateName=" + candidateName + ", electionId=" + electionId + "]";
  }
}
