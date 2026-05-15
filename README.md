# 💬 Real-time Chat App

A simple real-time chat app project built for learning purposes.

---

## 📌 Project status

🟢 Stage: Real-time Messaging & ngrok Integration 
📅 Last Updated: 2026-05-15

Currently:
- Backend with Spring Security & JWT Authentication ✅
- Frontend with React + TypeScript + Vite ✅
- User Registration & Login System ✅
- Docker setup for MySQL & SonarQube ✅
- Refresh Token via HttpOnly Cookie
- WebSocket (STOMP) real-time messaging
- Contact List
- ngrok tunneling (free plan)

---

## 🛠️ Technologies

### 
- Java 21 + Spring Boot 4.0.5
- Spring Web MVC
- Spring Data JPA
- Spring Security (JWT-based)
- MySQL (via Docker)
- JUnit 5 + Mockito (Full test coverage)
- SonarQube for code quality

### Frontend
- React 18 + TypeScript
- Vite
- shadcn/ui + Tailwind CSS v4
- React Router v7
- Axios (with authentication)
- Zustand (state management)

---

## 📁 Project structure

```
realtime-chat/
 ├── backend/        ← Spring Boot API + WebSocket
 ├── frontend/       ← React + TS client
 ├── docs/
 │    ├── backend/   ← tutorial/documentation BE
 │    ├── frontend/  ← tutorial/documentation FE
 │    └── shared/    ← API + flow
 ├── .gitignore
 └── README.md
```

---

## 🚀 Launch Instructions

### Prerequisites

- Java 21+
- Node.js 18+ & npm
- Docker & Docker Compose
- Maven (or use `./mvnw` wrapper)

---

## 1️⃣ Backend Setup

### Start MySQL & SonarQube containers

```bash
# Navigate to project root
cd realtime-chat/backend/tools

# Start Docker containers (MySQL on port 3336, SonarQube on port 9000)
docker-compose up -d

# Verify containers are running
docker-compose ps
```

**MySQL**
- Host: `localhost:3336`
- Database: `chat`
- User: `chatuser`
- Password: (set in docker-compose)

**SonarQube**
- URL: [SonarQube dashboard](http://localhost:9000)
- Default credentials: `admin` / `admin`

---

## Testing

### Backend Tests

```bash
cd backend

# Run all tests
mvn test

# Run tests with coverage report
mvn clean verify

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with output
mvn test -X
```

**Coverage reports:** `backend/target/site/jacoco/index.html`

---

**Run backend tests**

```bash
# go to main folder
cd realtime-chat/backend/
# Run all tests with coverage
mvn clean verify
```

**Start backend**
```bash
cd backend

# Standard startup
./mvnw spring-boot:run

# OR with debug mode enabled
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--debug"
---

**Build the application**

```bash
# go to main folder
cd realtime-chat/backend/

# Clean build
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

Backend runs at: [backend basic endpoint](http://localhost:8080)

### API Endpoints:
- `POST /api/auth/register` - User registration [`AuthController`]
- `POST /api/auth/refresh` - Refresh access token (requires authentication with refresh token) [`RegistrationController`]
- `POST /api/auth/login` - Login (returns JWT tokens) [`AuthController`]
- `GET /api/user/me` - Get current user profile (requires authentication) [`UserProfileController`]
- `POST /api/userc/hange-password` - Changes user password (requires authentication) [`UserProfileController`]
- `GET /api/contacts` - Get all user's (`@RequestParam String username`) contacts [`ContactController`]
- `DELETE /api/contacts/{id}` - Deletes contact with given id (`@PathVariable`) (requires authentication) [`ContactController`] - **REMOVE (unused)**
- `GET /api/message/all/{uuid}` - Get map of user's with uuid(`String`, `@PathVariable`) non-empty chats (at least 1 message) as Map<String, List<MessageResponseDto>>
- `GET /api/message/{userId}/{partnerUsername} - Get messages between user with userId (`UUID`, `@PathVariable`) and partnerUsername(`String`, `@PathVariable`) as List<MessageResponseDto>
- `app/chat.send` - websocket endpoint for message sending, used internally


---

### 2️⃣ Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server with hot reload
npm run dev

# OR build for production
npm run build

# Preview production build
npm run preview
```

The frontend will be available at: [frontend localhost](http://localhost:5173)

---

### 3️⃣ ngrok Tunneling (Free Plan)
The free ngrok plan injects a browser warning page that breaks CORS and Websocket connections.

To bypass it, **every Axios request must include:**.

```TypeScript
"ngrok-skip-browser-warning": "true"
```

This is already configured in:

```bash
frontend/src/authentication/authClient.ts
```

### WebSocket URL example:
```
wss://<your-ngrok-domain>.ngrok-free-dev/ws-raw
```

### Refresh Token Cookie

The backend sets a secure, cross-site cookie:

```Java
ResponseCookie.from("refreshToken", token)
.httpOnly(true)
.secure(true)
.sameSite("None")
.partitioned(true)
.path("/")
.build();
```

This ensures compatibility with:
- HTTPS
- ngrok
- Chrome / Firefox strict cookie policies

---

### 3️⃣ Code Quality Analysis (SonarQube)

Ensure SonarQube container is running (see step 1).

#### Generate SonarQube login token

1. Navigate to [SonarQube dashboard main page](http://localhost:9000)
2. Login with `admin` / `admin`
3. Go to **User Menu → My Account → Security**
4. Generate a token (e.g., `sqa_123456789abcdefghi`)

#### Run code analysis

```bash
cd backend

# Replace <YOUR_SONAR_TOKEN> with your generated token
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=chatapp \
  -Dsonar.login=<YOUR_SONAR_TOKEN>
```

#### View results at: (http://localhost:9000/projects)

---

## Development Workflow

### Full local setup (all services)

```bash
# 1. Start containers
docker-compose up -d

# 2. Terminal 1 - Backend
cd backend
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--debug"

# 3. Terminal 2 - Frontend
cd frontend
npm run dev

# 4. Access the app
# Frontend: http://localhost:5173
# Backend: http://localhost:8080
# SonarQube: http://localhost:9000
```

### Stop all services
```bash
# Stop Docker containers
docker-compose down

# Optional: Remove volumes (clears database)
docker-compose down -v
```

---

## 🧭 Feature Plan
Basic:
- ☑️ User Registration
- ☑️ Login with JWT
- ☑️ Real-time Messaging (WebSocket)
- ☑️ Refresh Token (HttpOnly cookie)
- ☑️ Contact List
- ☑️ ngrok tunneling support
Additional
- ☐ Online Status
- ☐ Message Read/Unread
- ☐ UI improvements
