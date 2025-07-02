package com.core.electionsystem.election.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.core.electionsystem.election.model.Election;
import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.election.repository.ElectionRepository;

import jakarta.transaction.Transactional;

@Component
public class ElectionStatusScheduler {

  private static final int FIXED_INTERVAL_FOR_THE_SCHEDULED_TASKS = 1000 * 60;

  private final ElectionRepository electionRepository;

  @Autowired
  public ElectionStatusScheduler(ElectionRepository electionRepository) {
    this.electionRepository = electionRepository;
  }

  @Transactional
  @Scheduled(fixedDelay = FIXED_INTERVAL_FOR_THE_SCHEDULED_TASKS)
  public void startElections() {
    final List<Election> inactiveElections = electionRepository.findAllElectionsWithTheSpecifiedStatus(Status.INACTIVE);
    if (inactiveElections.isEmpty()) {
      return;
    }
    final LocalDateTime currentTime = LocalDateTime.now();
    for (Election election : inactiveElections) {
      LocalDateTime electionStartTime = election.getStartTime();
      if (!electionStartTime.isAfter(currentTime)) {
        election.setStatus(Status.ACTIVE);
        electionRepository.save(election);
      }
    }
  }

  @Transactional
  @Scheduled(fixedDelay = FIXED_INTERVAL_FOR_THE_SCHEDULED_TASKS)
  public void endElections() {
    final List<Election> activeElections = electionRepository.findAllElectionsWithTheSpecifiedStatus(Status.ACTIVE);
    if (activeElections.isEmpty()) {
      return;
    }
    final LocalDateTime currentTime = LocalDateTime.now();
    for (Election election : activeElections) {
      LocalDateTime electionEndTime = election.getEndTime();
      if (!electionEndTime.isAfter(currentTime)) {
        election.setStatus(Status.COMPLETED);
        electionRepository.save(election);
      }
    }
  }
}
