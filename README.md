# LedgerPay

> Java backend sandbox for digital wallets, money movement, and financial domain modeling.

## Current Status

LedgerPay has completed its technical foundation and now includes its first in-memory financial flow: funding a wallet from an explicit `SystemTreasury`.

The project currently focuses on domain modeling, monetary correctness, unit testing, and incremental architecture. It does not yet include persistence, REST APIs, authentication, idempotency, or a formal ledger.

## Documentation

- [Initial project definition](docs/00-project-definition.md)

## Current Stack

- Java 21
- Maven
- Maven Wrapper
- JUnit 5
- AssertJ
- Spotless
* `google-java-format`

## Development with GitHub Codespaces

The repository includes a dev container configuration with Java 21 to provide a reproducible development environment in GitHub Codespaces.

## Running Tests

### Linux/macOS

```bash
./mvnw test
```

### Windows

```powershell
.\mvnw.cmd test
```

## Development Shortcuts

The commands below are optional shortcuts for environments with `make` available. The Maven Wrapper remains the canonical way to run the project build and tests.

| Command             | Description                                  |
| ------------------- | -------------------------------------------- |
| `make help`         | Lists available shortcuts.                   |
| `make test`         | Runs the test suite.                         |
| `make verify`       | Runs full validation before review or merge. |
| `make clean`        | Removes Maven build artifacts.               |
| `make format`       | Applies Java formatting.                     |
| `make format-check` | Checks Java formatting.                      |

## Automated Quality

The project uses Spotless with `google-java-format` to keep Java formatting consistent. Formatting checks are executed during `make verify`.

## Implemented Domain

- [x] `Money` Value Object
- [x] `Wallet` with protected in-memory balance operations
- [x] `SystemTreasury` with fixed initial funds
- [x] Treasury funding flow in memory
- [ ] Peer transfer between wallets
- [ ] Persistence with Spring Boot and PostgreSQL
- [ ] REST API sandbox
- [ ] Future evolution toward a formal ledger

## Current Limitations

At the current stage, LedgerPay does not include:

- REST API
- persistence
- authentication or authorization
- idempotency
- transaction boundaries
- formal ledger or double-entry accounting

The current model uses direct in-memory balances to support incremental domain learning before evolving toward more robust financial architecture.
