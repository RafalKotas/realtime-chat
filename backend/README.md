# Realtime Chat Application

Simple real-time chat application built with Spring Boot.

Supports user registration, authentication (JWT).

---

## Running the application

### 1. Start database (Docker)
```bash
cd backend/tools
docker compose up -d
```

### 2. Run application

```bash
cd backend
mvn spring-boot:run
```

### 3. Run tests
```bash
cd backend
mvn spring-boot:run
```

---

## Features:

### Security
- User registration & login (JWT authentication)
- User profile endpoint (`/api/user/me`) - basic info about user
- Secure password hashing (BCrypt)
- Spring Security configuration, JWT-based (now only one token for authorization)

### Data
- JPA auditing (created and modified timestamps)

### Testing
- Full test coverage (unit + integration tests)

---

## Tech Stack
- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- PostgreSQL (Docker)
- Maven
- JUnit 5 / Mockito
- SonarQube

---

## Project Structure

```
src
├───main
│   ├───java
│   │   └───com
│   │       └───rafkot
│   │           └───chatapp
│   │               │   ChatappApplication.java
│   │               │   
│   │               ├───auth
│   │               │   │   AuthController.java
│   │               │   │   AuthenticationService.java
│   │               │   │   JwtService.java
│   │               │   │   RegistrationController.java
│   │               │   │   
│   │               │   └───dto
│   │               │           AuthenticationRequestDto.java
│   │               │           AuthenticationResponseDto.java
│   │               │           RegistrationRequestDto.java
│   │               │           RegistrationResponseDto.java
│   │               │           
│   │               ├───common
│   │               │       AuditableEntity.java
│   │               │       
│   │               ├───config
│   │               │       JpaConfig.java
│   │               │       JwtConfig.java
│   │               │       SecurityConfig.java
│   │               │       
│   │               └───user
│   │                   │   JpaUserDetailsService.java
│   │                   │   User.java
│   │                   │   UserProfileController.java
│   │                   │   UserRegistrationService.java
│   │                   │   UserRepository.java
│   │                   │   UserService.java
│   │                   │   
│   │                   ├───dto
│   │                   │       UserProfileDto.java
│   │                   │       
│   │                   ├───exception
│   │                   │       UserValidationException.java
│   │                   │       
│   │                   └───mapper
│   │                           UserMapper.java
│   │                           UserRegistrationMapper.java
│   │                           
│   └───resources
│       │   application.yaml
│       │   
│       ├───db
│       │   └───migration
│       │           V001__users.sql
│       │           
│       ├───jwt
│       │       app.key
│       │       app.pub
│       │       
│       ├───static
│       └───templates
└───test
    ├───java
    │   └───com
    │       └───rafkot
    │           └───chatapp
    │               │   ChatappApplicationTests.java
    │               │   
    │               ├───auth
    │               │   │   AuthControllerTest.java
    │               │   │   AuthenticationServiceTest.java
    │               │   │   JwtServiceTest.java
    │               │   │   RegistrationControllerTest.java
    │               │   │   
    │               │   └───dto
    │               │           AuthenticationRequestDtoTest.java
    │               │           AuthenticationResponseDtoTest.java
    │               │           RegistrationRequestDtoTest.java
    │               │           RegistrationResponseDtoTest.java
    │               │           
    │               ├───common
    │               ├───config
    │               │       JwtConfigTest.java
    │               │       SecurityBeansTest.java
    │               │       SecurityConfigTest.java
    │               │       TestSecurityConfig.java
    │               │       
    │               ├───security
    │               └───user
    │                   │   JpaUserDetailsServiceTest.java
    │                   │   UserProfileControllerTest.java
    │                   │   UserRegistrationServiceTest.java
    │                   │   UserServiceTest.java
    │                   │   
    │                   ├───dto
    │                   │       UserProfileDtoTest.java
    │                   │       
    │                   ├───exception
    │                   │       UserValidationExceptionTest.java
    │                   │       
    │                   └───mapper
    │                           UserMapperTest.java
    │                           UserRegistrationMapperTest.java
    │                           
    └───resources
        └───jwt
                private.pem
                public.pem
```

## Key Concepts

### JWT (JSON Web Token)
- Stateless authentication mechanism
- Tokens are signed using RSA private key
- Tokens are verified using RSA public key
- Used to authorize requests via `Authorization: Bearer <token>`

### Transaction Management
- `@Transactional` ensures atomic operations
- Prevents partial data persistence

### Validation Strategy
- Application-level validation (`existsBy...`)
- DB-level constraints (`UNIQUE`, `NOT NULL`)

### Security
- Stateless authentication (no sessions)
- Password hashing with BCrypt
- Endpoint protection using Spring Security

---

## Testing
- Unit tests (services, mappers, DTOs)
- Integration tests (controllers)
- Security tests (MockMvc)
- ~100% coverage (excluding bootstrap class)

---

## Notes
- JWT keys (RSA public/private) are stored in application resources (e.g. .pem files)
- Used for signing (private key) and verifying (public key) JWT tokens
- Designed with separation of concerns in mind

---

## Future Improvements (backend)
1. WebSocket real-time messaging
2. User contacts / conversations
3. Refresh tokens