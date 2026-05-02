# Wallet (Kotlin) — Clean Architecture + Double-Entry Ledger

Money management REST API for **transaction recording only**.

Core stack: **Kotlin**, **Spring WebFlux**, **Coroutines**, **Arrow-kt**.
Transactional state in **PostgreSQL**; ledger/audit state in **Cassandra**.
Authentication uses **PASETO** modeled via a port + adapter. Redis-ready cache port included.

## Project Rules

- **TDD first**: write tests before implementation; then refactor.
- **Verification**: run `./gradlew test`.
- **Clean architecture**: keep domain logic independent of infrastructure/adapters.
- **Ledger correctness**: enforce double-entry consistency and explicit accounting rules in the domain.

## Architecture

```
┌──────────────────────────────────────────────────────┐
│ API (Presentation): `api/**`                          │
│ Controllers + request/response DTOs                 │
└───────────────────────┬──────────────────────────────┘
                        │ calls use cases
┌───────────────────────▼──────────────────────────────┐
│ Service (Application): `service/**`                  │
│ Use cases (`*UseCase`) + orchestration              │
│ plus **ports** in `service/common/ports/**`         │
└───────────┬──────────────────────┬──────────────────┘
            │ depends only on ports
┌───────────▼──────────┐  ┌────────▼──────────────────┐
│ Domain: `domain/**`    │  │ Infrastructure: `infrastructure/**` │
│ Core entities/value     │  │ Spring configuration + adapters       │
│ objects + ledger types │  │ for ports (security, persistence,     │
└────────────────────────┘  │ cache)                                  │
                             └─────────────────────────────────────────┘
 ```

## Domain Scope

- User identity and authorization claims
- Wallets and accounts
- Double-entry ledger transactions and entries
- Append-only ledger auditability

## Key Patterns

- **Layered ports**: application depends on ports; infrastructure fulfills them
- **Immutable domain**: small value objects and explicit accounting rules
- **Arrow `Either<DomainError, T>`**: explicit error handling; no exceptions in use cases
- **PASETO via port**: token issuance and verification behind `PasetoTokenPort` (+ WebFlux filter)
- **Double-entry ledger**: every transaction records one debit and one credit
- **Recording only**: no direct payment execution or gateway orchestration

## Tech Stack

| Concern    | Library                           |
|------------|-----------------------------------|
| Web        | Spring WebFlux + Coroutines       |
| Database   | PostgreSQL + Cassandra            |
| Cache      | Redis-ready port                  |
| Functional | Arrow-kt (`Either`, `arrow-core`) |
| Auth       | PASETO                            |
| Docs       | SpringDoc OpenAPI 3               |
| Build      | Gradle Kotlin DSL                 |
