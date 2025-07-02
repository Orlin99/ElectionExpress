package com.core.electionsystem.supervisor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.electionsystem.dto.LoginUserDTO;
import com.core.electionsystem.dto.PasswordHolderDTO;
import com.core.electionsystem.dto.PhoneNumberHolderDTO;
import com.core.electionsystem.dto.SecretAnswerHolderDTO;
import com.core.electionsystem.supervisor.dto.SupervisorDTO;
import com.core.electionsystem.supervisor.service.SupervisorService;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "api/v1/supervisor")
public class SupervisorController {

  private final SupervisorService supervisorService;

  @Autowired
  public SupervisorController(SupervisorService supervisorService) {
    this.supervisorService = supervisorService;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<String> registerNewSupervisor(@RequestBody SupervisorDTO supervisorDTO) {
    supervisorService.registerNewSupervisor(supervisorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(SupervisorUtility.SUCCESSFULLY_REGISTERED_SUPERVISOR_RESPONSE);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<Map<String, Object>> loginExistingSupervisor(@RequestBody LoginUserDTO loginUserDTO) {
    Map<String, Object> responseMap = supervisorService.loginExistingSupervisor(loginUserDTO);
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @GetMapping(path = "/getSupervisorById/{id}")
  public ResponseEntity<String> getSupervisorById(@PathVariable("id") @NotNull Long id) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(supervisorService.getSupervisorById(id));
  }

  // @formatter:off
  @GetMapping(path = "/getSupervisorByEmail/{supervisorEmail}")
  public ResponseEntity<String> getSupervisorByEmail(@PathVariable("supervisorEmail") @NotNull String supervisorEmail) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(supervisorService.getSupervisorByEmail(supervisorEmail));
  }

  @GetMapping(path = "/getSupervisorByPhoneNumber/{supervisorPhone}")
  public ResponseEntity<String> getSupervisorByPhoneNumber(@PathVariable("supervisorPhone") @NotNull String supervisorPhone) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(supervisorService.getSupervisorByPhoneNumber(supervisorPhone));
  }

  @GetMapping(path = "/listSupervisors")
  public ResponseEntity<List<String>> listSupervisors(
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(supervisorService.listSupervisors(pageNumber, pageSize));
  }
  // @formatter:on

  @PatchMapping(path = "/{id}/updatePassword")
  public ResponseEntity<String> updatePassword(@PathVariable("id") @NotNull Long id, @RequestBody PasswordHolderDTO passwordHolderDTO) {
    supervisorService.updatePassword(id, passwordHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_PASSWORD_RESPONSE);
  }

  @PatchMapping(path = "/{id}/updateSecretAnswer")
  public ResponseEntity<String> updateSecretAnswer(@PathVariable("id") @NotNull Long id, @RequestBody SecretAnswerHolderDTO secretAnswerHolderDTO) {
    supervisorService.updateSecretAnswer(id, secretAnswerHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_SECRET_ANSWER_RESPONSE);
  }

  @PatchMapping(path = "/{id}/updatePhoneNumber")
  public ResponseEntity<String> updatePhoneNumber(@PathVariable("id") @NotNull Long id, @RequestBody PhoneNumberHolderDTO phoneNumberHolderDTO) {
    supervisorService.updatePhoneNumber(id, phoneNumberHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_PHONE_NUMBER_RESPONSE);
  }

  @DeleteMapping(path = "/deleteSupervisorById/{id}")
  public ResponseEntity<String> deleteSupervisorById(@PathVariable("id") @NotNull Long id) {
    supervisorService.deleteSupervisorById(id);
    return ResponseEntity.status(HttpStatus.OK).body(SupervisorUtility.SUCCESSFULLY_REMOVED_USER_BY_ID_RESPONSE + id);
  }

  @DeleteMapping(path = "/deleteSupervisorByEmail/{supervisorEmail}")
  public ResponseEntity<String> deleteSupervisorByEmail(@PathVariable("supervisorEmail") @NotNull String supervisorEmail) {
    supervisorService.deleteSupervisorByEmail(supervisorEmail);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_USER_BY_EMAIL_RESPONSE + supervisorEmail);
  }

  @DeleteMapping(path = "/deleteSupervisorByPhoneNumber/{supervisorPhone}")
  public ResponseEntity<String> deleteSupervisorByPhoneNumber(@PathVariable("supervisorPhone") @NotNull String supervisorPhone) {
    supervisorService.deleteSupervisorByPhoneNumber(supervisorPhone);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_USER_BY_PHONE_NUMBER_RESPONSE + supervisorPhone);
  }
}
