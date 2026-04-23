# Wallet — Kotlin Clean Architecture + Double-Entry Ledger

Money management REST API for transaction recording only — **Kotlin**, **Spring WebFlux**, **Coroutines**, **Arrow-kt**, PostgreSQL for transactional state, Cassandra for ledger state. PASETO authentication is modeled as a port plus adapter. Redis-ready cache port included.

---
## Architecture Layers

```
┌──────────────────────────────────────────────────────┐
│  API (Presentation)                                   │
│  controllers · requests · responses                   │
│  middlewares · swagger                                │
└───────────────────────┬──────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────┐
│  Application                                          │
│  use cases · ports · orchestration                    │
│  auth · ledger · transaction recording               │
└───────────┬──────────────────────┬───────────────────┘
            │                      │
┌───────────▼──────────┐  ┌────────▼──────────────────┐
│  Domain               │  │  Infrastructure            │
│  value objects        │  │  persistence/postgres      │
│  accounting rules     │  │  persistence/cassandra     │
│  errors (zero dep)    │  │  security/config           │
└───────────────────────┘  │  cache/clients             │
                           └────────────────────────────┘
```

## Domain Scope

- User identity and authorization claims
- Wallets and accounts
- Double-entry ledger transactions and entries
- Append-only ledger auditability

## Key Patterns

- **Layered ports** — application depends on ports, infrastructure fulfills them
- **Immutable domain** — small value objects and explicit accounting rules
- **Arrow `Either<DomainError, T>`** — explicit error handling, no exceptions in use cases
- **PASETO auth** — token issuance and verification live behind `PasetoTokenPort`
- **Double-entry ledger** — every transaction records one debit and one credit
- **Recording only** — no direct payment execution, no payment gateway orchestration

## Tech Stack

| Concern    | Library                              |
|------------|--------------------------------------|
| Web        | Spring WebFlux + Coroutines          |
| Database   | PostgreSQL + Cassandra               |
| Cache      | Redis-ready port                     |
| Functional | Arrow-kt (`Either`, `arrow-core`)    |
| Auth       | PASETO                               |
| Docs       | SpringDoc OpenAPI 3                  |
| Build      | Gradle Kotlin DSL                    |