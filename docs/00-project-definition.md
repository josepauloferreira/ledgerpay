# LedgerPay — Initial Project Definition

## Project Summary

LedgerPay is a Java backend sandbox for modeling digital wallets, money movement, and financial domain rules.

## Main Objective

Build a backend system that demonstrates:
- monetary correctness
- domain modeling
- automated testing
- safe financial operations
- clear technical trade-offs
- future readiness for persistence, APIs, idempotency, and ledger-based accounting

## Current Stage

The project has completed its technical foundation and its first in-memory financial flow.

Currently implemented:
- `Money` as a monetary Value Object
- `Wallet` as a mutable in-memory balance holder
- `SystemTreasury` as an explicit institutional funding source
- `TreasuryFunding` as the first money movement operation

## Current Domain Model

### `Money`

`Money` represents monetary amounts.

Current responsibilities:
- wrap `BigDecimal`
- normalize values to two decimal places
- reject real fractions of cents
- reject `null`
- allow zero and negative monetary values as representable amounts
- support addition and subtraction
- expose positivity and comparison operations needed by domain flows

`Money` does not decide whether a financial operation is valid. Operational rules belong to the domain behavior that performs the operation.

### `Wallet`

`Wallet` represents a common wallet with a mutable in-memory balance.

Current responsibilities:
- start with zero balance
- reject `null` initial balance
- reject negative initial balance
- allow positive credits
- reject zero or negative credits
- allow positive debits when balance is sufficient
- reject zero or negative debits
- reject debits above available balance
- allow debiting the exact available balance

`Wallet` is currently modeled with direct balance mutation. This is intentional for the current stage and may evolve when ledger-based accounting is introduced.

### `SystemTreasury`

`SystemTreasury` represents the system’s institutional funding source.

Current responsibilities:
- start with a fixed fictitious balance of `1.000.000.00`
- expose its current balance
- allow protected debit through its internal funds
- act as the only valid source type for treasury funding

`SystemTreasury` is implemented using composition rather than inheritance from `Wallet`.

### `TreasuryFunding`

`TreasuryFunding` represents the operation of funding a common wallet from the system treasury.

Current responsibilities:
- require `SystemTreasury` as the funding source
- require a destination `Wallet`
- require a positive `Money` amount
- reject zero and negative funding amounts
- reject funding above treasury funds
- debit the treasury and credit the destination wallet

## Current Technical Foundation

The project currently uses:
- Java 21
- Maven
- JUnit 5
- AssertJ
- Spotless
- `google-java-format`
- GitHub Codespaces with Java 21
- optional Makefile shortcuts for local development

## Validation Strategy

The project uses unit tests to drive domain behavior.

Current validation includes:
- monetary invariants in `Money`
- wallet balance rules in `Wallet`
- treasury initialization in `SystemTreasury`
- valid and invalid funding flows in `TreasuryFunding`
- formatting validation through Spotless
- full local validation through `make verify`

## Current Scope

The current scope includes:
- in-memory domain modeling
- monetary correctness for basic operations
- wallet balance protection
- treasury-based funding
- automated tests for implemented behavior

## Out of Scope for the Current Stage

- REST API
- Spring Boot application layer
- PostgreSQL persistence
- authentication
- authorization
- idempotency
- distributed transactions
- formal ledger
- double-entry accounting
- users and wallet ownership
- production-grade treasury lifecycle management

## Known Limitations

- `Wallet.credit(...)` and `Wallet.debit(...)` are still publicly callable
- `SystemTreasury.debit(...)` is still publicly callable
- Multiple `SystemTreasury` instances can be created in memory
- `Wallet(Money balance)` remains public while creation versus rehydration has not yet been formally modeled
- Balances are stored directly instead of being derived from ledger entries
- Domain errors still use generic Java exceptions

These limitations are intentional at this stage and will be revisited when the project introduces additional flows, application services, persistence, or ledger modeling.

## Immediate Roadmap

### Peer Transfer Domain

Expected concerns:
- transferring a positive amount from one wallet to another
- rejecting zero and negative transfer amounts
- rejecting insufficient source balance
- preventing transfers from a wallet to itself
- preserving balances when a transfer is rejected
- deciding whether wallet identity is needed now or later
- observing the limits of direct balance mutation before introducing a ledger

### Future Stages

Later stages may include:
- wallet identity
- users and ownership
- application services
- domain-specific exceptions
- persistence with PostgreSQL
- REST API
- idempotent operations
- ledger entries
- double-entry accounting
