# Agent Guidance for `spring-boot-agent-agnostic-guardrails`

Source of truth for AI coding agents. Enforced by Checkstyle, ArchUnit.
## Project Overview

- Spring Boot 4.0.x, Java 25, Maven, Spring Data JDBC, Spring Web, Flyway, Spring Actuator.
- Layered architecture: Controller → Service → Repository
- JDBC / H2 (dev), PostgreSQL (prod)

## Build Commands

```bash
./mvnw verify              # Full build with all quality checks
./mvnw test                # Unit + architecture tests only
./mvnw checkstyle:check    # Checkstyle only
```

## Definition of Done

- Run `./mvnw verify` — it must exit with `BUILD SUCCESS`. Fix every Checkstyle, or ArchUnit violation; do not skip or suppress without documented justification.
- Run `./mvnw test` — all tests must pass, and coverage must meet thresholds.

## Logging
- Use SLF4J only — never `System.out`, `System.err`, or `java.util.logging`.
- Prefer : `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`
- Levels: `ERROR` (unrecoverable), `WARN` (recoverable/fallback), `INFO` (business events), `DEBUG` (dev flow).
- Parameterized only: `log.info("id={}", id)` — never concatenate.
- Never log sensitive data (passwords, tokens, PII).
- Never log-and-rethrow the same exception; log once at the handling layer.
- Never swallow exceptions with empty catch blocks.
- Pass exception as last arg: `log.error("msg", ex)`.
- Use `key=value` pairs for structured, grep-friendly messages.

## Coding Rules

- Constructor injection only — never `@Autowired` on fields. Use `private final` + `@RequiredArgsConstructor`.
- All domain exceptions extend `ApplicationException` (`RuntimeException`). Centralized handling via `@RestControllerAdvice` + `@ExceptionHandler`. Exception classes in `exception` package.
- Javadoc on all public classes, interfaces, methods. Include `@param`, `@return`, `@throws`.
- Return `Optional<T>` instead of `null`. Use `.orElseThrow()`/`.orElse()` — never bare `.get()`. Annotate `@NonNull`/`@Nullable` where needed.
- Refer to `checkstyle.xml` and `checkstyle-suppressions.xml` for style details.
- Never let Checkstyle warnings/errors exist in the codebase — fix them immediately. Do not suppress without documented justification.
- Use Flyway for all schema changes — never manual SQL scripts or direct DB modifications.

## Architecture (ArchUnit)

- Controllers depend only on Services — never Repositories or other Controllers.
- Services depend on Repositories and other Services — never Controllers.
- Repositories depend only on Entity/Model classes — never Services or Controllers.
- No circular dependencies.
- All `@Service` exceptions must extend `ApplicationException`.
- All controller methods must return `ResponseEntity`.

## Security

- No credentials in source — use env vars.
- Deny by default for CORS origins, methods, headers.
- Restrict Actuator to required endpoints: `health`, `info`, `metrics`. never expose sensitive endpoints publicly.

## Performance

- Set explicit timeouts for outbound HTTP/DB calls; no infinite waits.

## Java 25

- Prefer `record` for immutable DTOs/value carriers.
- Use pattern matching and switch expressions where they improve readability.
- Use virtual threads for I/O-bound concurrency only (not CPU-bound).
- Prefer immutable collections for outward-facing responses/config.

## Testing

- Test class mirrors source: `BookServiceTest`, `BookControllerTest`.
- Method name: `givenInput_whenMethodName_thenExpectedBehavior`.
- Use Given/When/Then sections.
- Every new public method in `service`/`controller` must have at least one test.
- Assert on behavior/output, not internal implementation.
- Never use `Thread.sleep` — use `Awaitility` for async.
- No shared mutable state between tests.
- Coverage minimums (JaCoCo): 80% line, 80% branch. Excluded: `model`, `dto`, `exception` packages and main app class.

---
## Controller Layer Instructions

### Rules

- `@RestController` + `@RequestMapping` on every controller class.
- Constructor injection only.
- Never use `@Autowired` on fields or setter methods.
- Inject service interfaces (e.g., `BookService`), never `Impl` classes, never `@Repository`.
- Keep controllers thin — HTTP concerns only, delegate logic to `@Service`.
- Use `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` — never generic `@RequestMapping` for CRUD.
- Paths: REST-compliant, lowercase.
- Always return `ResponseEntity<T>` — never raw objects.
- Status codes: `200` GET/PUT, `201` POST, `204` DELETE, `400` validation, `404` not found.


### Validation

- Annotate request bodies with `@Valid` and use Bean Validation: `@NotBlank`, `@Positive`, `@Email`, etc.
- `@PathVariable` for resource identifiers; `@RequestParam` for query filters.
- Optional params: `@RequestParam(required = false)`.


### Exception Handling

- Controllers do NOT catch exceptions — let `GlobalExceptionHandler` handle them.
- Never expose stack traces or framework internals in API responses.


### Testing

- Use `@WebMvcTest(XController.class)` — loads only web layer, not full context.
- Mock all service dependencies with `@MockBean`.
- Test every HTTP status code the endpoint can return (200, 201, 204, 400, 404).
- Use `MockMvc.perform(...)` + `.andExpect(status().isXxx())` and `.andExpect(jsonPath(...))`.
- Never use `@SpringBootTest` for controller unit tests.

---
## Service Layer Instructions

### Rules

- `@Service` on every implementation class.
- Define a Java class per service.

### Transaction Management

- `@Transactional` on write methods (create, update, delete).
- `@Transactional(readOnly = true)` on read methods — set at class level if most methods are reads.

### Exception Handling

- Throw domain exceptions extending `ApplicationException` — never raw `RuntimeException`.
- One exception class per error scenario (e.g., `BookNotFoundException`), in the `exception` package.
- Include meaningful messages with relevant data (IDs, names).

### Dependencies

- May depend on: `@Repository` beans and other `@Service` interfaces.
- Must NOT depend on: `@RestController`, `@Controller`, or any servlet/HTTP classes.
- Avoid circular dependencies between services.


### Business Logic

- All business logic lives here — controllers validate syntax, services validate business rules.
- Use `Optional<T>` for nullable results; prefer `.orElseThrow()` with domain exceptions.


### Testing

- Use Integration tests for the services — `@SpringBootTest` with an in-memory database (H2) to test real interactions with repositories and transactions.
- Test every branch: happy path, `Optional.empty()`, and every thrown domain exception.

---
## Repository Layer Instructions

### Rules

- All repositories must be Spring Data interfaces — never concrete classes.
- Extend `ListCrudRepository<Entity, ID>`. Annotate with `@Repository`.
- Naming: `EntityName + Repository` (e.g., `BookRepository`).

### Query Methods

- Prefer derived query methods (`findBy`, `existsBy`, `countBy`).
- For complex queries, use `@Query` with JPQL and `@Param` for parameter binding.
- Always parameterize queries — never concatenate user input (SQL injection risk).
- Avoid native SQL unless necessary. If used, set `nativeQuery = true` and add a comment explaining why.

### Return Types

- `Optional<T>` for single results that may not exist.
- `List<T>` for multiple results (empty list if none).
- `boolean` for existence checks (`existsBy*`).
- `long` for count queries (`countBy*`).
- `Page<T>` for paginated results (use `Pageable` parameter).
- Never return `null`.

### No Business Logic

- Repositories handle data access only — no calculations, validations, or transformations.
- Complex queries are fine; complex logic belongs in `@Service`.

### Performance

- Avoid N+1 queries.
- For read-only list views, return DTO projections instead of full entities.
- Use `saveAll` for bulk writes; never call `save` in a loop.

### Relationships & Transactions

- Do not manage transactions in repositories — that's the service layer's job.

### Testing

- Use `@Data# Repository Layer — AGENTS.md

Rules for `repository` package. Extends root `AGENTS.md`.

## Rules

- All repositories must be Spring Data interfaces — never concrete classes.
- Extend `JpaRepository<Entity, ID>`. Annotate with `@Repository`.
- Naming: `EntityName + Repository` (e.g., `BookRepository`).

## Query Methods

- Prefer derived query methods (`findBy`, `existsBy`, `countBy`).
- For complex queries, use `@Query` with JPQL and `@Param` for parameter binding.
- Always parameterize queries — never concatenate user input (SQL injection risk).
- Avoid native SQL unless necessary. If used, set `nativeQuery = true` and add a comment explaining why.

## Return Types

- `Optional<T>` for single results that may not exist.
- `List<T>` for multiple results (empty list if none).
- `boolean` for existence checks (`existsBy*`).
- `long` for count queries (`countBy*`).
- `Page<T>` for paginated results (use `Pageable` parameter).
- Never return `null`.

## No Business Logic

- Repositories handle data access only — no calculations, validations, or transformations.
- Complex queries are fine; complex logic belongs in `@Service`.

## Performance

- Avoid N+1 queries: use fetch joins, `@EntityGraph`, or JPQL projections for known access paths.
- For read-only list views, return DTO projections instead of full entities.
- Use `saveAll` for bulk writes; never call `save` in a loop.

## Relationships & Transactions

- Let JPA/Hibernate handle lazy loading and proxies.
- Do not manage transactions in repositories — that's the service layer's job.

## Testing

- Use `@DataJdbcTest` — loads only Jdbc slice (H2 in-memory), no full context.
- Test every custom `@Query` method; do not test Spring Data derived methods.
- Verify `Optional.empty()` is returned correctly for non-existent records.
- Use `@BeforeEach` to seed data — never rely on pre-existing state.JpaTest` — loads only JPA slice (H2 in-memory), no full context.
- Test every custom `@Query` method; do not test Spring Data derived methods.
- Verify `Optional.empty()` is returned correctly for non-existent records.