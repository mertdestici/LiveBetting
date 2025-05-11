
# 📈 Live Betting API

This project is a live betting platform where betting odds are updated in real-time. It is built with Spring Boot 3, Java 17, and H2 in-memory database.

---

## 🚀 Features

- 📋 Create and retrieve match bulletins
- 🎯 Place bets on listed events
- 🔄 Real-time odds updates every second (scheduler-based)
- 👤 User registration with password encryption
- 🔐 Basic Authentication secured API
- ✅ Comprehensive unit test coverage

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security
- H2 In-Memory Database
- Gradle
- Lombok
- JUnit & Mockito

---

## ⚙️ How to Run

### 1. Build the Project

```bash
./gradlew build
```

### 2. Run the Application

```bash
./gradlew bootRun
```

---

## 🧪 Run Tests

```bash
./gradlew test
```

---

## 🐳 Run with Docker

### 1. Build Docker Image

```bash
docker build -t livebetting-app .
```

### 2. Run the Application

```bash
docker run -p 8080:8080 livebetting-app
```

---

## 🔐 Default Users

| Role     | Username | Password   |
|----------|----------|------------|
| Admin    | admin    | admin123   |

---

## 📮 API Endpoints (Postman-ready)
[Postman collection](Live%20Betting%20API.postman_collection.json)

### 🔐 Register User
```
POST /api/users/register
{
  "username": "user1",
  "password": "pass1"
}
```

### 📋 Create Match (Admin only)
```
POST /api/bulletin
Authorization: Basic (admin / admin123)
```

### 📃 Get All Matches
```
GET /api/bulletin
```

### 🎯 Place a Bet (Customer only)
```
POST /api/bets
Authorization: Basic (user1 / pass1)
```

---

## 🧪 Tested Components

- UserController
- BulletinController
- BetController
- UserService
- BulletinService
- BetService
- OddsUpdateScheduler

---

## 🗄 Database

- Uses H2 in-memory database
- H2 Console available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
