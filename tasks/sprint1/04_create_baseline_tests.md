# Task 04: Create Baseline Unit Tests

**ID**: sprint1.task04
**Title**: Provide baseline unit tests for each module
**Dependencies**: sprint1.task02

**Description**:
- Create an initial test class stub for each module (`midi`, `runtime`, `cli`) using JUnit 5.
- Tests should compile and pass, serving as placeholders for later test coverage.

**Deliverables**:
- `midi/src/test/kotlin/MidiModuleSmokeTest.kt` with a single `assertTrue(true)` test
- `runtime/src/test/kotlin/RuntimeModuleSmokeTest.kt` with a single `assertTrue(true)` test
- `cli/src/test/kotlin/CliModuleSmokeTest.kt` with a single `assertTrue(true)` test

**Constraints**:
- Use JUnit 5 (`org.junit.jupiter.api.Test`)
- Tests must compile and pass to satisfy CI

**References**:
- Baseline tests enable CI pipeline success and future coverage tracking