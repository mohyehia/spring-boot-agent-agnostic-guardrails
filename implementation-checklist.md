# Implementation Checklist

Day-to-day development checklist derived from `AGENTS.md` for `spring-boot-agent-agnostic-guardrails`.

## Before Writing Code
- [ ] Confirm the feature fits the layered architecture: `controller -> service -> repository`.
- [ ] Keep package placement correct:
  - [ ] Controllers in `..controller..`
  - [ ] Services in `..service..`
  - [ ] Repositories in `..repository..`
  - [ ] Exceptions in `..exception..`
  - [ ] Configurations in `..config..`
- [ ] Use Spring Data JDBC conventions unless the project is explicitly changed.
- [ ] Plan schema changes through Flyway migrations only.
- [ ] Avoid introducing credentials or secrets into source files.

## Naming and Structure
- [ ] Controller class names end with `Controller`.
- [ ] Service class names end with `Service`.
- [ ] Repository interface names end with `Repository`.
- [ ] Configuration class names end with `Config`.
- [ ] Public classes, interfaces, and methods include Javadoc.
- [ ] Use constructor injection only.
- [ ] Do not use field injection.

## Controller Checklist
- [ ] Annotate each controller with `@RestController` and `@RequestMapping`.
- [ ] Inject service interfaces only.
- [ ] Keep controller logic limited to HTTP concerns.
- [ ] Use specific mapping annotations for CRUD:
  - [ ] `@GetMapping`
  - [ ] `@PostMapping`
  - [ ] `@PutMapping`
  - [ ] `@DeleteMapping`
- [ ] Return `ResponseEntity` from every controller method.
- [ ] Use lowercase REST-style paths.
- [ ] Validate request bodies with `@Valid`.
- [ ] Use `@PathVariable` for identifiers.
- [ ] Use `@RequestParam(required = false)` for optional query parameters.
- [ ] Do not catch domain exceptions in controllers.
- [ ] For `@GetMapping` methods, ensure SLF4J logging is present to satisfy current architecture rules.

## Service Checklist
- [ ] Annotate implementation classes with `@Service`.
- [ ] Put business logic in the service layer, not in controllers or repositories.
- [ ] Use `@Transactional(readOnly = true)` for reads.
- [ ] Use `@Transactional` for writes.
- [ ] Depend only on repositories and other services.
- [ ] Do not depend on controllers, servlet APIs, or HTTP classes.
- [ ] Throw domain-specific exceptions only.
- [ ] Make all service exceptions extend `ApplicationException`.

## Repository Checklist
- [ ] Repositories are Spring Data interfaces, not concrete classes.
- [ ] Prefer extending `ListCrudRepository<Entity, ID>` for the current Spring Data JDBC setup.
- [ ] Use derived query methods where possible.
- [ ] Return `Optional<T>` for single nullable results.
- [ ] Return `List<T>` for multi-result queries.
- [ ] Return `boolean` for existence checks.
- [ ] Return `long` for count queries.
- [ ] Never return `null`.
- [ ] Do not place business logic in repositories.
- [ ] Do not manage transactions in repositories.
- [ ] Parameterize custom queries; never concatenate user input.

## DTO and Model Checklist
- [ ] Prefer `record` for immutable DTOs/value carriers.
- [ ] Prefer immutable outward-facing response structures.
- [ ] Use `Optional<T>` instead of `null` where applicable.
- [ ] Never call bare `Optional#get()`.
- [ ] Use `.orElseThrow()` or `.orElse()` as appropriate.

## Exception and Error Handling Checklist
- [ ] Create one exception class per business error scenario.
- [ ] Place exception classes in the `exception` package.
- [ ] Make domain exceptions extend `ApplicationException`.
- [ ] Handle API errors centrally with `@RestControllerAdvice`.
- [ ] Do not expose stack traces or framework internals in API responses.

## Logging Checklist
- [ ] Use SLF4J only.
- [ ] Declare loggers as `private static final Logger log = LoggerFactory.getLogger(ClassName.class);`
- [ ] Use parameterized messages only, such as `log.info("id={}", id)`.
- [ ] Do not use string concatenation in log messages.
- [ ] Do not log passwords, tokens, secrets, or PII.
- [ ] Do not log and rethrow the same exception.
- [ ] Pass exceptions as the last logger argument.
- [ ] Use `key=value` style when practical.
- [ ] Do not use `System.out`, `System.err`, or `java.util.logging`.

## Security and Operations Checklist
- [ ] Restrict Actuator exposure to required endpoints only.
- [ ] Deny CORS by default unless explicitly configured.
- [ ] Set explicit timeouts for outbound calls.
- [ ] Use environment variables for secrets and environment-specific values.

## Testing Checklist
- [ ] Add at least one test for every new public controller method.
- [ ] Add at least one test for every new public service method.
- [ ] Use `@WebMvcTest` for controller tests.
- [ ] Mock service dependencies in controller tests.
- [ ] Test controller status codes: `200`, `201`, `204`, `400`, `404` where relevant.
- [ ] Use `MockMvc` with response body assertions.
- [ ] Use `@SpringBootTest` with H2 for service integration tests.
- [ ] Test happy paths and failure branches.
- [ ] Do not use `Thread.sleep` in tests.
- [ ] Keep test names in `givenInput_whenMethodName_thenExpectedBehavior` format.
- [ ] Keep test classes aligned with source class naming.

## Done Checklist
- [ ] Run tests successfully.
- [ ] Run full verification successfully.
- [ ] Fix all Checkstyle violations.
- [ ] Fix all ArchUnit violations.
- [ ] Confirm no layer dependency violations were introduced.
- [ ] Confirm public APIs are documented.

## Windows Commands
```powershell
.\mvnw.cmd verify
.\mvnw.cmd test
.\mvnw.cmd checkstyle:check
```

## Notes
- `AGENTS.md` contains a duplicated repository section with conflicting references to `ListCrudRepository` and `JpaRepository`.
- Because the project dependencies currently indicate Spring Data JDBC, this checklist assumes Spring Data JDBC patterns unless the project is intentionally migrated.

