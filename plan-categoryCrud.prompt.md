# Detailed Development Plan: Category CRUD Resource

## Goal
Build a complete `Category` REST resource for the Spring Boot application with full CRUD support, Flyway-managed schema migration, H2-backed development and test environments, and comprehensive tests across controller, service, and repository layers.

## Scope Summary
- Add a new `Category` resource.
- Implement Create, Read by id, Read all, Update, and Delete operations.
- Use a layered architecture: `controller -> service -> repository`.
- Use Spring Data JDBC.
- Use Flyway for database schema management.
- Use H2 in-memory database for development and testing.
- Add unit/integration tests with strong edge-case coverage.
- Keep the implementation compliant with `AGENTS.md`, Checkstyle, and ArchUnit rules.

---

## Phase 1: Baseline Assessment and Alignment

### Task 1.1 - Review current project structure
**Description:**
Inspect the existing source tree, dependencies, configuration files, seed scripts, and tests to understand the current baseline.

**Subtasks:**
- Review `pom.xml` dependencies and plugins.
- Review `src/main/resources/application.yaml`.
- Review current SQL initialization files such as `schema.sql` and `data.sql`.
- Review existing ArchUnit and Spring Boot test classes.
- Identify any placeholders or conflicting product-oriented sample artifacts.

**Output:**
- Clear inventory of what already exists.
- List of files that must be replaced, removed, or extended.

### Task 1.2 - Reconcile project rules with current codebase
**Description:**
Align the planned implementation with the development rules from `AGENTS.md`.

**Subtasks:**
- Confirm layered package structure.
- Confirm repository approach based on Spring Data JDBC.
- Confirm that Flyway will be the only schema migration mechanism used going forward.
- Note any inconsistencies in the current codebase that must be cleaned up before or during implementation.

**Output:**
- Implementation constraints list.
- Decision to use Spring Data JDBC repository patterns and Flyway migrations.

---

## Phase 2: Design the Category Resource

### Task 2.1 - Define the domain model
**Description:**
Design the `Category` entity structure according to the application description.

**Subtasks:**
- Define the persistence model fields:
  - `id: Long`
  - `name: String`
  - `description: String`
- Decide column constraints such as `NOT NULL` and length limits.
- Ensure the model structure fits Spring Data JDBC conventions.

**Output:**
- Final `Category` data model design.

### Task 2.2 - Define API contracts
**Description:**
Design request and response DTOs for the REST API.

**Subtasks:**
- Create a request DTO for create operations.
- Create a request DTO for update operations.
- Create a response DTO for returning category data.
- Decide validation constraints such as `@NotBlank` for textual fields.
- Use immutable `record` types for DTOs where appropriate.

**Output:**
- Request and response contract definitions.

### Task 2.3 - Define endpoint behavior
**Description:**
Plan the REST endpoints and their behavior.

**Subtasks:**
- Define `POST /categories`.
- Define `GET /categories/{id}`.
- Define `GET /categories`.
- Define `PUT /categories/{id}`.
- Define `DELETE /categories/{id}`.
- Map each endpoint to expected status codes.
- Define validation failure and not-found behavior.

**Output:**
- Endpoint matrix with request/response expectations.

---

## Phase 3: Database and Migration Plan

### Task 3.1 - Replace ad hoc schema initialization with Flyway
**Description:**
Move schema management to Flyway in line with project rules.

**Subtasks:**
- Create Flyway migration directory structure if missing.
- Add an initial migration for the `categories` table.
- Decide whether to retire `schema.sql` and `data.sql` or adapt them temporarily.
- Ensure Flyway is the source of truth for schema creation.

**Output:**
- Versioned Flyway migration for `categories`.

### Task 3.2 - Design the `categories` table
**Description:**
Create the database design for the new resource.

**Subtasks:**
- Add `id` as auto-generated primary key.
- Add `name` column.
- Add `description` column.
- Determine whether `name` should be unique.
- Decide whether to seed initial data for tests or keep test data isolated per test class.

**Output:**
- Final schema definition for `categories`.

### Task 3.3 - Validate runtime configuration
**Description:**
Ensure development and test environments both work with H2 and Flyway.

**Subtasks:**
- Verify `application.yaml` settings.
- Add or adjust test-specific configuration if needed.
- Ensure migrations run automatically during tests.

**Output:**
- Working H2 + Flyway setup for both main and test execution.

---

## Phase 4: Package and Class Skeleton Creation

### Task 4.1 - Create package structure
**Description:**
Add the required package layout to support layered architecture and ArchUnit rules.

**Subtasks:**
- Create `model` package.
- Create `dto` package.
- Create `repository` package.
- Create `service` package.
- Create `controller` package.
- Create `exception` package.
- Create `config` package only if required by implementation.

**Output:**
- Rule-compliant package structure.

### Task 4.2 - Create class and interface skeletons
**Description:**
Prepare the classes/interfaces needed for implementation.

**Subtasks:**
- Create `Category` model.
- Create `CategoryRepository`.
- Create service abstraction if needed.
- Create `CategoryService` implementation or interface + implementation pattern depending on project style.
- Create `CategoryController`.
- Create exception classes.
- Create `GlobalExceptionHandler` if not already present.
- Create DTO records.

**Output:**
- Compilable project skeleton for the Category resource.

---

## Phase 5: Repository Layer Implementation

### Task 5.1 - Implement `CategoryRepository`
**Description:**
Create the Spring Data JDBC repository responsible for category persistence.

**Subtasks:**
- Extend the correct Spring Data repository type.
- Annotate with `@Repository`.
- Provide standard CRUD access.
- Add derived query methods only if needed.

**Output:**
- Working repository interface for persistence.

### Task 5.2 - Verify repository constraints
**Description:**
Ensure repository design stays compliant with architectural rules.

**Subtasks:**
- Confirm no business logic exists in the repository.
- Confirm repository depends only on model/entity classes.
- Confirm null-safe return types.

**Output:**
- Repository aligned with ArchUnit and AGENTS rules.

---

## Phase 6: Service Layer Implementation

### Task 6.1 - Design service responsibilities
**Description:**
Define the service contract and business behavior for Category CRUD operations.

**Subtasks:**
- Define method for creating a category.
- Define method for finding a category by id.
- Define method for listing all categories.
- Define method for updating a category.
- Define method for deleting a category.
- Define validation/business rules, such as existence checks.

**Output:**
- Clear service method contract.

### Task 6.2 - Implement service logic
**Description:**
Add the business logic for each Category operation.

**Subtasks:**
- Map request DTOs to the persistence model.
- Save new categories.
- Fetch existing categories or throw domain exceptions.
- Update existing category fields safely.
- Delete existing categories after existence validation.
- Return response DTOs or model-to-DTO mappings as appropriate.

**Output:**
- Functional service layer.

### Task 6.3 - Add transaction boundaries
**Description:**
Ensure service methods use correct transaction semantics.

**Subtasks:**
- Mark read methods with `@Transactional(readOnly = true)`.
- Mark create/update/delete methods with `@Transactional`.

**Output:**
- Transactionally correct service methods.

### Task 6.4 - Add domain exceptions
**Description:**
Create exception types for business error cases.

**Subtasks:**
- Create base `ApplicationException` if missing.
- Create `CategoryNotFoundException`.
- Add other category-specific exceptions if needed, such as duplicate-name exceptions.
- Use meaningful error messages containing relevant identifiers.

**Output:**
- Domain-specific exception hierarchy.

---

## Phase 7: Controller Layer Implementation

### Task 7.1 - Implement REST controller
**Description:**
Create the HTTP entry points for the Category resource.

**Subtasks:**
- Annotate controller with `@RestController` and `@RequestMapping("/categories")`.
- Inject service dependency through constructor injection only.
- Implement CRUD endpoints using the specific mapping annotations.
- Return `ResponseEntity` from every endpoint.

**Output:**
- Working `CategoryController`.

### Task 7.2 - Add request validation
**Description:**
Validate incoming payloads at the controller boundary.

**Subtasks:**
- Annotate request bodies with `@Valid`.
- Apply Bean Validation annotations to request DTOs.
- Ensure invalid requests return `400 Bad Request`.

**Output:**
- Safe and validated input handling.

### Task 7.3 - Add request logging
**Description:**
Add SLF4J logging that complies with the project rules and current architecture tests.

**Subtasks:**
- Add `private static final Logger log` field.
- Add parameterized log statements.
- Ensure `@GetMapping` methods log requests to satisfy the existing logging rule.
- Avoid logging sensitive or unnecessary data.

**Output:**
- Controller logging compliant with current conventions.

---

## Phase 8: Global Error Handling

### Task 8.1 - Implement centralized exception handling
**Description:**
Provide consistent API error responses through a global handler.

**Subtasks:**
- Create or extend `@RestControllerAdvice`.
- Handle `CategoryNotFoundException` with `404`.
- Handle validation exceptions with `400`.
- Handle other application exceptions in a structured way.
- Define a stable error response DTO if needed.

**Output:**
- Consistent and safe API error responses.

### Task 8.2 - Standardize error payload format
**Description:**
Define how clients receive error details.

**Subtasks:**
- Decide on fields such as timestamp, status, error, message, and path.
- Ensure implementation avoids leaking internal exceptions or stack traces.

**Output:**
- Predictable error response format.

---

## Phase 9: Repository Test Plan

### Task 9.1 - Add repository integration tests
**Description:**
Verify the repository behavior against an H2-backed database.

**Subtasks:**
- Use `@DataJdbcTest` where appropriate.
- Validate save and load behavior.
- Validate empty results for non-existent records.
- Validate update persistence behavior.
- Validate delete behavior.

**Edge Cases to Cover:**
- Lookup by unknown id.
- Persisting records with required fields.
- Behavior with an empty table.

**Output:**
- Reliable repository-level integration coverage.

---

## Phase 10: Service Test Plan

### Task 10.1 - Add service integration tests
**Description:**
Verify the service layer with the real Spring context, H2 database, and Flyway migrations.

**Subtasks:**
- Use `@SpringBootTest`.
- Test create success flow.
- Test get-by-id success flow.
- Test list-all success flow.
- Test update success flow.
- Test delete success flow.
- Test not-found branches.
- Test any business validation branches.

**Edge Cases to Cover:**
- Updating a non-existent category.
- Deleting a non-existent category.
- Reading from an empty dataset.
- Persisting boundary-valid text values if length constraints are introduced.

**Output:**
- Service behavior proven end-to-end through the persistence layer.

---

## Phase 11: Controller Test Plan

### Task 11.1 - Add controller web-layer tests
**Description:**
Verify API contract behavior using web-slice tests.

**Subtasks:**
- Use `@WebMvcTest(CategoryController.class)`.
- Mock the service dependency.
- Test all success status codes and bodies.
- Test validation failures.
- Test not-found behavior.
- Test delete `204 No Content` behavior.

**Edge Cases to Cover:**
- Invalid JSON body.
- Missing required fields.
- Blank `name` or `description`.
- Not-found id path.
- Empty list response.

**Output:**
- API contract coverage independent of service implementation.

---

## Phase 12: Architecture and Quality Compliance

### Task 12.1 - Ensure naming and package compliance
**Description:**
Make sure all new code satisfies the existing ArchUnit rules.

**Subtasks:**
- Ensure controller class ends with `Controller`.
- Ensure service class ends with `Service`.
- Ensure repository interface ends with `Repository`.
- Place classes in the correct packages.
- Ensure controller methods are non-static and non-final.

**Output:**
- ArchUnit-compatible naming and packaging.

### Task 12.2 - Ensure coding standards compliance
**Description:**
Make sure the code conforms to project coding rules.

**Subtasks:**
- Add Javadoc to all public classes and methods.
- Avoid generic exceptions.
- Avoid field injection.
- Ensure no use of standard output streams.
- Ensure log statements use SLF4J only.
- Ensure `Optional` usage is safe and explicit.

**Output:**
- Checkstyle- and ArchUnit-ready codebase.

---

## Phase 13: Cleanup and Supporting Files

### Task 13.1 - Update local request examples
**Description:**
Provide local HTTP examples for manual verification.

**Subtasks:**
- Update `requests.http` with Category CRUD examples.
- Remove or replace outdated product-specific requests.

**Output:**
- Ready-to-run local API request examples.

### Task 13.2 - Update project documentation
**Description:**
Document the new feature and how it is exercised.

**Subtasks:**
- Update `README.md` with Category resource summary.
- Mention main endpoints.
- Mention Flyway and H2 usage if helpful.

**Output:**
- Repository documentation reflecting the new Category API.

---

## Phase 14: Final Validation

### Task 14.1 - Run targeted checks during implementation
**Description:**
Continuously validate correctness while building the feature.

**Subtasks:**
- Compile after major milestones.
- Run focused tests after each layer is implemented.
- Fix issues immediately before proceeding.

**Output:**
- Reduced integration risk.

### Task 14.2 - Run full project verification
**Description:**
Perform the final quality gate checks required by the project guidance.

**Subtasks:**
- Run test suite.
- Run full verify lifecycle.
- Fix failing tests, Checkstyle violations, and ArchUnit violations.
- Re-run until stable.

**Output:**
- Feature complete and quality-compliant build.

---

## Suggested Implementation Order
1. Baseline assessment
2. Category API design
3. Flyway migration setup
4. Model and DTO creation
5. Repository implementation
6. Service implementation
7. Exception hierarchy and global handler
8. Controller implementation
9. Repository tests
10. Service tests
11. Controller tests
12. Documentation and request examples
13. Final verification

---

## Deliverables Checklist
- [ ] Flyway migration for `categories`
- [ ] `Category` model
- [ ] Category DTOs
- [ ] `CategoryRepository`
- [ ] `CategoryService`
- [ ] `CategoryController`
- [ ] `ApplicationException` and category-specific exceptions
- [ ] `GlobalExceptionHandler`
- [ ] Repository integration tests
- [ ] Service integration tests
- [ ] Controller tests
- [ ] Updated `requests.http`
- [ ] Updated `README.md`
- [ ] Passing `test` and `verify` builds

---

## Risks and Attention Points
- The current project still contains product-oriented SQL/resources that may conflict with the new Category-focused application direction.
- `AGENTS.md` contains duplicated repository guidance with conflicting references to JDBC and JPA; implementation should stay aligned with the actual project stack, which is Spring Data JDBC.
- Existing ArchUnit rules enforce package names and class suffixes, so file placement and naming must be planned carefully from the beginning.
- The existing logging rule requires logging on `@GetMapping` methods, which should be accounted for in the controller design.

---

## Definition of Done for This Feature
The feature is done when:
- The `Category` resource fully supports create, read one, read all, update, and delete.
- The database schema is managed through Flyway.
- H2 is used successfully for development and tests.
- All controller, service, and repository tests pass, including edge cases.
- The application passes the project quality gates defined in `AGENTS.md`.

