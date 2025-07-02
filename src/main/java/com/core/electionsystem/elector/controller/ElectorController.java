package com.core.electionsystem.elector.controller;

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
import com.core.electionsystem.elector.dto.ElectorDTO;
import com.core.electionsystem.elector.dto.ElectorResidenceDTO;
import com.core.electionsystem.elector.model.properties.Sex;
import com.core.electionsystem.elector.service.ElectorService;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "api/v1/elector")
public class ElectorController {

  private final ElectorService electorService;

  @Autowired
  public ElectorController(ElectorService electorService) {
    this.electorService = electorService;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<String> registerNewElector(@RequestBody ElectorDTO electorDTO) {
    electorService.registerNewElector(electorDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(ElectorUtility.SUCCESSFULLY_REGISTERED_USER_RESPONSE);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<Map<String, Object>> loginExistingElector(@RequestBody LoginUserDTO loginUserDTO) {
    Map<String, Object> responseMap = electorService.loginExistingElector(loginUserDTO);
    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
  }

  @GetMapping(path = "/getUserByEGN/{electorId}")
  public ResponseEntity<String> getElectorById(@PathVariable("electorId") @NotNull String electorId) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.getElectorById(electorId));
  }

  @GetMapping(path = "/getUserByDocumentId/{documentId}")
  public ResponseEntity<String> getElectorByDocumentId(@PathVariable("documentId") @NotNull String documentId) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.getElectorByDocumentId(documentId));
  }

  @GetMapping(path = "/getUserByEmail/{email}")
  public ResponseEntity<String> getElectorByEmail(@PathVariable("email") @NotNull String email) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.getElectorByEmail(email));
  }

  @GetMapping(path = "/getUserByPhoneNumber/{phoneNumber}")
  public ResponseEntity<String> getElectorByPhoneNumber(@PathVariable("phoneNumber") @NotNull String phoneNumber) throws JsonProcessingException {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.getElectorByPhoneNumber(phoneNumber));
  }

  // @formatter:off
  @GetMapping(path = "/listUsersByName")
  public ResponseEntity<List<String>> listElectorsByName(
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String middleName,
      @RequestParam(required = false) String surname,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.listElectorsByNames(firstName, middleName, surname, pageNumber, pageSize));
  }

  @GetMapping(path = "/listUsersByResidence")
  public ResponseEntity<List<String>> listElectorsByResidence(
      @RequestParam(required = false) String region,
      @RequestParam(required = false) String municipality,
      @RequestParam(required = false) String locality,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.listElectorsByResidence(region, municipality, locality, pageNumber, pageSize));
  }

  @GetMapping(path = "/listUsersByBirthDate")
  public ResponseEntity<List<String>> listElectorsByBirthDate(
      @RequestParam(required = false) Integer day,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "10") int pageSize) {
    return ResponseEntity.status(HttpStatus.OK).body(electorService.listElectorsByBirthDate(day, month, year, pageNumber, pageSize));
  }
  // @formatter:on

  @GetMapping(path = "/getUsersCountBySex/{sex}")
  public ResponseEntity<Integer> getElectorsCountBySex(@PathVariable("sex") @NotNull String sex) {
    Sex.validateSex(sex);
    return ResponseEntity.status(HttpStatus.OK).body(electorService.getElectorsCountBySex(sex));
  }

  // @formatter:off
  @PatchMapping(path = "/{electorId}/updateUserFullName")
  public ResponseEntity<String> updateUserFullName(
      @PathVariable("electorId") @NotNull String electorId,
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String middleName,
      @RequestParam(required = false) String surname) {
    electorService.updateUserFullName(electorId, firstName, middleName, surname);
    return ResponseEntity.status(HttpStatus.OK).body(ElectorUtility.SUCCESSFULLY_UPDATED_FULL_NAME_RESPONSE);
  }
  // @formatter:on

  @PatchMapping(path = "/{electorId}/updateDocumentId")
  public ResponseEntity<String> updateDocumentId(@PathVariable("electorId") @NotNull String electorId, @RequestParam String documentId) {
    electorService.updateDocumentId(electorId, documentId);
    return ResponseEntity.status(HttpStatus.OK).body(ElectorUtility.SUCCESSFULLY_UPDATED_DOCUMENT_ID_RESPONSE + documentId);
  }

  // @formatter:off
  @PatchMapping(path = "/{electorId}/updateUserFullResidence")
  public ResponseEntity<String> updateUserFullResidence(
      @PathVariable("electorId") @NotNull String electorId,
      @RequestBody ElectorResidenceDTO electorResidenceDTO) {
    electorService.updateUserFullResidence(electorId, electorResidenceDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectorUtility.SUCCESSFULLY_UPDATED_FULL_RESIDENCE_RESPONSE);
  }

  @PatchMapping(path = "/{electorId}/updatePassword")
  public ResponseEntity<String> updatePassword(@PathVariable("electorId") @NotNull String electorId, @RequestBody PasswordHolderDTO passwordHolderDTO) {
    electorService.updatePassword(electorId, passwordHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_PASSWORD_RESPONSE);
  }

  @PatchMapping(path = "/{electorId}/updateSecretAnswer")
  public ResponseEntity<String> updateSecretAnswer(
      @PathVariable("electorId") @NotNull String electorId,
      @RequestBody SecretAnswerHolderDTO secretAnswerHolderDTO) {
    electorService.updateSecretAnswer(electorId, secretAnswerHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_SECRET_ANSWER_RESPONSE);
  }

  @PatchMapping(path = "/{electorId}/updatePhoneNumber")
  public ResponseEntity<String> updatePhoneNumber(
      @PathVariable("electorId") @NotNull String electorId,
      @RequestBody PhoneNumberHolderDTO phoneNumberHolderDTO) {
    electorService.updatePhoneNumber(electorId, phoneNumberHolderDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_PHONE_NUMBER_RESPONSE);
  }
  // @formatter:on

  @DeleteMapping(path = "/deleteUserByEGN/{electorId}")
  public ResponseEntity<String> deleteElectorById(@PathVariable("electorId") @NotNull String electorId) {
    electorService.deleteElectorById(electorId);
    return ResponseEntity.status(HttpStatus.OK).body(ElectorUtility.SUCCESSFULLY_REMOVED_USER_BY_EGN_RESPONSE + electorId);
  }

  @DeleteMapping(path = "/deleteUserByDocumentId/{documentId}")
  public ResponseEntity<String> deleteElectorByDocumentId(@PathVariable("documentId") @NotNull String documentId) {
    electorService.deleteElectorByDocumentId(documentId);
    return ResponseEntity.status(HttpStatus.OK).body(ElectorUtility.SUCCESSFULLY_REMOVED_USER_BY_DOCUMENT_ID_RESPONSE + documentId);
  }

  @DeleteMapping(path = "/deleteUserByEmail/{email}")
  public ResponseEntity<String> deleteElectorByEmail(@PathVariable("email") @NotNull String email) {
    electorService.deleteElectorByEmail(email);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_USER_BY_EMAIL_RESPONSE + email);
  }

  @DeleteMapping(path = "/deleteUserByPhoneNumber/{phoneNumber}")
  public ResponseEntity<String> deleteElectorByPhoneNumber(@PathVariable("phoneNumber") @NotNull String phoneNumber) {
    electorService.deleteElectorByPhoneNumber(phoneNumber);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_USER_BY_PHONE_NUMBER_RESPONSE + phoneNumber);
  }
}
