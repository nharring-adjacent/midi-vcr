# Project Plan for MIDI-VCR

This project plan outlines tasks, milestones, and timeline based on the architecture documented in `architecture.md`【F:architecture.md†L6-L49】.

## Objectives

- Implement a robust, spec-driven SysEx validator and cassette replayer tool with virtual MIDI ports.
- Deliver a stable CLI interface and comprehensive documentation.
- Ensure high test coverage and CI integration.

## Architecture Overview

Refer to the architecture doc for component breakdown, data flow, IR contract, and error model:
- Components: MIDI wrapper, runtime core engine, CLI【F:architecture.md†L8-L19】
- Data flow: client ↔ virtual ports ↔ validator ↔ cassette engine ↔ real device【F:architecture.md†L21-L28】
- Spec IR contract and transport adapters【F:architecture.md†L30-L37】
- Error model and reporting【F:architecture.md†L39-L49】

## Milestones & Timeline

We recommend a 14-week roadmap broken into seven two-week sprints. Adjust durations based on team capacity and priorities.

| Milestone                      | Description                                                                                 | Duration | Dependencies |
|--------------------------------|---------------------------------------------------------------------------------------------|----------|--------------|
| M1: Foundations                | Finalize IR contract; setup project scaffolding, build, and CI                               | 2 weeks  | —            |
| M2: Core Engine I              | Implement spec loader and IR AST; unit tests for IR loader                                  | 2 weeks  | M1           |
| M3: Core Engine II             | Develop streaming validator and built-in validation hooks; transport adapters               | 2 weeks  | M2           |
| M4: Cassette Engine & Canonicalizer | Build cassette engine (record/replay, matching, timing, chaos) and canonicalizer components | 2 weeks  | M3           |
| M5: CLI & Reporting            | Implement Picocli commands; report generator (JSON + JUnit); end-to-end CLI tests          | 2 weeks  | M4           |
| M6: MIDI Wrapper & Integration | Finalize RtMidi JNI wrapper, virtual port manager; integration testing with devices         | 2 weeks  | M5           |
| M7: Docs & Release             | Complete user guide, architecture docs, examples, packaging, final QA, and official release | 2 weeks  | M6           |

*(Total: ~14 weeks; sprints may overlap or be compressed as needed.)*

## Sprint Breakdown

### Sprint 1: Foundations (Weeks 1–2)
- Review and finalize IR schema (AST nodes, hooks, constraints).
- Setup project structure (modules: midi/, runtime/, cli/).
- Establish CI pipelines and build validation.
- **Deliverables**: CI green, skeleton modules, updated docs.

### Sprint 2: Core Engine I (Weeks 3–4)
- Implement spec loader → IR transformation.
- Define IR AST classes and validation interfaces.
- Unit tests for IR loader and schema invariants.
- **Deliverables**: IR loader complete; ≥80% unit coverage for IR module.

### Sprint 3: Core Engine II (Weeks 5–6)
- Streaming validator implementation (7-bit enforcement, framing, real-time filtering).
- Built-in checksum hooks and expression engine.
- Transport adapters for SysEx7/SysEx8.
- **Deliverables**: Validator passing integration tests with sample specs.

### Sprint 4: Cassette Engine & Canonicalizer (Weeks 7–8)
- Develop cassette engine for record/replay, matching rules, timing, chaos injection.
- Implement canonicalizer (strip real-time bytes, normalize IDs).
- Create sample cassette YAMLs and corresponding tests.
- **Deliverables**: Cassette engine and canonicalizer integrated and validated.

### Sprint 5: CLI & Reporting (Weeks 9–10)
- Build Picocli commands: `emulate`, `validate`, `spec lint`, `cassette lint`.
- Implement report generator with JSON and JUnit XML outputs.
- End-to-end CLI integration tests using example specs and cassettes.
- **Deliverables**: Stable CLI UX and reporting; docs/examples updated.

### Sprint 6: MIDI Wrapper & Integration (Weeks 11–12)
- Finalize RtMidi JNI binding and virtual port manager.
- Implement proxy logic for `record`/`auto` modes.
- Integration tests with real and simulated MIDI devices.
- **Deliverables**: Reliable virtual port support and record/replay workflows.

### Sprint 7: Docs & Release (Weeks 13–14)
- Complete user guide, architecture deep-dive documentation, and API reference.
- Package artifacts (JARs, binaries, Docker images).
- Final QA, bug-fixing, version bump, tagging, and release notes.
- **Deliverables**: v1.0.0 release; published artifacts.

## Dependencies & Risk Mitigation
- Lock down IR contract early to avoid schema churn.
- Prototype JNI wrapper early to uncover build and platform issues.
- Validate on macOS, Linux, and Windows (loopMIDI) platforms in parallel.
- Use representative example specs (Roland, Yamaha) to catch edge cases.

## Success Metrics
- ≥90% unit test coverage for core modules.
- CI green across major platforms.
- Successful validation against real-world MIDI devices.
- Clear, comprehensive documentation and published examples.

*This project plan provides a structured roadmap aligning with the documented architecture. Adjust as needed based on resources and stakeholder feedback.*