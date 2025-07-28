# 🗳️ Online Voting System

This is a Java-based RESTful API project for managing an **Online Voting System**, built with **Spring Boot** and using **PostgreSQL** as the database. The system supports two user roles – **Elector** and **Supervisor** – and provides a secure, feature-rich environment for online elections.

---

## ✅ Features

- [x] Elector and Supervisor roles
- [x] Voting management
- [x] Election creation and control
- [x] Optional 2-Factor Authentication (TOTP)
- [x] JWT-based login and logout
- [x] Mail-based credential recovery (Supports `abv.bg` and `gmail.com`)
- [x] 59 REST APIs for full system interaction

---

## 🛠️ Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Security:** Spring Security, JSON Web Tokens (JWT), 2FA (TOTP)  
- **Database:** PostgreSQL  
- **Build Tool:** Maven  
- **API Type:** REST  
- **Mail Integration:** Spring MailSender  
- **Authentication:** JWT Tokens + Optional 2FA  

---

## 📦 Project Structure

📦 election-express/<br>
 ├── 📁 src/<br>
 │    └── 📁 main/<br>
 │       ├── 📁 java/<br>
 │       │   └── 📁 com/core/electionsystem/<br>
 │       │       ├── 📄 ElectionSystemMain.java/<br>
 │       │       ├── 📁 configuration/<br>
 |       │       │    ├── 📄 ApplicationConfiguration.java/<br>
 |       │       │    ├── 📄 CrossOriginResourceSharingConfiguration.java/<br>
 |       │       │    ├── 📄 EmailConfiguration/<br>
 |       │       │    └── 📁 security/<br>
 |       │       │        ├── 📄 SecuredAccessDeniedHandler.java/<br>
 |       │       │        ├── 📄 SecuredAuthenticationEntryPoint.java/<br>
 |       │       │        ├── 📄 SecuredLogoutHandler.java/<br>
 |       │       │        ├── 📄 SecurityConfiguration.java/<br>
 |       │       │        ├── 📁 filter/<br>
 |       │       │        |    ├── 📄 JwtFilter.java/<br>
 |       │       │        |    └── 📄 SecondAuthenticationFilter.java/<br>
 |       │       │        └── 📁 utility/<br>
 |       │       │            ├── 📄 BufferedResponseWrapper.java/<br>
 |       │       │            └── 📄 SecurityUtility.java/<br>
 │       │       ├── 📁 controller/<br>
 |       │       |   ├── 📄 ControllerAdvisor.java/<br>
 |       │       |   ├── 📄 ForgottenPropertiesController.java/<br>
 |       │       |   └── 📄 SecondAuthenticationFactorController.java/<br>
 │       │       ├── 📁 dto/<br>
 |       │       |   ├── 📄 LoginUserDTO.java/<br>
 |       │       |   ├── 📄 PasswordHolderDTO.java/<br>
 |       │       |   ├── 📄 PhoneNumberHolderDTO.java/<br>
 |       │       |   ├── 📄 SecretAnswerHolderDTO.java/<br>
 |       │       |   ├── 📁 mfa/<br>
 |       │       |   |   ├── 📄 TwoFactorAuthenticationActivatorDTO.java/<br>
 |       │       |   |   ├── 📄 TwoFactorAuthenticationRemoverForElectorDTO.java/<br>
 |       │       |   |   └── 📄 TwoFactorAuthenticationRemoverForSupervisorDTO.java/<br>
 |       │       |   └── 📁 recovery/<br>
 |       │       |       ├── 📄 PasswordRecovererDTO.java/<br>
 |       │       |       ├── 📄 PasswordUpdaterDTO.java/<br>
 |       │       |       ├── 📄 SecretAnswerRecovererDTO.java/<br>
 |       │       |       └── 📄 SecretAnswerUpdaterDTO.java/<br>
 │       │       ├── 📁 election/<br>
 |       │       |   ├── 📁 controller/<br>
 |       │       |   |   └── 📄 ElectionController.java/<br>
 |       │       |   ├── 📁 dto/<br>
 |       │       |   |   ├── 📄 CandidateCreatorDTO.java/<br>
 |       │       |   |   ├── 📄 ElectionEventCreatorDTO.java/<br>
 |       │       |   |   ├── 📄 PreferenceCreatorDTO.java/<br>
 |       │       |   |   └── 📄 VoteCreatorDTO.java/<br>
 |       │       |   ├── 📁 exception/<br>
 |       │       |   |   ├── 📄 AlreadyVotedException.java/<br>
 |       │       |   |   ├── 📄 InvalidCandidateIdException.java/<br>
 |       │       |   |   ├── 📄 InvalidCandidateNameException.java/<br>
 |       │       |   |   ├── 📄 InvalidDescriptionException.java/<br>
 |       │       |   |   ├── 📄 InvalidElectionEventTimingException.java/<br>
 |       │       |   |   ├── 📄 InvalidElectionIdException.java/<br>
 |       │       |   |   ├── 📄 InvalidPreferenceIdException.java/<br>
 |       │       |   |   ├── 📄 InvalidPreferenceNameException.java/<br>
 |       │       |   |   ├── 📄 InvalidStatusException.java/<br>
 |       │       |   |   ├── 📄 InvalidTitleException.java/<br>
 |       │       |   |   ├── 📄 NonExistentCandidateException.java/<br>
 |       │       |   |   ├── 📄 NonExistentElectionException.java/<br>
 |       │       |   |   └── 📄 NonExistentPreferenceException.java/<br>
 |       │       |   ├── 📁 model/<br>
 |       │       |   |   ├── 📄 Election.java/<br>
 |       │       |   |   ├── 📁 properties/<br>
 |       │       |   |   |   ├── 📄 Status.java/<br>
 |       │       |   |   |   ├── 📁 candidate/<br>
 |       │       |   |   |   |   ├── 📄 Candidate.java/<br>
 |       │       |   |   |   |   └── 📄 CandidateCompositeId.java/<br>
 |       │       |   |   |   ├── 📁 preference/<br>
 |       │       |   |   |   |   ├── 📄 Preference.java/<br>
 |       │       |   |   |   |   └── 📄 PreferenceCompositeId.java/<br>
 |       │       |   |   |   └── 📁 vote/<br>
 |       │       |   |   |       └── 📄 Vote.java/<br>
 |       │       |   ├── 📁 repository/<br>
 |       │       |   |   └── 📄 ElectionRepository.java/<br>
 |       │       |   ├── 📁 scheduler/<br>
 |       │       |   |   └── 📄 ElectionStatusScheduler.java/<br>
 |       │       |   ├── 📁 service/<br>
 |       │       |   |   └── 📄 ElectionService.java/<br>
 |       │       |   └── 📁 utility/<br>
 |       │       |       ├── 📄 ElectionUtility.java/<br>
 |       │       |       └── 📄 StatusAttributeConverter.java/<br>
 │       │       ├── 📁 elector/<br>
 |       │       |   ├── 📁 controller/<br>
 |       │       |   |   └── 📄 ElectorController.java/<br>
 |       │       |   ├── 📁 dto/<br>
 |       │       |   |   ├── 📄 ElectorDTO.java/<br>
 |       │       |   |   └── 📄 ElectorResidenceDTO.java/<br>
 |       │       |   ├── 📁 exception/<br>
 |       │       |   |   ├── 📄 AlreadyUsedDocumentIdException.java/<br>
 |       │       |   |   ├── 📄 AlreadyUsedEgnException.java/<br>
 |       │       |   |   ├── 📄 EmptyDateParameterException.java/<br>
 |       │       |   |   ├── 📄 EmptyNameParameterException.java/<br>
 |       │       |   |   ├── 📄 EmptyRegionParameterException.java/<br>
 |       │       |   |   ├── 📄 EmptySexParameterException.java/<br>
 |       │       |   |   ├── 📄 IllegalElectorPropertyException.java/<br>
 |       │       |   |   ├── 📄 InvalidDateParameterException.java/<br>
 |       │       |   |   ├── 📄 InvalidDocumentIdException.java/<br>
 |       │       |   |   ├── 📄 InvalidEgnException.java/<br>
 |       │       |   |   ├── 📄 InvalidSexParameterException.java/<br>
 |       │       |   |   ├── 📄 InvalidSexParameterLengthException.java/<br>
 |       │       |   |   ├── 📄 InvalidValueForDocumentIdException.java/<br>
 |       │       |   |   ├── 📄 InvalidValueForEgnException.java/<br>
 |       │       |   |   ├── 📄 InvalidValueForSexAttributeException.java/<br>
 |       │       |   |   ├── 📄 NonExistentElectorException.java/<br>
 |       │       |   |   └── 📄 UserIsNotAdultException.java/<br>
 |       │       |   ├── 📁 model/<br>
 |       │       |   |   ├── 📄 Elector.java/<br>
 |       │       |   |   ├── 📁 properties/<br>
 |       │       |   |   |   ├── 📄 ElectorBearerToken.java/<br>
 |       │       |   |   |   ├── 📄 ElectorCredentials.java/<br>
 |       │       |   |   |   ├── 📄 ElectorMetadata.java/<br>
 |       │       |   |   |   ├── 📄 ElectorName.java/<br>
 |       │       |   |   |   ├── 📄 ElectorResidence.java/<br>
 |       │       |   |   |   └── 📄 Sex.java/<br>
 |       │       |   ├── 📁 repository/<br>
 |       │       |   |   ├── 📄 ElectorBearerTokenRepository.java/<br>
 |       │       |   |   └── 📄 ElectorRepository.java/<br>
 |       │       |   ├── 📁 service/<br>
 |       │       |   |   └── 📄 ElectorService.java/<br>
 |       │       |   └── 📁 utility/<br>
 |       │       |       ├── 📄 ElectorUtility.java/<br>
 |       │       |       └── 📄 SexAttributeConverter.java/<br>
 │       │       ├── 📁 exception/<br>
 |       │       |   ├── 📄 AlreadyOnboarded2faException.java/<br>
 |       │       |   ├── 📄 AlreadyUsedEmailException.java/<br>
 |       │       |   ├── 📄 AlreadyUsedPhoneNumberException.java/<br>
 |       │       |   ├── 📄 IllegalStateForTokenOwnerException.java/<br>
 |       │       |   ├── 📄 IllegalStateForTokenTypeException.java/<br>
 |       │       |   ├── 📄 IllegalStateForTokenValueException.java/<br>
 |       │       |   ├── 📄 IncorrectPasswordException.java/<br>
 |       │       |   ├── 📄 IncorrectSecretAnswerException.java/<br>
 |       │       |   ├── 📄 IncorrectValueForPhoneNumberException.java/<br>
 |       │       |   ├── 📄 IncorrectValueForUserIdException.java/<br>
 |       │       |   ├── 📄 InvalidArgumentsForElectorException.java/<br>
 |       │       |   ├── 📄 InvalidArgumentsForSupervisorException.java/<br>
 |       │       |   ├── 📄 InvalidPasswordException.java/<br>
 |       │       |   ├── 📄 InvalidSecurityAlgorithmException.java/<br>
 |       │       |   ├── 📄 InvalidValueForEmailException.java/<br>
 |       │       |   ├── 📄 InvalidValueForNameException.java/<br>
 |       │       |   ├── 📄 InvalidValueForPhoneNumberException.java/<br>
 |       │       |   ├── 📄 InvalidValueForSecretAnswerException.java/<br>
 |       │       |   ├── 📄 InvalidValueForStatusAttributeException.java/<br>
 |       │       |   ├── 📄 InvalidValueForTokenTypeAttributeException.java/<br>
 |       │       |   ├── 📄 InvalidValueForTotpCodeException.java/<br>
 |       │       |   ├── 📄 InvalidValueForUserIdException.java/<br>
 |       │       |   ├── 📄 NonExistentJsonWebTokenException.java/<br>
 |       │       |   ├── 📄 NonExistentUserException.java/<br>
 |       │       |   ├── 📄 NonExistentUserRoleException.java/<br>
 |       │       |   ├── 📄 OtpAuthURISchemeGenerationException.java/<br>
 |       │       |   ├── 📄 QrCodeGenerationFailureException.java/<br>
 |       │       |   ├── 📄 SecretKeyDecryptionException.java/<br>
 |       │       |   ├── 📄 SecretKeyEncryptionException.java/<br>
 |       │       |   ├── 📄 TotpCodeVerificationSystemFailureException.java/<br>
 |       │       |   ├── 📄 UnauthenticatedUserException.java/<br>
 |       │       |   └── 📄 UnknownAuthorityException.java/<br>
 │       │       ├── 📁 model/<br>
 |       │       |   └── 📄 CustomizedUserDetails.java/<br>
 │       │       ├── 📁 service/<br>
 |       │       |   ├── 📄 CustomizedUserDetailsService.java/<br>
 |       │       |   ├── 📄 ForgottenPropertiesService.java/<br>
 |       │       |   ├── 📄 JwtService.java/<br>
 |       │       |   └── 📄 SecondAuthenticationFactorService.java/<br>
 │       │       ├── 📁 supervisor/<br>
 |       │       |   ├── 📁 controller/<br>
 |       │       |   |   └── 📄 SupervisorController.java/<br>
 |       │       |   ├── 📁 dto/<br>
 |       │       |   |   └── 📄 SupervisorDTO.java/<br>
 |       │       |   ├── 📁 exception/<br>
 |       │       |   |   ├── 📄 InvalidValueForSupervisorIdException.java/<br>
 |       │       |   |   └── 📄 NonExistentSupervisorException.java/<br>
 |       │       |   ├── 📁 model/<br>
 |       │       |   |   ├── 📄 Supervisor.java/<br>
 |       │       |   |   ├── 📁 properties/<br>
 |       │       |   |   |   ├── 📄 SupervisorBearerToken.java/<br>
 |       │       |   |   |   └── 📄 SupervisorMetadata.java/<br>
 |       │       |   ├── 📁 repository/<br>
 |       │       |   |   ├── 📄 SupervisorBearerTokenRepository.java/<br>
 |       │       |   |   └── 📄 SupervisorRepository.java/<br>
 |       │       |   ├── 📁 service/<br>
 |       │       |   |   └── 📄 SupervisorService.java/<br>
 |       │       |   └── 📁 utility/<br>
 |       │       |       └── 📄 SupervisorUtility.java/<br>
 │       │       └── 📁 utility/<br>
 |       │           ├── 📄 ElectionSystemUtility.java/<br>
 |       │           ├── 📄 EmailUtility.java/<br>
 |       │           ├── 📄 Role.java/<br>
 |       │           ├── 📄 TokenType.java/<br>
 |       │           └── 📄 TokenTypeAttributeConverter.java/<br>
 │       └── 📁 resources/<br>
 │           └── 📄 application.yml<br>
 ├── 📄 pom.xml<br>
 └── 📄 README.md<br>

---

## 🔐 Security Highlights

- JWT-Based Secure authentication and authorization
- Role-Based Access Control (Elector And Supervisor)
- Optional 2FA - Using a Smartphone Authenticator App
- Password Recovery via Email (SMTP support for `Gmail.com` and `ABV.bg`)

---

## 🔗 Frontend

The Frontend for this Project is built with `React.js` and is available here:  
👉 [ElectionExpressUI](https://github.com/Orlin99/ElectionExpressUI)
