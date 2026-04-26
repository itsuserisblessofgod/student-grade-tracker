# 🎓 Student Grade Tracker API

A simple REST API built with Spring Boot for managing students, courses, and grades.

## 🚀 Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Docker & Docker Compose

---

## 📦 Features

* Create, read, delete students
* Create and view courses
* Assign grades to students
* RESTful API structure
* Dockerized environment

---

## 🛠️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/student-grade-tracker.git
cd student-grade-tracker
```

---

### 2. Run with Docker

```bash
docker-compose up --build
```

---

## 🌐 API Endpoints

### Students

* `GET /students` → Get all students
* `GET /students/{id}` → Get student by ID
* `POST /students` → Create student
* `DELETE /students/{id}` → Delete student

### Courses

* `GET /courses` → Get all courses
* `POST /courses` → Create course

### Grades

* `POST /grades?studentId=1&courseId=1&score=90`

---

## 📥 Example Requests

### Create Student

```json
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

### Create Course

```json
{
  "name": "Mathematics"
}
```

---

## 🐳 Docker Setup

* App runs on: `http://localhost:8080`
* PostgreSQL runs on: `localhost:5433` (if configured)

---

## 📌 Future Improvements

* DTO layer
* Validation (`@Valid`)
* Global exception handling
* Swagger API documentation
* Authentication (JWT)

---

## 👨‍💻 Author

Zhanassyl Bekmurat
