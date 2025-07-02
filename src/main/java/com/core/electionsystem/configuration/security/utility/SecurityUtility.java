package com.core.electionsystem.configuration.security.utility;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.core.electionsystem.dto.LoginUserDTO;
import com.core.electionsystem.elector.dto.ElectorDTO;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.model.properties.ElectorCredentials;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.IncorrectSecretAnswerException;
import com.core.electionsystem.exception.InvalidPasswordException;
import com.core.electionsystem.exception.InvalidValueForTotpCodeException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.exception.OtpAuthURISchemeGenerationException;
import com.core.electionsystem.exception.QrCodeGenerationFailureException;
import com.core.electionsystem.exception.SecretKeyDecryptionException;
import com.core.electionsystem.exception.SecretKeyEncryptionException;
import com.core.electionsystem.exception.TotpCodeVerificationSystemFailureException;
import com.core.electionsystem.service.CustomizedUserDetailsService;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.service.SecondAuthenticationFactorService;
import com.core.electionsystem.supervisor.dto.SupervisorDTO;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import jakarta.servlet.http.HttpServletRequest;

public final class SecurityUtility {

  private SecurityUtility() {
    // Default Empty Constructor
  }

  public static final String REGISTER_SUPERVISOR_ENDPOINT = "api/v1/supervisor/register";
  public static final String LOGIN_SUPERVISOR_ENDPOINT = "api/v1/supervisor/login";
  public static final String REMOVE_2FA_FOR_SUPERVISOR_ENDPOINT = "api/v1/multifactor-authentication/supervisor/remove-multifactor-authentication";
  public static final String REGISTER_ELECTOR_ENDPOINT = "api/v1/elector/register";
  public static final String LOGIN_ELECTOR_ENDPOINT = "api/v1/elector/login";
  public static final String REMOVE_2FA_FOR_ELECTOR_ENDPOINT = "api/v1/multifactor-authentication/elector/remove-multifactor-authentication";
  public static final String LOGOUT_USER_ENDPOINT = "/api/v1/logout";
  public static final String ALL_ENDPOINTS_OF_FORGOTTEN_PROPERTIES = "api/v1/forgotten/**";

  public static final int BCRYPT_PASSWORD_HASHING_ROUNDS = 12;
  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  public static final String AUTHENTICATION_HEADER_PREFIX = "Bearer ";
  public static final int AUTHENTICATION_HEADER_PREFIX_LENGTH = 7;

  public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
  public static final String MESSAGE_FOR_INVALID_SECURITY_ALGORITHM_EXCEPTION = "Unable To Create Key Generator For Algorithm 'HmacSHA256'. Algorithm Is Not Supported";
  public static final String JSON_WEB_TOKEN_ROLE_ATTRIBUTE = "role";
  public static final String JSON_WEB_TOKEN_2FA_VERIFIED_ATTRIBUTE = "2fa_verified";
  public static final String JSON_WEB_TOKEN_PURPOSE_ATTRIBUTE = "purpose";
  public static final String JSON_WEB_TOKEN_RECOVERY_PURPOSE = "recovery_of_secret_property";
  public static final int JSON_WEB_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30;
  public static final int RECOVERY_JSON_WEB_TOKEN_EXPIRATION_TIME = 1000 * 60 * 15;
  public static final String HEADER_TYPE_JSON_WEB_TOKEN = "JWT";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
  public static final String CHARACTER_ENCODING_UTF_8 = "UTF-8";
  public static final String RESPONSE_DATA_TOKEN_KEY_ATTRIBUTE = "token";

  public static final String RESPONSE_HEADER_VALUE = "Basic realm=\"Test realm\"";
  public static final String ERROR_MESSAGE_FOR_UNAUTHENTICATED_USER = "Access Denied Due To Failed Authentication. Please Log In With Valid Credentials To Proceed";
  public static final String ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE = "Access Denied. You Do Not Have Permission To Access This Resource";
  public static final String SHOULD_NOT_FILTER_REQUESTS_WITH_THIS_PREFIX = "/api/v1/multifactor-authentication";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION = "The Provided User Role Does Not Exist";
  public static final String MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION = "There Was A Failure With The Provided JSON Web Token: It Does Not Exist";

  public static final int DEFAULT_BYTES_LENGTH_FOR_2FA_SECRET_KEY = 20;
  public static final int DEFAULT_LENGTH_FOR_2FA_RAW_SECRET_KEY = 32;
  public static final int LENGTH_FOR_2FA_BASE_64_ENCODED_SECRET_KEY = 64;
  public static final int QR_CODE_WIDTH = 300;
  public static final int QR_CODE_HEIGHT = 300;
  public static final String MESSAGE_FOR_ALREADY_ONBOARDED_2FA_EXCEPTION = "Access Denied Due To Already Onboarded Second Authentication Factor";
  public static final String TIME_BASED_ONE_TIME_PASSWORD_HEADER_NAME = "X-2FA-OTP";
  public static final String MESSAGE_FOR_INVALID_OR_WRONG_2FA_TOTP_CODE = "Code Verification Failed: The Code You Provided Was Incorrect. Please Provide The Valid 6-Digit Code From Your Authenticator App";
  public static final String MESSAGE_FOR_TOTP_CODE_VERIFICATION_SYSTEM_FAILURE = "There Was A System Failure With The TOTP Code Verification";

  public static final String GMAIL_AS_DEFAULT_EMAIL_PROVIDER = "gmail";
  public static final String ABV_AS_DEFAULT_EMAIL_PROVIDER = "abv";
  public static final String MAIL_SMTP_AUTHENTICATION = "mail.smtp.auth";
  public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
  public static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";

  private static final char STORED_VALUE_IN_ALL_ELEMENTS_OF_CHAR_ARRAY = '\0';
  private static final int STORED_VALUE_IN_ALL_ELEMENTS_OF_BYTE_ARRAY = 0;
  private static final String APPLICATION_NAME_ISSUER = "Election Express Application";
  private static final String ISSUER_EMAIL_SEPARATOR = ":";
  private static final String CHARACTER_SEQUENCE_TARGET_EMPTY_SPACE = "+";
  private static final String CHARACTER_SEQUENCE_REPLACEMENT_EMPTY_SPACE = "%20";
  private static final String OTP_AUTH_URI_SCHEME_PREFIX = "otpauth://totp/";
  private static final String OTP_AUTH_URI_SCHEME_SECRET_ATTRIBUTE = "?secret=";
  private static final String OTP_AUTH_URI_SCHEME_ISSUER_ATTRIBUTE = "&issuer=";
  private static final String MESSAGE_FOR_OTP_AUTH_URI_SCHEME_GENERATION_EXCEPTION = "An Error Occurred While Encoding URI Components For The OTP Auth URI Scheme";
  private static final String QR_CODE_IMAGE_FORMAT = "PNG";
  private static final String MESSAGE_FOR_QR_CODE_GENERATION_FAILURE_EXCEPTION = "An Error Occurred While Generating QR-Code. Please Try Again";

  private static final int OFFSET = 0;
  private static final int EMPTY_ARRAY_LENGTH = 0;
  private static final String TOTP_CODE_NULL_VALUE_ERROR_MESSAGE = "The TOTP Code Cannot Be Null Or Empty";
  private static final int TOTP_CODE_LENGTH = 6;
  private static final String TOTP_CODE_INVALID_LENGTH_ERROR_MESSAGE = "The TOTP Code Must Be Exactly '6' Characters Long";
  private static final String TOTP_CODE_REGEX_EXPRESSION = "\\d{6}";
  private static final String TOTP_CODE_NON_MATCHING_REGEX_EXPRESSION_ERROR_MESSAGE = "The TOTP Code Must Contain Only Digits";
  private static final String INVALID_VALUE_FOR_TOTP_CODE_ERROR_MESSAGE = "The Provided TOTP Code Does Not Match With The Code From Your Authenticator App";
  private static final int DEFAULT_SALT_LENGTH = 16;
  private static final int DEFAULT_INITIALIZATION_VECTOR_LENGTH = 12;
  private static final String PASSWORD_BASED_KEY_DERIVATION_FUNCTION_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";
  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 256;
  private static final String AES = "AES";
  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 128;
  private static final String SECRET_KEY_ENCRYPTION_ERROR_MESSAGE = "System Error Has Occurred: Secret Key Encryption Has Failed";
  private static final String SECRET_KEY_DECRYPTION_ERROR_MESSAGE = "System Error Has Occurred: Secret Key Decryption Has Failed";

  public static void validateAndHashSupervisorPassword(SupervisorDTO supervisorDTO, BCryptPasswordEncoder encoder) {
    char[] inputPassword = supervisorDTO.getPasswordHash().toCharArray();
    validatePassword(inputPassword);
    String passwordHash = hashPassword(inputPassword, encoder);
    clearArray(inputPassword);
    supervisorDTO.setPasswordHash(passwordHash);
  }

  public static void validateAndHashElectorPassword(ElectorDTO electorDTO, BCryptPasswordEncoder encoder) {
    char[] inputPassword = electorDTO.getElectorCredentials().getPasswordHash().toCharArray();
    validatePassword(inputPassword);
    String passwordHash = hashPassword(inputPassword, encoder);
    clearArray(inputPassword);
    electorDTO.getElectorCredentials().setPasswordHash(passwordHash);
  }

  public static void validatePassword(char[] inputPassword) {
    String password = String.valueOf(inputPassword);
    Pattern pattern = Pattern.compile(ElectionSystemUtility.REGEX_FOR_PASSWORD);
    Matcher matcher = pattern.matcher(password);
    if (!matcher.matches()) {
      throw new InvalidPasswordException(ElectionSystemUtility.INVALID_PASSWORD_MESSAGE);
    }
  }

  public static String hashPassword(char[] inputPassword, BCryptPasswordEncoder encoder) {
    String password = String.valueOf(inputPassword);
    return encoder.encode(password);
  }

  public static void hashSecretAnswerOfSupervisorOnRegistration(SupervisorDTO supervisorDTO, BCryptPasswordEncoder encoder) {
    String hashedSecretAnswer = hashSecretAnswer(supervisorDTO.getSecretAnswer(), encoder);
    supervisorDTO.setSecretAnswer(hashedSecretAnswer);
  }

  public static void hashSecretAnswerOfElectorOnRegistration(ElectorDTO electorDTO, BCryptPasswordEncoder encoder) {
    ElectorCredentials credentials = electorDTO.getElectorCredentials();
    String hashedSecretAnswer = hashSecretAnswer(credentials.getSecretAnswer(), encoder);
    credentials.setSecretAnswer(hashedSecretAnswer);
  }

  public static String hashSecretAnswer(String inputSecretAnswer, BCryptPasswordEncoder encoder) {
    return encoder.encode(inputSecretAnswer);
  }

  public static void validateLoginPassword(char[] inputPassword, String passwordHash, BCryptPasswordEncoder encoder) {
    String inputPasswordString = String.valueOf(inputPassword);
    boolean passwordsAreEqual = encoder.matches(inputPasswordString, passwordHash);
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.INCORRECT_PASSWORD_MESSAGE);
    }
  }

  public static void validateSecretAnswer(String inputSecretAnswer, String secretAnswer, BCryptPasswordEncoder encoder) {
    boolean secretAnswersAreEqual = encoder.matches(inputSecretAnswer, secretAnswer);
    if (!secretAnswersAreEqual) {
      throw new IncorrectSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_SECRET_ANSWER);
    }
  }

  public static UserDetails getUserDetailsByRole(String userRole, String userEmail, CustomizedUserDetailsService customizedUserDetailsService) {
    if (Role.SUPERVISOR.getValue().equals(userRole) || Role.ELECTOR.getValue().equals(userRole)) {
      return customizedUserDetailsService.loadUserByUsername(userEmail);
    }
    throw new NonExistentUserRoleException(MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
  }

  public static void setUpAuthentication(UserDetails userDetails, HttpServletRequest request) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  // @formatter:off
  public static Authentication authenticateUser(AuthenticationManager authenticationManager, LoginUserDTO loginUserDTO) {
    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), String.valueOf(loginUserDTO.getPassword())));
  }
  // @formatter:on

  public static void clearArray(char[] array) {
    if (array != null) {
      Arrays.fill(array, STORED_VALUE_IN_ALL_ELEMENTS_OF_CHAR_ARRAY);
    }
  }

  public static void clearArray(byte[] array) {
    if (array != null) {
      Arrays.fill(array, (byte) STORED_VALUE_IN_ALL_ELEMENTS_OF_BYTE_ARRAY);
    }
  }

  public static String generateBase32SecretKey(int bytesLength, SecureRandom secureRandom, Base32 base32) {
    byte[] randomBytes = new byte[bytesLength];
    secureRandom.nextBytes(randomBytes);
    return base32.encodeToString(randomBytes);
  }

  public static String generateOTPAuthURIScheme(String currentUserEmail, String secretKey) {
    try {
      // @formatter:off
      String label = URLEncoder.encode(APPLICATION_NAME_ISSUER + ISSUER_EMAIL_SEPARATOR + currentUserEmail, UTF_8.name())
          .replace(CHARACTER_SEQUENCE_TARGET_EMPTY_SPACE, CHARACTER_SEQUENCE_REPLACEMENT_EMPTY_SPACE);
      String secret = URLEncoder.encode(secretKey, UTF_8.name())
          .replace(CHARACTER_SEQUENCE_TARGET_EMPTY_SPACE, CHARACTER_SEQUENCE_REPLACEMENT_EMPTY_SPACE);
      String issuer = URLEncoder.encode(APPLICATION_NAME_ISSUER, UTF_8.name())
          .replace(CHARACTER_SEQUENCE_TARGET_EMPTY_SPACE, CHARACTER_SEQUENCE_REPLACEMENT_EMPTY_SPACE);
      // @formatter:on
      return OTP_AUTH_URI_SCHEME_PREFIX + label + OTP_AUTH_URI_SCHEME_SECRET_ATTRIBUTE + secret + OTP_AUTH_URI_SCHEME_ISSUER_ATTRIBUTE + issuer;
    } catch (UnsupportedEncodingException exception) {
      throw new OtpAuthURISchemeGenerationException(MESSAGE_FOR_OTP_AUTH_URI_SCHEME_GENERATION_EXCEPTION, exception);
    }
  }

  public static byte[] generateQrCode(String otpAuthURIScheme, int width, int height) {
    Map<EncodeHintType, ErrorCorrectionLevel> qrCodeHints = new EnumMap<>(EncodeHintType.class);
    qrCodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    try {
      BitMatrix qrCodeMatrix = new MultiFormatWriter().encode(otpAuthURIScheme, BarcodeFormat.QR_CODE, width, height, qrCodeHints);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(qrCodeMatrix, QR_CODE_IMAGE_FORMAT, outputStream);
      return outputStream.toByteArray();
    } catch (WriterException | IOException exception) {
      throw new QrCodeGenerationFailureException(MESSAGE_FOR_QR_CODE_GENERATION_FAILURE_EXCEPTION, exception);
    }
  }

  // @formatter:off
  public static Set<String> getTOTPCodes(Base32 base32, String secretKey, TimeBasedOneTimePasswordGenerator totpGenerator) throws InvalidKeyException {
    byte[] decodedSecretKey = base32.decode(secretKey);
    Key key = new SecretKeySpec(decodedSecretKey, OFFSET, decodedSecretKey.length, HMAC_SHA256_ALGORITHM);
    clearArray(decodedSecretKey);

    Instant presentStep = Instant.now();
    Instant previousStep = presentStep.minus(totpGenerator.getTimeStep());
    Instant nextStep = presentStep.plus(totpGenerator.getTimeStep());

    String previousCode = totpGenerator.generateOneTimePasswordString(key, previousStep);
    String presentCode = totpGenerator.generateOneTimePasswordString(key, presentStep);
    String nextCode = totpGenerator.generateOneTimePasswordString(key, nextStep);
    return Set.of(previousCode, presentCode, nextCode);
  }
  // @formatter:on

  public static void validateTotpCode(Set<String> totpCodes, char[] inputTotpCode) {
    if ((inputTotpCode == null) || (inputTotpCode.length == EMPTY_ARRAY_LENGTH)) {
      throw new InvalidValueForTotpCodeException(TOTP_CODE_NULL_VALUE_ERROR_MESSAGE);
    }
    String inputTotpCodeString = String.valueOf(inputTotpCode);
    clearArray(inputTotpCode);
    if (inputTotpCodeString.length() != TOTP_CODE_LENGTH) {
      throw new InvalidValueForTotpCodeException(TOTP_CODE_INVALID_LENGTH_ERROR_MESSAGE);
    } else if (!inputTotpCodeString.matches(TOTP_CODE_REGEX_EXPRESSION)) {
      throw new InvalidValueForTotpCodeException(TOTP_CODE_NON_MATCHING_REGEX_EXPRESSION_ERROR_MESSAGE);
    } else if (!totpCodes.contains(inputTotpCodeString)) {
      throw new InvalidValueForTotpCodeException(INVALID_VALUE_FOR_TOTP_CODE_ERROR_MESSAGE);
    }
  }

  public static byte[] generateSalt(SecureRandom secureRandom) {
    byte[] salt = new byte[DEFAULT_SALT_LENGTH];
    secureRandom.nextBytes(salt);
    return salt;
  }

  public static byte[] generateInitializationVector(SecureRandom secureRandom) {
    byte[] initializationVector = new byte[DEFAULT_INITIALIZATION_VECTOR_LENGTH];
    secureRandom.nextBytes(initializationVector);
    return initializationVector;
  }

  public static String encodeToBase64(byte[] inputBytes) {
    final Encoder base64Encoder = Base64.getEncoder().withoutPadding();
    return base64Encoder.encodeToString(inputBytes);
  }

  public static byte[] decodeFromBase64(String encodedText) {
    final Decoder base64Decoder = Base64.getDecoder();
    return base64Decoder.decode(encodedText);
  }

  public static void clearSensitiveData(byte[] decodedSalt, byte[] decodedIv, char[] passwordHash, byte[] encryptedSecretKey,
      byte[] decryptedSecretKey, char[] inputTotpCodeChars) {
    clearArray(decodedSalt);
    clearArray(decodedIv);
    clearArray(passwordHash);
    clearArray(encryptedSecretKey);
    clearArray(decryptedSecretKey);
    clearArray(inputTotpCodeChars);
  }

  public static boolean isAuthenticated(Authentication authentication) {
    return (authentication != null) && authentication.isAuthenticated();
  }

  public static Role getRoleOfCurrentUser(Authentication authentication, JwtService jwtService) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    return jwtService.getRoleFromAuthorities(authorities).orElseThrow(() -> new AccessDeniedException(ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));
  }

  public static boolean isVerified2faClaim(HttpServletRequest request, JwtService jwtService) {
    final String authorizationHeader = request.getHeader(SecurityUtility.AUTHORIZATION_HEADER_NAME);
    final String jsonWebToken = authorizationHeader.substring(SecurityUtility.AUTHENTICATION_HEADER_PREFIX_LENGTH);
    return jwtService.extract2faVerifiedClaim(jsonWebToken);
  }

  // @formatter:off
  public static boolean isSupervisorInputTotpCodeAuthentic(String inputTotpCode, SecondAuthenticationFactorService secondAuthenticationFactorService, Supervisor supervisor) {
    try {
      return (inputTotpCode != null) && !inputTotpCode.isBlank() && secondAuthenticationFactorService.isSupervisorInputTotpCodeValid(supervisor, inputTotpCode);
    } catch (InvalidKeyException exception) {
      throw new TotpCodeVerificationSystemFailureException(SecurityUtility.MESSAGE_FOR_TOTP_CODE_VERIFICATION_SYSTEM_FAILURE, exception);
    }
  }

  public static boolean isElectorInputTotpCodeAuthentic(String inputTotpCode, SecondAuthenticationFactorService secondAuthenticationFactorService, Elector elector) {
    try {
      return (inputTotpCode != null) && !inputTotpCode.isBlank() && secondAuthenticationFactorService.isElectorInputTotpCodeValid(elector, inputTotpCode);
    } catch (InvalidKeyException exception) {
      throw new TotpCodeVerificationSystemFailureException(SecurityUtility.MESSAGE_FOR_TOTP_CODE_VERIFICATION_SYSTEM_FAILURE, exception);
    }
  }
  // @formatter:on

  public static String generateBuildAndStoreSupervisorToken(JwtService jwtService, String currentUserEmail, Role role, Supervisor supervisor,
      SupervisorBearerTokenRepository supervisorBearerTokenRepository) {
    final String newSupervisorTokenValue = jwtService.generate2faToken(currentUserEmail, role);
    final SupervisorBearerToken newSupervisorToken = jwtService.buildSupervisorToken(supervisor, newSupervisorTokenValue);
    supervisorBearerTokenRepository.save(newSupervisorToken);
    return newSupervisorTokenValue;
  }

  public static String generateBuildAndStoreElectorToken(JwtService jwtService, String currentUserEmail, Role role, Elector elector,
      ElectorBearerTokenRepository electorBearerTokenRepository) {
    final String newElectorTokenValue = jwtService.generate2faToken(currentUserEmail, role);
    final ElectorBearerToken newElectorToken = jwtService.buildElectorToken(elector, newElectorTokenValue);
    electorBearerTokenRepository.save(newElectorToken);
    return newElectorTokenValue;
  }

  public static byte[] encryptSecretKey(byte[] secretKey, char[] password, byte[] salt, byte[] initializationVector) {
    try {
      SecretKey keyFromPassword = getKeyFromPassword(password, salt);
      clearArray(password);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      GCMParameterSpec gcmParameterSpec = createGCMParameterSpec(initializationVector);
      cipher.init(Cipher.ENCRYPT_MODE, keyFromPassword, gcmParameterSpec);
      byte[] cipherBytes = cipher.doFinal(secretKey);
      clearArray(secretKey);
      return Base64.getEncoder().encode(cipherBytes);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
        | IllegalBlockSizeException | BadPaddingException exception) {
      throw new SecretKeyEncryptionException(SECRET_KEY_ENCRYPTION_ERROR_MESSAGE, exception);
    }
  }

  public static byte[] decryptSecretKey(byte[] cipherBytes, char[] password, byte[] salt, byte[] initializationVector) {
    try {
      SecretKey keyFromPassword = getKeyFromPassword(password, salt);
      clearArray(password);
      clearArray(salt);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      GCMParameterSpec gcmParameterSpec = createGCMParameterSpec(initializationVector);
      clearArray(initializationVector);
      cipher.init(Cipher.DECRYPT_MODE, keyFromPassword, gcmParameterSpec);
      byte[] decodedCipherText = Base64.getDecoder().decode(cipherBytes);
      clearArray(cipherBytes);
      return cipher.doFinal(decodedCipherText);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
        | IllegalBlockSizeException | BadPaddingException exception) {
      throw new SecretKeyDecryptionException(SECRET_KEY_DECRYPTION_ERROR_MESSAGE, exception);
    }
  }

  private static SecretKey getKeyFromPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_BASED_KEY_DERIVATION_FUNCTION_2_WITH_HMAC_SHA_256);
    KeySpec keySpec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    return new SecretKeySpec(secretKey.getEncoded(), AES);
  }

  private static GCMParameterSpec createGCMParameterSpec(byte[] initializationVector) {
    return new GCMParameterSpec(GCM_TAG_LENGTH, initializationVector);
  }
}
