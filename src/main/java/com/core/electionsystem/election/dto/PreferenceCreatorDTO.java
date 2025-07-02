package com.core.electionsystem.election.dto;

import java.util.Objects;

public class PreferenceCreatorDTO {

  private Integer preferenceId;
  private String preferenceName;
  private Long electionId;
  private Integer candidateId;

  public PreferenceCreatorDTO() {
    // Default Empty Constructor
  }

  public PreferenceCreatorDTO(Integer preferenceId, String preferenceName, Long electionId, Integer candidateId) {
    this.preferenceId = preferenceId;
    this.preferenceName = preferenceName;
    this.electionId = electionId;
    this.candidateId = candidateId;
  }

  public Integer getPreferenceId() {
    return preferenceId;
  }

  public void setPreferenceId(Integer preferenceId) {
    this.preferenceId = preferenceId;
  }

  public String getPreferenceName() {
    return preferenceName;
  }

  public void setPreferenceName(String preferenceName) {
    this.preferenceName = preferenceName;
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

  @Override
  public int hashCode() {
    return Objects.hash(preferenceId, preferenceName, electionId, candidateId);
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
    PreferenceCreatorDTO other = (PreferenceCreatorDTO) object;
    return Objects.equals(preferenceId, other.preferenceId) && Objects.equals(preferenceName, other.preferenceName)
        && Objects.equals(electionId, other.electionId) && Objects.equals(candidateId, other.candidateId);
  }

  @Override
  public String toString() {
    return "PreferenceCreatorDTO [preferenceId=" + preferenceId + ", preferenceName=" + preferenceName + ", electionId=" + electionId
        + ", candidateId=" + candidateId + "]";
  }
}
