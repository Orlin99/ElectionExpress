package com.core.electionsystem.election.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ElectionEventCreatorDTO {

  private String title;
  private String description;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  public ElectionEventCreatorDTO() {
    // Default Empty Constructor
  }

  public ElectionEventCreatorDTO(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
    this.title = title;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, startTime, endTime);
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
    ElectionEventCreatorDTO other = (ElectionEventCreatorDTO) object;
    return Objects.equals(title, other.title) && Objects.equals(description, other.description) && Objects.equals(startTime, other.startTime)
        && Objects.equals(endTime, other.endTime);
  }

  @Override
  public String toString() {
    return "ElectionEventCreatorDTO [title=" + title + ", description=" + description + ", startTime=" + startTime + ", endTime=" + endTime + "]";
  }
}
