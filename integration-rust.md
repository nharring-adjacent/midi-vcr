# Rust Integration

## Test Pattern

1) Spawn emulator
```rust
let mut child = std::process::Command::new("midivcr")
    .args([
        "emulate",
        "--mode","replay",
        "--cassette","./examples/cassettes/d110_patch_read.yaml",
        "--spec","./examples/specs/roland.d110.yaml",
        "--in-port","MIDI-VCR In",
        "--out-port","MIDI-VCR Out",
        "--report","./target/midivcr.json",
    ])
    .spawn()
    .expect("spawn midivcr");
```

2) Wait for ports to appear (poll ALSA/CoreMIDI listing).

3) Run your API under test (must send/receive real SysEx).

4) Assert on report
```rust
#[derive(serde::Deserialize)]
struct Report { passed: bool, errors: Vec<serde_json::Value> }

let report: Report = serde_json::from_reader(std::fs::File::open("./target/midivcr.json")?)?;
assert!(report.passed, "MIDI-VCR errors: {:#?}", report.errors);
```

## Record New Cassettes
- Plug in real device, set:
  - `MIDI_VCR_MODE=record`
  - `MIDI_VCR_UPSTREAM_IN/OUT` to the real device ports
- Run the same test to capture golden interactions.
