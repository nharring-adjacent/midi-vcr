# Task 02: Set Up Project Scaffolding

**ID**: sprint1.task02
**Title**: Initialize multi-module project structure (midi, runtime, cli)
**Dependencies**: None

**Description**:
- Create a Gradle Kotlin DSL multi-module skeleton to house the three main components:
  - `midi/` (JNI wrapper & port manager)
  - `runtime/` (core engine)
  - `cli/` (Picocli commands)
- Configure root-level `settings.gradle.kts` and `build.gradle.kts` to include subprojects.
- Establish source directory layout for each module under `src/main/kotlin` and `src/test/kotlin`.

**Deliverables**:
- `settings.gradle.kts` listing `:midi`, `:runtime`, and `:cli`
- Root `build.gradle.kts` with common plugin and dependency management placeholders
- Empty `build.gradle.kts` files in each module directory
- Directory structure:
  ```
  midi/
    src/{main,test}/kotlin
  runtime/
    src/{main,test}/kotlin
  cli/
    src/{main,test}/kotlin
  ```

**Constraints**:
- Follow Gradle Kotlin DSL conventions
- Do not add business logic or tests in this task

**References**:
- Component breakdown:【F:architecture.md†L8-L19】