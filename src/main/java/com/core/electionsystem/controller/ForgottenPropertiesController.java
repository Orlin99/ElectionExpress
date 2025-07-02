package com.core.electionsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.electionsystem.dto.recovery.PasswordRecovererDTO;
import com.core.electionsystem.dto.recovery.PasswordUpdaterDTO;
import com.core.electionsystem.dto.recovery.SecretAnswerRecovererDTO;
import com.core.electionsystem.dto.recovery.SecretAnswerUpdaterDTO;
import com.core.electionsystem.service.ForgottenPropertiesService;
import com.core.electionsystem.utility.ElectionSystemUtility;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping(path = "api/v1/forgotten")
public class ForgottenPropertiesController {

  private final ForgottenPropertiesService forgottenPropertiesService;

  @Autowired
  public ForgottenPropertiesController(ForgottenPropertiesService forgottenPropertiesService) {
    this.forgottenPropertiesService = forgottenPropertiesService;
  }

  @PostMapping(path = "/secret-answer")
  public ResponseEntity<String> recoverSecretAnswer(@RequestBody SecretAnswerRecovererDTO secretAnswerRecovererDTO) throws MessagingException {
    forgottenPropertiesService.validateInputDataForSecretAnswerRecovery(secretAnswerRecovererDTO);
    final String validatedUserEmail = secretAnswerRecovererDTO.getEmail();
    char[] recoveryToken = forgottenPropertiesService.generateRecoveryToken(validatedUserEmail);
    forgottenPropertiesService.sendSecretAnswerRecoveryToken(validatedUserEmail, recoveryToken);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_SENT_EMAIL_FOR_SECRET_ANSWER_RECOVERY);
  }

  // @formatter:off
  @PatchMapping(path = "/secret-answer/token")
  public ResponseEntity<String> changeForgottenSecretAnswer(@RequestParam String recoveryToken, @RequestBody SecretAnswerUpdaterDTO secretAnswerUpdaterDTO) {
    final char[] userIdFromRecoveryToken = forgottenPropertiesService.validateRecoveryTokenAndGetUserId(recoveryToken);
    final char[] inputUserId = secretAnswerUpdaterDTO.getUserId();
    forgottenPropertiesService.validateUserIdByRecoveryToken(recoveryToken, userIdFromRecoveryToken, inputUserId);
    forgottenPropertiesService.patchForgottenSecretAnswer(secretAnswerUpdaterDTO, recoveryToken, userIdFromRecoveryToken);
    forgottenPropertiesService.invalidateAndDeleteAllUserTokens(recoveryToken);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_SECRET_ANSWER_RESPONSE);
  }
  // @formatter:on

  @PostMapping(path = "/password")
  public ResponseEntity<String> recoverPassword(@RequestBody PasswordRecovererDTO passwordRecovererDTO) throws MessagingException {
    forgottenPropertiesService.validateInputDataForPasswordRecovery(passwordRecovererDTO);
    final String validatedUserEmail = passwordRecovererDTO.getEmail();
    char[] recoveryToken = forgottenPropertiesService.generateRecoveryToken(validatedUserEmail);
    forgottenPropertiesService.sendPasswordRecoveryToken(validatedUserEmail, recoveryToken);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_SENT_EMAIL_FOR_PASSWORD_RECOVERY);
  }

  @PatchMapping(path = "/password/token")
  public ResponseEntity<String> changeForgottenPassword(@RequestParam String recoveryToken, @RequestBody PasswordUpdaterDTO passwordUpdaterDTO) {
    final char[] userIdFromRecoveryToken = forgottenPropertiesService.validateRecoveryTokenAndGetUserId(recoveryToken);
    final char[] inputUserId = passwordUpdaterDTO.getUserId();
    forgottenPropertiesService.validateUserIdByRecoveryToken(recoveryToken, userIdFromRecoveryToken, inputUserId);
    forgottenPropertiesService.validateSecretAnswerForPasswordRecovery(recoveryToken, userIdFromRecoveryToken, passwordUpdaterDTO);
    forgottenPropertiesService.patchForgottenPassword(passwordUpdaterDTO, recoveryToken, userIdFromRecoveryToken);
    forgottenPropertiesService.invalidateAndDeleteAllUserTokens(recoveryToken);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_UPDATED_PASSWORD_RESPONSE);
  }
}
