# LedgerPay

LedgerPay is a backend API for exploring consistency, transactions and failure handling in financial operations.

The project focuses on a simple question:

> How can a system preserve financial invariants when operations fail, are retried or happen concurrently?

It is intentionally limited in scope and is not intended to simulate a complete banking platform.

## Problem

Financial operations must remain consistent even when unexpected situations occur.

For example:

- an operation fails after only part of its data has been persisted;
- the same request is sent more than once;
- multiple operations try to spend the same balance concurrently.

A successful HTTP response is not enough. The underlying financial state must remain valid.

LedgerPay is built around investigating and protecting these properties.

## Goal

Build a small transactional API that supports accounts and financial transfers while preserving clearly defined domain invariants.

The project will evolve incrementally.

More complex mechanisms will only be introduced after the problem they solve can be demonstrated or reproduced.

## Core invariants

- Financial amounts must be greater than zero.
- A regular account cannot spend more than its available balance.
- Every completed ledger transaction must have equal total debits and credits.
- A financial operation must either be persisted completely or not persisted at all.
- Confirmed ledger entries are immutable.

## Initial scope

The first version will support:

- account creation and retrieval;
- account funding;
- transfers between accounts;
- balance queries;
- paginated account statements.

Later iterations will investigate retries, idempotency and concurrent operations.

## Out of scope

LedgerPay does not attempt to implement:

- real money transfers;
- PIX or integrations with financial institutions;
- multiple currencies;
- interest or fees;
- KYC;
- fraud detection;
- full banking infrastructure;
- distributed microservices.

These limitations are intentional so the project can remain focused on transactional consistency and backend engineering.

## Tech stack

Currently:

- Java 21
- Spring Boot
- Maven

The stack will evolve as new requirements are implemented.

## Status

🚧 Under development — project bootstrap and domain definition.
