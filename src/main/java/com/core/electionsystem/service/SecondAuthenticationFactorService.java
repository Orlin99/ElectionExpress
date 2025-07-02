package com.core.electionsystem.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationActivatorDTO;
import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationRemoverForElectorDTO;
import com.core.electionsystem.dto.mfa.TwoFactorAuthenticationRemoverForSupervisorDTO;
import com.core.electionsystem.elector.exception.InvalidEgnException;
import com.core.electionsystem.elector.exception.NonExistentElectorException;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorCredentials;
import com.core.electionsystem.elector.model.properties.ElectorMetadata;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.InvalidValueForTotpCodeException;
import com.core.electionsystem.supervisor.exception.NonExistentSupervisorException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorMetadata;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import jakarta.transaction.Transactional;

@Service
public class SecondAuthenticationFactorService {

  private final SupervisorRepository supervisorRepository;
  private final ElectorRepository electorRepository;
  private final TimeBasedOneTimePasswordGenerator totpGenerator;
  private final BCryptPasswordEncoder encoder;
  private final SecureRandom secureRandom;
  private final Base32 base32;

  @Autowired
  public SecondAuthenticationFactorService(SupervisorRepository supervisorRepository, ElectorRepository electorRepository,
      TimeBasedOneTimePasswordGenerator totpGenerator, BCryptPasswordEncoder encoder, SecureRandom secureRandom, Base32 base32) {
    this.supervisorRepository = supervisorRepository;
    this.electorRepository = electorRepository;
    this.totpGenerator = totpGenerator;
    this.encoder = encoder;
    this.secureRandom = secureRandom;
    this.base32 = base32;
  }

  public byte[] generateQrCodeImageForSupervisor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(currentUserEmail)
        .orElseThrow(() -> new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));

    SupervisorUtility.validateIfEligibleFor2faSetup(supervisor);
    final String secretKey = SecurityUtility.generateBase32SecretKey(SecurityUtility.DEFAULT_BYTES_LENGTH_FOR_2FA_SECRET_KEY, secureRandom, base32);
    supervisor.setSecretKey(secretKey);
    supervisorRepository.save(supervisor);

    final String otpAuthURIScheme = SecurityUtility.generateOTPAuthURIScheme(currentUserEmail, secretKey);
    return SecurityUtility.generateQrCode(otpAuthURIScheme, SecurityUtility.QR_CODE_WIDTH, SecurityUtility.QR_CODE_HEIGHT);
  }

  public void validateTotpCodeAndEnable2faForSupervisor(TwoFactorAuthenticationActivatorDTO twoFactorAuthenticationActivatorDTO)
      throws InvalidKeyException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(currentUserEmail)
        .orElseThrow(() -> new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));

    String passwordHash = supervisor.getPasswordHash();
    final char[] inputPassword = twoFactorAuthenticationActivatorDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, passwordHash, encoder);

    String secretKey = supervisor.getSecretKey();
    Set<String> totpCodes = SecurityUtility.getTOTPCodes(base32, secretKey, totpGenerator);
    final char[] inputTotpCode = twoFactorAuthenticationActivatorDTO.getTotpCode();
    SecurityUtility.validateTotpCode(totpCodes, inputTotpCode);

    byte[] salt = SecurityUtility.generateSalt(secureRandom);
    byte[] initializationVector = SecurityUtility.generateInitializationVector(secureRandom);
    byte[] encryptedSecretKey = SecurityUtility.encryptSecretKey(secretKey.getBytes(), passwordHash.toCharArray(), salt, initializationVector);
    final String encryptedSecretKeyString = new String(encryptedSecretKey, StandardCharsets.UTF_8);
    SecurityUtility.clearArray(encryptedSecretKey);
    supervisor.setSecretKey(encryptedSecretKeyString);
    SupervisorUtility.processSaltAndIvForMetadata(supervisor, salt, initializationVector);
    supervisorRepository.save(supervisor);
  }

  public boolean isSecondAuthenticationFactorRequiredForSupervisor(Supervisor supervisor) {
    SupervisorMetadata supervisorMetadata = supervisor.getSupervisorMetadata();
    final String salt = supervisorMetadata.getSalt();
    final String initializationVector = supervisorMetadata.getInitializationVector();
    final String supervisorSecretKey = supervisor.getSecretKey();
    if (supervisorSecretKey == null) {
      return false;
    } else if (supervisorSecretKey.length() == SecurityUtility.DEFAULT_LENGTH_FOR_2FA_RAW_SECRET_KEY) {
      supervisor.setSecretKey(null);
      supervisorRepository.save(supervisor);
      return false;
    } else if ((supervisorSecretKey.length() == SecurityUtility.LENGTH_FOR_2FA_BASE_64_ENCODED_SECRET_KEY) && (salt != null)
        && (initializationVector != null)) {
      return true;
    } else {
      supervisor.setSecretKey(null);
      supervisorMetadata.setSalt(null);
      supervisorMetadata.setInitializationVector(null);
      supervisorRepository.save(supervisor);
      return false;
    }
  }

  public boolean isSupervisorInputTotpCodeValid(Supervisor supervisor, String inputTotpCode) throws InvalidKeyException {
    SupervisorMetadata supervisorMetadata = supervisor.getSupervisorMetadata();
    byte[] decodedSalt = SecurityUtility.decodeFromBase64(supervisorMetadata.getSalt());
    byte[] decodedIv = SecurityUtility.decodeFromBase64(supervisorMetadata.getInitializationVector());
    char[] passwordHash = supervisor.getPasswordHash().toCharArray();
    byte[] encryptedSecretKey = supervisor.getSecretKey().getBytes();
    byte[] decryptedSecretKey = SecurityUtility.decryptSecretKey(encryptedSecretKey, passwordHash, decodedSalt, decodedIv);

    final String decryptedSecretKeyString = new String(decryptedSecretKey, StandardCharsets.UTF_8);
    Set<String> totpCodes = SecurityUtility.getTOTPCodes(base32, decryptedSecretKeyString, totpGenerator);
    char[] inputTotpCodeChars = inputTotpCode.toCharArray();
    try {
      SecurityUtility.validateTotpCode(totpCodes, inputTotpCodeChars);
    } catch (InvalidValueForTotpCodeException exception) {
      return false;
    } finally {
      SecurityUtility.clearSensitiveData(decodedSalt, decodedIv, passwordHash, encryptedSecretKey, decryptedSecretKey, inputTotpCodeChars);
    }
    return true;
  }

  // @formatter:off
  @Transactional
  public void removeMultifactorAuthenticationForSupervisor(TwoFactorAuthenticationRemoverForSupervisorDTO twoFactorAuthenticationRemoverForSupervisorDTO) {
    final String inputSupervisorEmail = twoFactorAuthenticationRemoverForSupervisorDTO.getSupervisorEmail();
    ElectionSystemUtility.validateEmail(inputSupervisorEmail);
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(inputSupervisorEmail).orElseThrow(
        () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + inputSupervisorEmail));

    final char[] inputPassword = twoFactorAuthenticationRemoverForSupervisorDTO.getPasswordFirstInput();
    final String supervisorPassword = supervisor.getPasswordHash();
    SecurityUtility.validateLoginPassword(inputPassword, supervisorPassword, encoder);
    final boolean passwordsAreEqual = Arrays.equals(inputPassword, twoFactorAuthenticationRemoverForSupervisorDTO.getPasswordSecondInput());
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_PASSWORD_VALUE);
    }

    final String inputSecretAnswer = twoFactorAuthenticationRemoverForSupervisorDTO.getSecretAnswer();
    final String supervisorSecretAnswer = supervisor.getSecretAnswer();
    SecurityUtility.validateSecretAnswer(inputSecretAnswer, supervisorSecretAnswer, encoder);

    supervisor.setSecretKey(null);
    SupervisorMetadata supervisorMetadata = supervisor.getSupervisorMetadata();
    supervisorMetadata.setSalt(null);
    supervisorMetadata.setInitializationVector(null);
    supervisorRepository.save(supervisor);
  }
  // @formatter:on

  public byte[] generateQrCodeImageForElector() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    Elector elector = electorRepository.findElectorByEmail(currentUserEmail)
        .orElseThrow(() -> new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));
    ElectorCredentials electorCredentials = elector.getElectorCredentials();

    ElectorUtility.validateIfEligibleFor2faSetup(elector, electorCredentials);
    final String secretKey = SecurityUtility.generateBase32SecretKey(SecurityUtility.DEFAULT_BYTES_LENGTH_FOR_2FA_SECRET_KEY, secureRandom, base32);
    electorCredentials.setSecretKey(secretKey);
    electorRepository.save(elector);

    final String otpAuthURIScheme = SecurityUtility.generateOTPAuthURIScheme(currentUserEmail, secretKey);
    return SecurityUtility.generateQrCode(otpAuthURIScheme, SecurityUtility.QR_CODE_WIDTH, SecurityUtility.QR_CODE_HEIGHT);
  }

  public void validateTotpCodeAndEnable2faForElector(TwoFactorAuthenticationActivatorDTO twoFactorAuthenticationActivatorDTO)
      throws InvalidKeyException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    Elector elector = electorRepository.findElectorByEmail(currentUserEmail)
        .orElseThrow(() -> new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));
    ElectorCredentials electorCredentials = elector.getElectorCredentials();

    String passwordHash = electorCredentials.getPasswordHash();
    final char[] inputPassword = twoFactorAuthenticationActivatorDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, passwordHash, encoder);

    String secretKey = electorCredentials.getSecretKey();
    Set<String> totpCodes = SecurityUtility.getTOTPCodes(base32, secretKey, totpGenerator);
    final char[] inputTotpCode = twoFactorAuthenticationActivatorDTO.getTotpCode();
    SecurityUtility.validateTotpCode(totpCodes, inputTotpCode);

    byte[] salt = SecurityUtility.generateSalt(secureRandom);
    byte[] initializationVector = SecurityUtility.generateInitializationVector(secureRandom);
    byte[] encryptedSecretKey = SecurityUtility.encryptSecretKey(secretKey.getBytes(), passwordHash.toCharArray(), salt, initializationVector);
    final String encryptedSecretKeyString = new String(encryptedSecretKey, StandardCharsets.UTF_8);
    SecurityUtility.clearArray(encryptedSecretKey);
    electorCredentials.setSecretKey(encryptedSecretKeyString);
    ElectorUtility.processSaltAndIvForMetadata(elector, salt, initializationVector);
    electorRepository.save(elector);
  }

  public boolean isSecondAuthenticationFactorRequiredForElector(Elector elector) {
    ElectorMetadata electorMetadata = elector.getElectorMetadata();
    final String salt = electorMetadata.getSalt();
    final String initializationVector = electorMetadata.getInitializationVector();
    final ElectorCredentials electorCredentials = elector.getElectorCredentials();
    final String electorSecretKey = electorCredentials.getSecretKey();
    if (electorSecretKey == null) {
      return false;
    } else if (electorSecretKey.length() == SecurityUtility.DEFAULT_LENGTH_FOR_2FA_RAW_SECRET_KEY) {
      electorCredentials.setSecretKey(null);
      electorRepository.save(elector);
      return false;
    } else if ((electorSecretKey.length() == SecurityUtility.LENGTH_FOR_2FA_BASE_64_ENCODED_SECRET_KEY) && (salt != null)
        && (initializationVector != null)) {
      return true;
    } else {
      electorCredentials.setSecretKey(null);
      electorMetadata.setSalt(null);
      electorMetadata.setInitializationVector(null);
      electorRepository.save(elector);
      return false;
    }
  }

  public boolean isElectorInputTotpCodeValid(Elector elector, String inputTotpCode) throws InvalidKeyException {
    final ElectorCredentials electorCredentials = elector.getElectorCredentials();
    ElectorMetadata electorMetadata = elector.getElectorMetadata();
    byte[] decodedSalt = SecurityUtility.decodeFromBase64(electorMetadata.getSalt());
    byte[] decodedIv = SecurityUtility.decodeFromBase64(electorMetadata.getInitializationVector());
    char[] passwordHash = electorCredentials.getPasswordHash().toCharArray();
    byte[] encryptedSecretKey = electorCredentials.getSecretKey().getBytes();
    byte[] decryptedSecretKey = SecurityUtility.decryptSecretKey(encryptedSecretKey, passwordHash, decodedSalt, decodedIv);

    final String decryptedSecretKeyString = new String(decryptedSecretKey, StandardCharsets.UTF_8);
    Set<String> totpCodes = SecurityUtility.getTOTPCodes(base32, decryptedSecretKeyString, totpGenerator);
    char[] inputTotpCodeChars = inputTotpCode.toCharArray();
    try {
      SecurityUtility.validateTotpCode(totpCodes, inputTotpCodeChars);
    } catch (InvalidValueForTotpCodeException exception) {
      return false;
    } finally {
      SecurityUtility.clearSensitiveData(decodedSalt, decodedIv, passwordHash, encryptedSecretKey, decryptedSecretKey, inputTotpCodeChars);
    }
    return true;
  }

  @Transactional
  public void removeMultifactorAuthenticationForElector(TwoFactorAuthenticationRemoverForElectorDTO twoFactorAuthenticationRemoverForElectorDTO) {
    final String inputUserEmail = twoFactorAuthenticationRemoverForElectorDTO.getEmail();
    ElectionSystemUtility.validateEmail(inputUserEmail);
    Elector elector = electorRepository.findElectorByEmail(inputUserEmail)
        .orElseThrow(() -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + inputUserEmail));

    final String inputElectorId = String.valueOf(twoFactorAuthenticationRemoverForElectorDTO.getElectorId());
    ElectorUtility.validateEGN(inputElectorId);
    final String electorId = elector.getElectorId();
    if (!inputElectorId.equals(electorId)) {
      throw new InvalidEgnException(ElectorUtility.MESSAGE_FOR_INVALID_EGN_EXCEPTION);
    }

    ElectorCredentials electorCredentials = elector.getElectorCredentials();
    final char[] inputPassword = twoFactorAuthenticationRemoverForElectorDTO.getPasswordFirstInput();
    final String electorPassword = electorCredentials.getPasswordHash();
    SecurityUtility.validateLoginPassword(inputPassword, electorPassword, encoder);
    final boolean passwordsAreEqual = Arrays.equals(inputPassword, twoFactorAuthenticationRemoverForElectorDTO.getPasswordSecondInput());
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_PASSWORD_VALUE);
    }

    final String inputSecretAnswer = twoFactorAuthenticationRemoverForElectorDTO.getSecretAnswer();
    final String electorSecretAnswer = electorCredentials.getSecretAnswer();
    SecurityUtility.validateSecretAnswer(inputSecretAnswer, electorSecretAnswer, encoder);

    electorCredentials.setSecretKey(null);
    ElectorMetadata electorMetadata = elector.getElectorMetadata();
    electorMetadata.setSalt(null);
    electorMetadata.setInitializationVector(null);
    electorRepository.save(elector);
  }
}
