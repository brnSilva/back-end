# Challenge API - Secure Card Management

## Overview

This project is a secure API for card registration and lookup, developed as part of a technical challenge.

The API supports:

- OAuth2 authentication with JWT
- Secure card storage using AES encryption and SHA-256 hashing
- Card lookup without exposing sensitive data
- TXT batch upload processing
- Partial processing with detailed error handling
- Structured logging with correlation IDs
- Hexagonal architecture

---

# Technologies

- Java 21
- Spring Boot 3.5
- Spring Security
- OAuth2 Authorization Server
- JWT
- Spring Data JPA
- H2 Database
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Gradle

---

# Architecture

The project follows a Hexagonal Architecture (Ports and Adapters) approach.

```text
src/main/java/br/com/challenge
├── domain
├── application
├── adapter
└── config
```

---

# Security

Sensitive card data is protected using:

## SHA-256 Hash

Used for:
- secure lookup
- uniqueness validation

## AES Encryption

Used for:
- secure storage of card numbers

The API never exposes raw card data.

## Authentication

The API uses OAuth2 with JWT tokens.

---

# Generate Token

```http
POST /api/v1/oauth2/token
```

## Authorization - Basic Auth

| Username | Password |
|---|---|
| challenge-client | 123456 |

## Body (x-www-form-urlencoded)

```text
grant_type=client_credentials
```

## Example cURL

```bash
curl --location 'http://localhost:8080/api/v1/oauth2/token' \
--header 'Authorization: Basic Y2hhbGxlbmdlLWNsaWVudDoxMjM0NTY=' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=client_credentials'
```

## Example Response

```json
{
  "access_token": "jwt-token",
  "token_type": "Bearer",
  "expires_in": 300
}
```

---

# Swagger

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

---

# Running the Project

## Clone Repository

```bash
git clone https://github.com/brnSilva/back-end.git
```

## Run Application

```bash
./gradlew bootRun
```

---

# H2 Database Console

```text
http://localhost:8080/api/v1/h2-console
```

## Connection

| Property | Value |
|---|---|
| JDBC URL | jdbc:h2:mem:challenge |
| User | sa |
| Password | (empty) |

---

# API Endpoints

## Create Card

```http
POST /api/v1/cards
Authorization: Bearer <token>
```

### Request

```json
{
  "cardNumber": "4456897999999999"
}
```

### Response

```json
{
  "id": "uuid"
}
```

---

## Find Card

```http
GET /api/v1/cards/{cardNumber}
Authorization: Bearer <token>
```

### Response

```json
{
  "id": "uuid"
}
```

---

## Upload TXT File

```http
POST /api/v1/cards/upload
Authorization: Bearer <token>

Content-Type: multipart/form-data
Form Field: file
```

### Example Response

```json
{
  "processed": 10,
  "success": 6,
  "failed": 4,
  "errorDetails": [
    {
      "cardIdentifier": "C3",
      "message": "Card already exists"
    }
  ]
}
```

---

# Logging

The API uses structured logging with:

- correlation ID
- request duration
- masked card numbers

## Example

```text
[correlation-id] POST /api/v1/cards - status=201 - duration=45ms
```

Card numbers are masked:

```text
4456********9999
```

---

# Batch Processing Strategy

TXT uploads support:

- partial processing
- invalid line isolation
- duplicate validation
- resilient execution

The upload process does not stop when invalid records are found.

---

# Tests

The project contains unit tests for:

- value objects
- services
- adapters
- mappers
- controllers

Run tests:

```bash
./gradlew test
```

---

# Actuator

Health endpoint:

```text
GET /api/v1/actuator/health
```

---

# Scalability Considerations

The upload processing was implemented using a (`BufferedReader`) over the file's input stream to avoid loading the entire file into memory.

The application also includes:

- stateless services
- hash-based lookup
- database uniqueness constraints
- partial batch processing
- isolated error handling

These decisions allow the application to scale for large file processing scenarios.

---

# Possible Future Improvements

- PostgreSQL support
- Docker support
- Testcontainers
- Metrics and tracing
- Retry strategies
- Async batch processing
- Rate limiting

---

## Author: Bruno Batista da Silva
