# Project Overview

- Create new Rest resource for `Category` entity
- Implement CRUD operations: Create, Read (single + list), Update, Delete
- The `Category` entity has the below columns:
  - `id` field (Long, primary key, auto-generated).
  - `name` field (string).
  - `description` field (string).
- Implement uit & integration tests for the `Category` resource.
- For each layer (controller, service, repository), create integration tests to cover all cases specially edge cases.
- Depend on flyway for database migration and use H2 in-memory database for both development & testing.