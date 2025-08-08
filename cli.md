# CLI

## Emulate

```bash
midivcr emulate       --mode ${MIDI_VCR_MODE}       --cassette ${MIDI_VCR_CASSETTES_DIR}/${MIDI_VCR_CASSETTE}       --spec ${MIDI_VCR_SPEC}       --in-port  "${MIDI_VCR_IN_PORT}"       --out-port "${MIDI_VCR_OUT_PORT}"       --report ./reports/midivcr.json
```

### Modes
- `record`: proxy to real device, capture to cassette.
- `replay`: serve from cassette only; fail on unmatched.
- `auto`: replay if match else proxy+record.
- `lockdown`: only allow cassette’s `required_interactions`; fail otherwise.
- `off`: pass‑through; validate if `--spec` set.

### Env Vars (VCR‑style)
- `MIDI_VCR_MODE=record|replay|auto|lockdown|off`
- `MIDI_VCR_CASSETTES_DIR`, `MIDI_VCR_CASSETTE`
- `MIDI_VCR_SPEC` (path or spec id)
- `MIDI_VCR_IN_PORT`, `MIDI_VCR_OUT_PORT`
- `MIDI_VCR_UPSTREAM_IN`, `MIDI_VCR_UPSTREAM_OUT` (record/proxy)
- `MIDI_VCR_TIMEOUT_MS` (default 500)
- `MIDI_VCR_MATCHERS=mfr,cmd,address,data_len`
- `MIDI_VCR_STRICT=1`
- `MIDI_VCR_TIMING=strict|ignore|recorded`
- Chaos: `MIDI_VCR_JITTER_MS`, `MIDI_VCR_DROP_PCT`, `MIDI_VCR_CORRUPT_CS`

## Validate a single hex blob

```bash
midivcr validate --spec ./examples/specs/roland.d110.yaml       --hex "F0 41 10 16 00 11 01 00 00 00 20 CS F7"
```

## Lint

```bash
midivcr spec lint ./examples/specs/roland.d110.yaml
midivcr cassette lint ./examples/cassettes/d110_patch_read.yaml
```
