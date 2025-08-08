# MIDI-VCR (Kotlin) — Spec-Driven SysEx Validator & Cassette Replayer

**MIDI-VCR** is a Kotlin/JVM tool that:
1) Loads **device specs** (YAML) to validate SysEx messages (7-bit rules, framing, checksums, layouts).
2) Records & replays **cassettes** (YAML) of request/response flows to test real I/O, multi-step logic, and error handling.
3) Exposes **virtual MIDI ports** so clients must actually send/receive bytes (no mocks).

Primary use case: validate a Rust MIDI library with strongly-typed models by forcing real SysEx traffic in CI.

## Highlights
- Cross-platform: macOS/Linux (virtual ports via RtMidi). Windows supported; virtual ports typically require loopMIDI.
- VCR-style **modes**: `record`, `replay`, `auto`, `lockdown`, `off`.
- **Spec DSL** for manufacturers/models (checksums, choices, address tables).
- **Cassette DSL** for multi-interaction flows, matching rules, timing, and chaos/error injection.
- **Reports**: JSON + JUnit XML (CI-friendly).

## Quick Start

```bash
# Replay a cassette with virtual ports:
midivcr emulate \
  --mode replay \
  --cassette ./examples/cassettes/d110_patch_read.yaml \
  --spec ./examples/specs/roland.d110.yaml \
  --in-port  "MIDI-VCR In" \
  --out-port "MIDI-VCR Out" \
  --report ./reports/midivcr.json
