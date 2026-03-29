# Wallet — Kotlin Clean Architecture + CQRS

Money management REST API — **Kotlin**, **Spring WebFlux**, **Coroutines**, **Arrow-kt**, single **PostgreSQL** database via R2DBC. Redis-ready cache port included.

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
│  CommandService · QueryService                        │
│  AppError · CachePort (interface)                     │
└───────────┬──────────────────────┬───────────────────┘
            │                      │
┌───────────▼──────────┐  ┌────────▼──────────────────┐
│  Domain               │  │  Infrastructure            │
│  models · valueobjects│  │  persistence/ (R2DBC PG)   │
│  events · errors      │  │  config/                   │
│  (zero dependencies)  │  │  NoOpCacheAdapter (stub)   │
└───────────────────────┘  │  clients/                  │
                           └────────────────────────────┘
```

## Domain Aggregates


## Key Patterns

- **CQRS** — Commands mutate state; queries read (add Redis caching via `CachePort` when ready)
- **Rich domain models** — private setters, `Either`-returning business methods, always-valid state
- **Arrow `Either<AppError, T>`** — explicit error handling, no exceptions in business logic
- **CachePort** — interface in `application/common/ports/cache`; currently wired to `NoOpCacheAdapter`; swap in `RedisCacheAdapter` without touching any other layer

## Tech Stack

| Concern    | Library                              |
|------------|--------------------------------------|
| Web        | Spring WebFlux + Coroutines          |
| Database   | R2DBC + PostgreSQL                   |
| Cache      | NoOp stub → Redis (when ready)       |
| Functional | Arrow-kt (`Either`, `arrow-core`)    |
| Docs       | SpringDoc OpenAPI 3                  |
| Build      | Gradle Kotlin DSL                    |