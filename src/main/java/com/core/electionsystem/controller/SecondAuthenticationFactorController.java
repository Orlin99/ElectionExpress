package com.core.electionsystem.controller;

import java.security.InvalidKeyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationActivatorDTO;
import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationRemoverForElectorDTO;
import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationRemoverForSupervisorDTO;
import com.core.electionsystem.service.SecondAuthenticationFactorService;
import com.core.electionsystem.utility.ElectionSystemUtility;

@RestController
@RequestMapping(path = "api/v1/multifactor-authentication")
public class SecondAuthenticationFactorController {

  private final SecondAuthenticationFactorService secondAuthenticationFactorService;

  @Autowired
  public SecondAuthenticationFactorController(SecondAuthenticationFactorService secondAuthenticationFactorService) {
    this.secondAuthenticationFactorService = secondAuthenticationFactorService;
  }

  @GetMapping(path = "/supervisor/setup")
  public ResponseEntity<byte[]> beginSecondAuthenticationFactorSetupForSupervisor() {
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG)
        .body(secondAuthenticationFactorService.generateQrCodeImageForSupervisor());
  }

  @PostMapping(path = "/supervisor/setup")
  public ResponseEntity<String> completeSecondAuthenticationFactorSetupForSupervisor(
      @RequestBody TwoFactorAuthenticationActivatorDTO twoFactorAuthenticationActivatorDTO) throws InvalidKeyException {
    secondAuthenticationFactorService.validateTotpCodeAndEnable2faForSupervisor(twoFactorAuthenticationActivatorDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_ACTIVATED_2FA_RESPONSE);
  }

  @PatchMapping(path = "/supervisor/remove-multifactor-authentication")
  public ResponseEntity<String> removeMultifactorAuthenticationForSupervisor(
      @RequestBody TwoFactorAuthenticationRemoverForSupervisorDTO twoFactorAuthenticationRemoverForSupervisorDTO) {
    secondAuthenticationFactorService.removeMultifactorAuthenticationForSupervisor(twoFactorAuthenticationRemoverForSupervisorDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_2FA_RESPONSE);
  }

  @GetMapping(path = "/elector/setup")
  public ResponseEntity<byte[]> beginSecondAuthenticationFactorSetupForElector() {
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG)
        .body(secondAuthenticationFactorService.generateQrCodeImageForElector());
  }

  @PostMapping(path = "/elector/setup")
  public ResponseEntity<String> completeSecondAuthenticationFactorSetupForElector(
      @RequestBody TwoFactorAuthenticationActivatorDTO twoFactorAuthenticationActivatorDTO) throws InvalidKeyException {
    secondAuthenticationFactorService.validateTotpCodeAndEnable2faForElector(twoFactorAuthenticationActivatorDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_ACTIVATED_2FA_RESPONSE);
  }

  @PatchMapping(path = "/elector/remove-multifactor-authentication")
  public ResponseEntity<String> removeMultifactorAuthenticationForElector(
      @RequestBody TwoFactorAuthenticationRemoverForElectorDTO twoFactorAuthenticationRemoverForElectorDTO) {
    secondAuthenticationFactorService.removeMultifactorAuthenticationForElector(twoFactorAuthenticationRemoverForElectorDTO);
    return ResponseEntity.status(HttpStatus.OK).body(ElectionSystemUtility.SUCCESSFULLY_REMOVED_2FA_RESPONSE);
  }
}
