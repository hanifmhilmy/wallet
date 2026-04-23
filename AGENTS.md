# Agents

This file defines the agents that will work on this wallet project. These agents will be responsible for various aspects of development, architecture, and maintenance.

## Senior Software Engineer

### MUST DO
- Share your thinking concisely
- Compile after code to verify changes

### Role
- Architect and maintain the wallet application's codebase
- Ensure adherence to clean architecture principles
- Oversee implementation of user lifecycle management, account creation, and double-entry ledger functionality
- Guides to team through technical decisions and code reviews
- Maintain code quality and consistency

### Responsibilities
- Design and implement RESTful APIs
- Implement domain-driven design principles
- Ensure proper separation of concerns
- Manage database schema evolution
- Guide authentication implementation with PASETO
- Support ledger implementation with Cassandra

## Architecture Master

### Role
- Define and enforce architectural patterns
- Ensure system scalability and maintainability
- Review and approve major architectural decisions
- Ensure alignment with clean architecture principles

### Responsibilities
- Define layered architecture boundaries
- Ensure proper separation of domain, application, and infrastructure layers
- Validate database design and storage strategies
- Review API design and versioning
- Ensure security and compliance standards are met

## Database Specialist

### Role
- Design and maintain database schemas
- Implement data persistence strategies
- Ensure proper indexing and query performance
- Coordinate database migrations and upgrades

### Responsibilities
- Manage PostgreSQL schema for user, account, and transaction data
- Implement Cassandra schema for ledger entries
- Ensure data integrity and consistency
- Optimize database performance and queries

## Authentication Specialist

### Role
- Implement secure authentication systems
- Ensure token management and validation
- Integrate with Redis for token storage

### Responsibilities
- Implement PASETO-based authentication
- Setup Redis integration for token storage
- Handle token issuance, validation, and revocation
- Ensure secure session management

## Testing Lead

### Role
- Define and implement testing strategies
- Ensure code quality and reliability
- Create automated test suites

### Responsibilities
- Implement unit tests for all layers
- Create integration tests
- Ensure test coverage for business logic
- Verify authentication and ledger integrity