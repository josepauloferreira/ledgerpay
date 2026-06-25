# LedgerPay

<p align="center">
  <a href="https://github.com/josepauloferreira/ledgerpay/actions/workflows/ci.yml">
    <img src="https://github.com/josepauloferreira/ledgerpay/actions/workflows/ci.yml/badge.svg" alt="CI"/>
  </a>
  <img src="https://img.shields.io/badge/Java-21-blue?logo=openjdk" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/build-Maven-orange?logo=apachemaven" alt="Maven"/>
  <img src="https://img.shields.io/badge/status-active-success" alt="Project status"/>
</p>

<p align="center">
  Java/Spring Boot backend for modeling digital wallets, treasury funding, peer-to-peer transfers, and financial domain rules.
</p>

---

## Overview

LedgerPay is a backend project built to explore financial-domain modeling with Java and Spring Boot.

The project currently exposes a REST API for creating wallets, funding wallets from a system treasury, and transferring money between wallets.

## Current capabilities

- Create wallets with an initial `0.00` balance.
- Retrieve wallets by id.
- Fund wallets from a system treasury.
- Transfer money between wallets.
- Validate request payloads at the API boundary.
- Enforce financial rules in the domain layer.
- Keep an in-memory record of successful money movements.
- Run automated validation through Maven and GitHub Actions CI.

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Build | Maven Wrapper |
| API | Spring Web |
| Validation | Bean Validation |
| Tests | JUnit 5, AssertJ, MockMvc |
| Formatting | Spotless, google-java-format |
| CI | GitHub Actions |
| Current storage | In-memory |

## Project structure

```text
src/main/java/io/github/josepauloferreira/ledgerpay
├── api
│   └── HTTP controllers, request DTOs, response DTOs, and API error handling
├── application
│   └── use cases that coordinate financial operations
├── domain
│   └── value objects, entities, domain services, and domain exceptions
└── infra
    └── in-memory storage and application configuration
```

Current dependency direction:

```text
HTTP layer → application use case → domain service → domain model
```

Controllers handle HTTP concerns. Use cases coordinate application flows. The domain layer owns financial rules and does not depend on Spring.

## Core concepts

| Concept | Responsibility |
|---|---|
| `Money` | Represents monetary values with fixed decimal scale and arithmetic behavior. |
| `Wallet` | Represents a wallet with identity and balance rules. |
| `SystemTreasury` | Represents the institutional source used to fund wallets. |
| `TreasuryFunding` | Applies domain rules for treasury-to-wallet funding. |
| `PeerTransfer` | Applies domain rules for wallet-to-wallet transfers. |
| `MoneyMovement` | Represents a completed financial movement. |
| `MoneyMovementHistory` | Keeps an in-memory record of successful financial movements. |
| `FundWalletUseCase` | Coordinates funding and records the resulting movement. |
| `TransferMoneyUseCase` | Coordinates peer transfers and records the resulting movement. |

## API

### Create wallet

```http
POST /wallets
```

Response:

```http
201 Created
Location: /wallets/{id}
```

```json
{
  "id": "wallet-id",
  "balance": "0.00"
}
```

### Get wallet

```http
GET /wallets/{id}
```

| Status | Meaning |
|---|---|
| `200 OK` | Wallet found. |
| `404 Not Found` | Wallet does not exist. |

### Fund wallet

```http
POST /wallets/{id}/funding
Content-Type: application/json
```

Request:

```json
{
  "amount": "100.00"
}
```

| Status | Meaning |
|---|---|
| `200 OK` | Wallet funded successfully. |
| `400 Bad Request` | Missing, invalid, zero, or negative amount. |
| `404 Not Found` | Wallet does not exist. |

Returns the updated wallet.

### Transfer between wallets

```http
POST /wallets/{sourceWalletId}/transfers
Content-Type: application/json
```

Request:

```json
{
  "targetWalletId": "target-wallet-id",
  "amount": "40.00"
}
```

Response:

```json
{
  "source": {
    "id": "source-wallet-id",
    "balance": "60.00"
  },
  "target": {
    "id": "target-wallet-id",
    "balance": "40.00"
  }
}
```

| Status | Meaning |
|---|---|
| `200 OK` | Transfer completed. |
| `400 Bad Request` | Invalid request, same-wallet transfer, insufficient balance, or domain rejection. |
| `404 Not Found` | Source or target wallet does not exist. |

## Running locally

Requirements:

- Java 21
- Git

Clone and run:

```bash
git clone https://github.com/josepauloferreira/ledgerpay.git
cd ledgerpay
./mvnw spring-boot:run
```

The API starts at:

```text
http://localhost:8080
```

## Configuration

LedgerPay currently runs with the default Spring Boot configuration.

No external services are required at this stage:

- no database;
- no Docker Compose;
- no message broker;
- no environment variables required for local execution.

The current storage is in memory, so application state is reset when the process stops.

## Example requests

Create a wallet:

```bash
curl -i -X POST http://localhost:8080/wallets
```

Get a wallet:

```bash
curl -i http://localhost:8080/wallets/{walletId}
```

Fund a wallet:

```bash
curl -i -X POST http://localhost:8080/wallets/{walletId}/funding \
  -H 'Content-Type: application/json' \
  -d '{"amount":"100.00"}'
```

Transfer between wallets:

```bash
curl -i -X POST http://localhost:8080/wallets/{sourceWalletId}/transfers \
  -H 'Content-Type: application/json' \
  -d '{"targetWalletId":"{targetWalletId}","amount":"40.00"}'
```

## Testing and validation

Run the full validation pipeline:

```bash
./mvnw clean verify
```

If `make` is available:

```bash
make verify
```

Useful commands:

| Command | Description |
|---|---|
| `make test` | Runs the test suite. |
| `make verify` | Runs the full validation pipeline. |
| `make format` | Applies Java formatting. |
| `make format-check` | Checks formatting without modifying files. |
| `make clean` | Removes build artifacts. |

The test suite currently covers:

- monetary invariants and arithmetic;
- wallet identity and balance rules;
- treasury funding;
- peer transfers;
- money movement history;
- application use cases;
- Spring Boot context loading;
- HTTP wallet creation and lookup;
- HTTP funding and transfer flows;
- `400 Bad Request` and `404 Not Found` scenarios.

GitHub Actions runs `./mvnw clean verify` on pull requests and pushes to `main`.

## Architecture decisions

| Decision | Rationale |
|---|---|
| Domain-first model | Keeps financial rules independent from Spring and HTTP. |
| Use cases between API and domain | Prevents controllers from owning business orchestration. |
| Spring-free domain layer | Keeps the core model testable without framework infrastructure. |
| In-memory storage | Allows API and domain boundaries to stabilize before persistence. |
| Bean Validation at the API boundary | Rejects malformed input before conversion into domain objects. |
| Minimal CI | Mirrors the current quality contract without adding release or deployment complexity. |

## Roadmap

- [ ] Standardize API error response bodies.
- [ ] Expose money movement history through the API.
- [ ] Add PostgreSQL persistence.
- [ ] Introduce JPA/Hibernate mappings.
- [ ] Add integration tests with a real database.
- [ ] Add idempotency for money movement operations.
- [ ] Explore a formal double-entry ledger model.
- [ ] Add Docker-based local environment.
- [ ] Add OpenAPI documentation.
