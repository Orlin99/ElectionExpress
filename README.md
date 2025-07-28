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

📦 election-express  
├── 📁 src  
│   └── 📁 main  
│       ├── 📁 java  
│       │   └── 📁 com/core/electionsystem  
│       │       ├── 📄 ElectionSystemMain.java  
│       │       ├── 📁 configuration  
│       │       │   ├── 📄 ApplicationConfiguration.java  
│       │       │   ├── 📄 CrossOriginResourceSharingConfiguration.java  
│       │       │   ├── 📄 EmailConfiguration.java  
│       │       │   └── 📁 security  
│       │       │       ├── 📄 SecuredAccessDeniedHandler.java  
│       │       │       ├── 📄 SecuredAuthenticationEntryPoint.java  
│       │       │       ├── 📄 SecuredLogoutHandler.java  
│       │       │       ├── 📄 SecurityConfiguration.java  
│       │       │       ├── 📁 filter  
│       │       │       │   ├── 📄 JwtFilter.java  
│       │       │       │   └── 📄 SecondAuthenticationFilter.java  
│       │       │       └── 📁 utility  
│       │       │           ├── 📄 BufferedResponseWrapper.java  
│       │       │           └── 📄 SecurityUtility.java  
│       │       ├── 📁 controller  
│       │       │   ├── 📄 ControllerAdvisor.java  
│       │       │   ├── 📄 ForgottenPropertiesController.java  
│       │       │   └── 📄 SecondAuthenticationFactorController.java  
│       │       ├── 📁 dto  
│       │       │   ├── 📄 LoginUserDTO.java  
│       │       │   ├── 📄 PasswordHolderDTO.java  
│       │       │   ├── 📄 PhoneNumberHolderDTO.java  
│       │       │   ├── 📄 SecretAnswerHolderDTO.java  
│       │       │   ├── 📁 mfa  
│       │       │   │   ├── 📄 TwoFactorAuthenticationActivatorDTO.java  
│       │       │   │   ├── 📄 TwoFactorAuthenticationRemoverForElectorDTO.java  
│       │       │   │   └── 📄 TwoFactorAuthenticationRemoverForSupervisorDTO.java  
│       │       │   └── 📁 recovery  
│       │       │       ├── 📄 PasswordRecovererDTO.java  
│       │       │       ├── 📄 PasswordUpdaterDTO.java  
│       │       │       ├── 📄 SecretAnswerRecovererDTO.java  
│       │       │       └── 📄 SecretAnswerUpdaterDTO.java  
│       │       ├── 📁 election  
│       │       │   ├── 📁 controller  
│       │       │   │   └── 📄 ElectionController.java  
│       │       │   ├── 📁 dto  
│       │       │   │   ├── 📄 CandidateCreatorDTO.java  
│       │       │   │   ├── 📄 ElectionEventCreatorDTO.java  
│       │       │   │   ├── 📄 PreferenceCreatorDTO.java  
│       │       │   │   └── 📄 VoteCreatorDTO.java  
│       │       │   ├── 📁 exception  
│       │       │   │   └── 📄 ... (Multiple Election-specific Exceptions)  
│       │       │   ├── 📁 model  
│       │       │   │   ├── 📄 Election.java  
│       │       │   │   └── 📁 properties  
│       │       │   │       ├── 📄 Status.java  
│       │       │   │       ├── 📁 candidate  
│       │       │   │       │   ├── 📄 Candidate.java  
│       │       │   │       │   └── 📄 CandidateCompositeId.java  
│       │       │   │       ├── 📁 preference  
│       │       │   │       │   ├── 📄 Preference.java  
│       │       │   │       │   └── 📄 PreferenceCompositeId.java  
│       │       │   │       └── 📁 vote  
│       │       │   │           └── 📄 Vote.java  
│       │       │   ├── 📁 repository  
│       │       │   │   └── 📄 ElectionRepository.java  
│       │       │   ├── 📁 scheduler  
│       │       │   │   └── 📄 ElectionStatusScheduler.java  
│       │       │   ├── 📁 service  
│       │       │   │   └── 📄 ElectionService.java  
│       │       │   └── 📁 utility  
│       │       │       ├── 📄 ElectionUtility.java  
│       │       │       └── 📄 StatusAttributeConverter.java  
│       │       ├── 📁 elector  
│       │       │   ├── 📁 controller  
│       │       │   │   └── 📄 ElectorController.java  
│       │       │   ├── 📁 dto  
│       │       │   │   ├── 📄 ElectorDTO.java  
│       │       │   │   └── 📄 ElectorResidenceDTO.java  
│       │       │   ├── 📁 exception  
│       │       │   │   └── 📄 ... (Multiple Elector-specific Exceptions)  
│       │       │   ├── 📁 model  
│       │       │   │   ├── 📄 Elector.java  
│       │       │   │   └── 📁 properties  
│       │       │   │       └── 📄 ... (Elector Properties)  
│       │       │   ├── 📁 repository  
│       │       │   │   └── 📄 ElectorRepository.java  
│       │       │   ├── 📁 service  
│       │       │   │   └── 📄 ElectorService.java  
│       │       │   └── 📁 utility  
│       │       │       └── 📄 ElectorUtility.java  
│       │       ├── 📁 exception  
│       │       │   └── 📄 ... (Shared application-wide exceptions)  
│       │       ├── 📁 model  
│       │       │   └── 📄 CustomizedUserDetails.java  
│       │       ├── 📁 service  
│       │       │   ├── 📄 CustomizedUserDetailsService.java  
│       │       │   ├── 📄 ForgottenPropertiesService.java  
│       │       │   ├── 📄 JwtService.java  
│       │       │   └── 📄 SecondAuthenticationFactorService.java  
│       │       ├── 📁 supervisor  
│       │       │   ├── 📁 controller  
│       │       │   │   └── 📄 SupervisorController.java  
│       │       │   ├── 📁 dto  
│       │       │   │   └── 📄 SupervisorDTO.java  
│       │       │   ├── 📁 exception  
│       │       │   │   └── 📄 ...  
│       │       │   ├── 📁 model  
│       │       │   │   └── 📄 Supervisor.java  
│       │       │   ├── 📁 repository  
│       │       │   │   └── 📄 SupervisorRepository.java  
│       │       │   ├── 📁 service  
│       │       │   │   └── 📄 SupervisorService.java  
│       │       │   └── 📁 utility  
│       │       │       └── 📄 SupervisorUtility.java  
│       │       └── 📁 utility  
│       │           ├── 📄 ElectionSystemUtility.java  
│       │           ├── 📄 EmailUtility.java  
│       │           ├── 📄 Role.java  
│       │           ├── 📄 TokenType.java  
│       │           └── 📄 TokenTypeAttributeConverter.java  
│       └── 📁 resources  
│           └── 📄 application.yml  
├── 📄 pom.xml  
└── 📄 README.md

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
