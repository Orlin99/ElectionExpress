# 🗳️ Election Express

**Election Express** is a secure, extensible Java-based **Spring Boot** web application designed to manage and conduct online elections. Built with **PostgreSQL**, it supports two user roles: **Elector** (voter) and **Supervisor** (administrator). It offers a comprehensive suite of **59 REST APIs**, token-based **authentication (JWT)**, optional **2-Factor Authentication (2FA)**, and robust **account recovery via email**.

---

## ✅ Features

- [x] Role-Based Access: Elector & Supervisor
- [x] Secure Login/Logout with JWT
- [x] Optional 2FA using smartphone-based OTP
- [x] Email recovery support (`abv.bg` and `gmail`)
- [x] Voting and Election Management
- [x] 59+ REST APIs
- [x] Spring Security + Custom Filters
- [x] React.js Frontend ([View Here](https://github.com/Orlin99/ElectionExpressUI))
- [x] Scheduler to auto-update election statuses

---

## 🛠️ Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security, JWT, 2FA (TOTP)
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Frontend:** React.js (separate repo)
- **API Style:** REST
- **Mail Support:** Gmail & abv.bg
- **Auth:** JWT, 2FA, Email Recovery

---

## 📦 Project Structure

📦 `election-express`  
├── 📁 `src`  
│  └── 📁 `main`  
│    ├── 📁 `java`  
│    │  └── 📁 `com/core/electionsystem`  
│    │    ├── 📄 `ElectionSystemMain.java`  
│    │    ├── 📁 `configuration`  
│    │    │  ├── 📄 `ApplicationConfiguration.java`  
│    │    │  ├── 📄 `CrossOriginResourceSharingConfiguration.java`  
│    │    │  ├── 📄 `EmailConfiguration`  
│    │    │  └── 📁 `security`  
│    │    │    ├── 📄 `SecuredAccessDeniedHandler.java`  
│    │    │    ├── 📄 `SecuredAuthenticationEntryPoint.java`  
│    │    │    ├── 📄 `SecuredLogoutHandler.java`  
│    │    │    ├── 📄 `SecurityConfiguration.java`  
│    │    │    ├── 📁 `filter`  
│    │    │    │  ├── 📄 `JwtFilter.java`  
│    │    │    │  └── 📄 `SecondAuthenticationFilter.java`  
│    │    │    └── 📁 `utility`  
│    │    │      ├── 📄 `BufferedResponseWrapper.java`  
│    │    │      └── 📄 `SecurityUtility.java`  
│    │    ├── 📁 `controller`  
│    │    ├── 📁 `dto`  
│    │    ├── 📁 `election`  
│    │    ├── 📁 `elector`  
│    │    ├── 📁 `supervisor`  
│    │    ├── 📁 `exception`  
│    │    ├── 📁 `model`  
│    │    ├── 📁 `repository`  
│    │    ├── 📁 `service`  
│    │    └── 📁 `utility`  
│    └── 📁 `resources`  
│      └── 📄 `application.yml`  
├── 📄 `pom.xml`  
└── 📄 `README.md`  

> 🔎 **Note:** Full class breakdown is visible in the repository tree. It includes DTOs, domain models, services, custom exceptions, schedulers, and security logic.

---

## 🔒 Authentication Overview

- **JWT Tokens:** Stateless session for Elector/Supervisor
- **2FA:** Optional onboarding via TOTP (QR Code for smartphone)
- **Email Recovery:** Built-in support for `abv.bg` and `gmail` SMTP
- **Custom Filters:** JWT + 2FA filters in Spring Security chain

---

## 🧪 API Overview

A total of **59 REST endpoints** grouped into:

- `/elector`: Voting operations
- `/supervisor`: Election management
- `/recovery`: Password and secret answer recovery
- `/election`: Create/manage election events, candidates, preferences

---

## 📎 Frontend Project

The React.js frontend for this system is hosted separately.  
👉 [Election Express Frontend (React)](https://github.com/Orlin99/ElectionExpressUI)
