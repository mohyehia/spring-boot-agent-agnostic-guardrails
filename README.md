# spring-boot-agent-agnostic-guardrails

Spring Boot sample application demonstrating guardrail-compliant delivery of a
`Category` REST resource.

## Implemented Resource

The application exposes CRUD endpoints for `Category`:

- `POST /categories`
- `GET /categories/{id}`
- `GET /categories`
- `PUT /categories/{id}`
- `DELETE /categories/{id}`

## Technology Choices

- Spring Boot 4
- Spring Web MVC
- Spring Data JDBC
- Flyway for schema migrations
- H2 in-memory database for development and testing

## Local Validation Commands

On Windows:

```powershell
.\mvnw.cmd test
.\mvnw.cmd verify
.\mvnw.cmd checkstyle:check
```

The project reads datasource settings from environment variables when provided
and falls back to an in-memory H2 datasource for local development.
