# Task 03: Establish CI Pipeline

**ID**: sprint1.task03
**Title**: Configure continuous integration workflow
**Dependencies**: sprint1.task02

**Description**:
- Add a CI workflow to build the project and run tests on push and pull requests.
- Use GitHub Actions (or preferred CI) to set up JDK, cache Gradle, and execute `./gradlew build` and `./gradlew test`.

**Deliverables**:
- `.github/workflows/ci.yml` (or equivalent) that:
  - Checks out the code
  - Sets up JDK 11+ (matrix if needed)
  - Caches Gradle wrapper and build cache
  - Runs `./gradlew build --no-daemon --parallel`
  - Archives build/test reports

**Constraints**:
- Workflow must be valid YAML and pass GitHub Actions lint
- Do not include secrets or environment-specific variables

**References**:
- CI integration is part of Sprint 1 deliverables (CI green)