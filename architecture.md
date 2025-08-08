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

### AST Node Types

| Node    | Parameters                             | Description                                      |
|---------|----------------------------------------|--------------------------------------------------|
| `Seq`   | `nodes: List<Node>`                    | Ordered sequence of child nodes.                 |
| `Const` | `byte: Byte`                           | Literal byte value to match or emit.             |
| `U7`    | `name: String`                         | Named 7-bit field.                               |
| `U14`   | `name: String`                         | Named 14-bit field (two successive U7 bytes).    |
| `Bytes` | `name: String, len: Int`               | Raw byte array of fixed length.                  |
|         | `name: String, until: Byte`            | Raw bytes until sentinel value.                  |
| `Choice`| `cases: Map<Byte, Node>`               | Branch into sub-spec based on key byte.          |
| `Repeat`| `node: Node, count: Int` or `until: Expr` | Repeat sub-node fixed times or until condition. |

### Built-in Validation Hooks

- `all_bytes_7bit`: ensure every byte in payload is ≤ 0x7F.
- Checksum functions:
  - `roland_sum7`, `xor`, `twos_compl`, `yamaha_xor` (configurable).

Hooks are declared under `validate` in a spec:

```yaml
validate:
  - all_bytes_7bit
  - checksum: roland_sum7
```

### Expression Engine for Computed Fields & Rules

Supports integer and byte-array operations, slices, sums, and comparisons.  
Computed fields and validation rules use expressions:

```yaml
computed:
  data_length:
    expr: "payload.size()"    # bytes count

validate:
  - expr: "data_length == expected_length"
```

### Transport Adapters

- Raw MIDI (SysEx7): de-chunk framing, validate 7-bit constraints.  
- MIDI 2.0 UMP SysEx7/SysEx8: de-chunk UMP packets to payload then validate.

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
