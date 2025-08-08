---

# docs/architecture.md

```markdown
# Architecture

## Components

- **midi/** — RtMidi JNI wrapper + port manager
  - Creates virtual IN/OUT ports; proxies to real device ports in `record`/`auto`.
- **runtime/** — Core engine
  - Spec loader → IR (sequence, choice, repeat, computed, constraints)
  - Streaming validator (7-bit, framing, checksums, interleaved real-time ignore)
  - Cassette engine (record/replay, matchers, timing, chaos)
  - Canonicalizer (strip real-time, normalize device ID, etc.)
  - Report generator (JSON + JUnit)
- **cli/** — Picocli commands
  - `emulate`, `validate`, `spec lint`, `cassette lint`

## Data Flow
Rust Client <-> [Virtual Ports] MIDI-VCR <-> (optional) Real Device
|                  ^
v                  |
Spec Validator       Proxy (record)
|
Cassette
(match/replay/record)

## Spec IR (minimal contract)

- Nodes: `Seq(nodes)`, `Const(byte)`, `U7(name)`, `U14(name)`, `Bytes(name,len|until)`, `Choice(cases)`, `Repeat(node,count|until)`.
- Validate hooks:
  - Built-ins: `all_bytes_7bit`, checksum functions (`roland_sum7`, `xor`, `twos_compl`, `yamaha_xor`, configurable).
  - Expression engine for computed fields and rules (ints, byte arrays, sum, slice).
- Transport adapters:
  - Raw MIDI (SysEx7) and MIDI 2.0 UMP SysEx7/SysEx8 (de-chunk to payload then validate).

## Error Model

- Fail on:
  - Non-7-bit data inside SysEx7.
  - Unexpected status bytes (except real-time).
  - Checksum mismatch.
  - Schema mismatch (wrong const, length, choice key).
  - Timeout waiting for match.
- Provide:
  - Hex diff with offset and expected/got.
  - Decoded fields (when possible) to explain failure.
