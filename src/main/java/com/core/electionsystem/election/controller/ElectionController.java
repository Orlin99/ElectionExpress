package com.core.electionsystem.election.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.electionsystem.election.dto.CandidateCreatorDTO;
import com.core.electionsystem.election.dto.ElectionEventCreatorDTO;
import com.core.electionsystem.election.dto.PreferenceCreatorDTO;
import com.core.electionsystem.election.dto.VoteCreatorDTO;
import com.core.electionsystem.election.service.ElectionService;
import com.core.electionsystem.election.utility.ElectionUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "api/v1/election")
public class ElectionController {

  private final ElectionService electionService;

  @Autowired
  public ElectionController(ElectionService electionService) {
    this.electionService = electionService;
  }

  @PostMapping(path = "/create-event")
  public ResponseEntity<String> createElectionEvent(@RequestBody ElectionEventCreatorDTO electionEventCreatorDTO) {
    electionService.createElectionEvent(electionEventCreatorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(ElectionUtility.SUCCESSFULLY_CREATED_ELECTION_EVENT_RESPONSE);
  }

  @PostMapping(path = "/add-candidate")
  public ResponseEntity<String> addCandidate(@RequestBody CandidateCreatorDTO candidateCreatorDTO) {
    electionService.addCandidate(candidateCreatorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(ElectionUtility.SUCCESSFULLY_ADDED_CANDIDATE_RESPONSE);
  }

  @PostMapping(path = "/add-preference")
  public ResponseEntity<String> addPreference(@RequestBody PreferenceCreatorDTO preferenceCreatorDTO) {
    electionService.addPreference(preferenceCreatorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(ElectionUtility.SUCCESSFULLY_ADDED_PREFERENCE_RESPONSE);
  }

  @PostMapping(path = "/vote")
  public ResponseEntity<String> vote(@RequestBody VoteCreatorDTO voteCreatorDTO) {
    electionService.vote(voteCreatorDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionUtility.SUCCESSFULLY_CAST_VOTE_RESPONSE);
  }

  @GetMapping(path = "/get-event-by-id/{id}")
  public ResponseEntity<String> getElectionEventById(@PathVariable("id") @NotNull Long id) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getElectionEventById(id));
  }

  @GetMapping(path = "/get-event-by-title/{title}")
  public ResponseEntity<String> getElectionEventByTitle(@PathVariable("title") @NotNull String title) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getElectionEventByTitle(title));
  }

  // @formatter:off
  @GetMapping(path = "/get-candidate/{candidateId}/from-election/{id}")
  public ResponseEntity<String> getCandidate(@PathVariable("candidateId") @NotNull Integer candidateId, @PathVariable("id") @NotNull Long id) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getCandidate(candidateId, id));
  }

  @GetMapping(path = "/get-preference/{preferenceId}/from-candidate/{candidateId}/from-election/{id}")
  public ResponseEntity<String> getPreference(
      @PathVariable("preferenceId") @NotNull Integer preferenceId,
      @PathVariable("candidateId") @NotNull Integer candidateId,
      @PathVariable("id") @NotNull Long id) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getPreference(preferenceId, candidateId, id));
  }
  // @formatter:on

  @GetMapping(path = "/get-voter-turnout/for-election/{id}")
  public ResponseEntity<String> getVoterTurnoutForSpecificElection(@PathVariable("id") @NotNull Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getVoterTurnoutForSpecificElection(id));
  }

  @GetMapping(path = "/get-percentage-results/for-election/{id}")
  public ResponseEntity<String> getPercentageResultsForSpecificElection(@PathVariable("id") @NotNull Long id) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.getPercentageResultsForSpecificElection(id));
  }

  // @formatter:off
  @GetMapping(path = "/list-all-elections/with-status/{status}")
  public ResponseEntity<List<String>> listAllElectionsWithSpecificStatus(
      @PathVariable("status") @NotNull String status,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.listAllElectionsWithSpecificStatus(status, pageNumber, pageSize));
  }

  @GetMapping(path = "/list-all-candidates/from-election/{id}")
  public ResponseEntity<List<String>> listAllCandidatesFromSpecificElection(
      @PathVariable("id") @NotNull Long id,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.listAllCandidatesFromSpecificElection(id, pageNumber, pageSize));
  }

  @GetMapping(path = "/list-all-preferences/from-candidate/{candidateId}/from-election/{id}")
  public ResponseEntity<List<String>> listAllPreferencesFromSpecificCandidateFromSpecificElection(
      @PathVariable("candidateId") @NotNull Integer candidateId,
      @PathVariable("id") @NotNull Long id,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electionService.listAllPreferencesFromSpecificCandidateFromSpecificElection(candidateId, id, pageNumber, pageSize));
  }
  // @formatter:on

  @DeleteMapping(path = "/delete-event/{id}")
  public ResponseEntity<String> deleteElectionEvent(@PathVariable("id") @NotNull Long id) {
    electionService.deleteElectionEvent(id);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionUtility.SUCCESSFULLY_DELETED_ELECTION_EVENT_RESPONSE);
  }

  @DeleteMapping(path = "/delete-candidate/{candidateId}/from-election/{id}")
  public ResponseEntity<String> deleteCandidate(@PathVariable("candidateId") @NotNull Integer candidateId, @PathVariable("id") @NotNull Long id) {
    electionService.deleteCandidate(candidateId, id);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionUtility.SUCCESSFULLY_DELETED_CANDIDATE_RESPONSE);
  }

  // @formatter:off
  @DeleteMapping(path = "/delete-preference/{preferenceId}/from-candidate/{candidateId}/from-election/{id}")
  public ResponseEntity<String> deletePreference(
      @PathVariable("preferenceId") @NotNull Integer preferenceId,
      @PathVariable("candidateId") @NotNull Integer candidateId,
      @PathVariable("id") @NotNull Long id) {
    electionService.deletePreference(preferenceId, candidateId, id);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionUtility.SUCCESSFULLY_DELETED_PREFERENCE_RESPONSE);
  }
  // @formatter:on
}
