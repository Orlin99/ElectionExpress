package com.core.electionsystem.election.dto;

import java.util.Objects;

public class VoteCreatorDTO {

  private String votedElectorId;
  private String votedElectorDocumentId;
  private Long electionId;
  private Integer candidateId;
  private Integer preferenceId;

  public VoteCreatorDTO() {
    // Default Empty Constructor
  }

  public VoteCreatorDTO(String votedElectorId, String votedElectorDocumentId, Long electionId, Integer candidateId, Integer preferenceId) {
    this.votedElectorId = votedElectorId;
    this.votedElectorDocumentId = votedElectorDocumentId;
    this.electionId = electionId;
    this.candidateId = candidateId;
    this.preferenceId = preferenceId;
  }

  public String getVotedElectorId() {
    return votedElectorId;
  }

  public void setVotedElectorId(String votedElectorId) {
    this.votedElectorId = votedElectorId;
  }

  public String getVotedElectorDocumentId() {
    return votedElectorDocumentId;
  }

  public void setVotedElectorDocumentId(String votedElectorDocumentId) {
    this.votedElectorDocumentId = votedElectorDocumentId;
  }

  public Long getElectionId() {
    return electionId;
  }

  public void setElectionId(Long electionId) {
    this.electionId = electionId;
  }

  public Integer getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(Integer candidateId) {
    this.candidateId = candidateId;
  }

  public Integer getPreferenceId() {
    return preferenceId;
  }

  public void setPreferenceId(Integer preferenceId) {
    this.preferenceId = preferenceId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(votedElectorId, votedElectorDocumentId, electionId, candidateId, preferenceId);
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
    VoteCreatorDTO other = (VoteCreatorDTO) object;
    return Objects.equals(votedElectorId, other.votedElectorId) && Objects.equals(votedElectorDocumentId, other.votedElectorDocumentId)
        && Objects.equals(electionId, other.electionId) && Objects.equals(candidateId, other.candidateId)
        && Objects.equals(preferenceId, other.preferenceId);
  }

  @Override
  public String toString() {
    return "VoteCreatorDTO [votedElectorId=" + votedElectorId + ", votedElectorDocumentId=" + votedElectorDocumentId + ", electionId=" + electionId
        + ", candidateId=" + candidateId + ", preferenceId=" + preferenceId + "]";
  }
}
