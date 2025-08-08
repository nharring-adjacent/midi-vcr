# Task 05: Integrate Tests into CI Pipeline

**ID**: sprint1.task05
**Title**: Ensure CI executes baseline tests and reports results
**Dependencies**: sprint1.task03, sprint1.task04

**Description**:
- Update the CI workflow to include test execution and result reporting.
- Verify that smoke tests in each module are executed and their results are captured.

**Deliverables**:
- Updated `.github/workflows/ci.yml` with steps to:
  - Run `./gradlew test` after `build`
  - Archive or publish test reports (JUnit XML)
- CI run demonstrates successful test execution across all modules

**Constraints**:
- Do not modify tests themselves; only adjust CI workflow
- Maintain the same CI naming and conventions as initial pipeline

**References**:
- Baseline tests: sprint1.task04
- CI workflow: sprint1.task03