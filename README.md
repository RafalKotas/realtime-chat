# 💬 Real-time Chat App

A simple real-time chat app project built for learning purposes.

---

## 📌 Project status

🟡 Stage: User Authentication & Registration (Active Development)
📅 Last Updated: 2026-05-02

Currently:
- Backend with Spring Security & JWT Authentication ✅
- Frontend with React + TypeScript + Vite ✅
- User Registration & Login System ✅
- Docker setup for MySQL & SonarQube ✅

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
 ├── backend/        ← Spring Boot
 ├── frontend/       ← React
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

### 1️⃣ Backend Setup

#### Start MySQL & SonarQube containers

```bash
# Navigate to project root
cd realtime-chat

# Start Docker containers (MySQL on port 3336, SonarQube on port 9000)
docker-compose up -d

# Verify containers are running
docker-compose ps
```

### MySQL Connection Details:

Host: localhost:3336
Database: chat
User: chatuser
Password: <CREATE_YOUR_PASSWORD>

### SonarQube
- URL: [SonarQube dashboard](http://localhost:9000)
- Default credentials: `admin` / `admin`

---

### Install dependencies & run tests

``` bash
cd backend

# Run all tests with coverage
mvn clean verify

# or shorter version
mvn test
```

---

### Build the application

```bash
cd backend

# Clean build
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

---

### Start Spring Boot in development mode

```bash
cd backend

# Standard startup
./mvnw spring-boot:run

# OR with debug mode enabled
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--debug"
```

---

The backend will be available at: [backend basic endpoint](http://localhost:8080)

### API Endpoints:
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - Login (returns JWT tokens)
- `POST /api/auth/refresh` - Refresh access token
- `GET /api/user/me` - Get current user profile (requires authentication)

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
- ☑️ User Registration
- ☑️ Login with JWT
- ☐ Contact List
- ☑️ Real-time Messaging (WebSocket)
- ☐ (additional) Online Status
