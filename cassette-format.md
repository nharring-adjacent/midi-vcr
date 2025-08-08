# Cassette Format (YAML)

```yaml
version: 1
name: d110_patch_read
spec: ./examples/specs/roland.d110.yaml     # or spec id "roland.d110@1"
canonicalization:
  strip_realtime: true
  normalize_device_id: "*"
match_on: [mfr, cmd, address, data_len]     # overridable by env
required_interactions:
  - key: "rq1:010000/32"

interactions:
  - request:
      # Example RQ1 (address 0x010000, size 32). Checksum shown as CS placeholder.
      hex: "F0 41 10 16 00 11 01 00 00 00 20 CS F7"
      meta:
        decoded: { cmd: RQ1, address: 0x010000, size: 32 }
    response:
      # Example DT1 with 32 data bytes; CS recomputed on replay.
      hex: "F0 41 10 16 00 12 01 00 00 <32b data> CS F7"
      compute:
        checksum: "roland_sum7(address_bytes + data)"
      delay_ms: 40

  # Error injection example:
  - request:
      key: "rq1:030000/128"                # use a key instead of literal hex
    response:
      hex: "F0 41 10 16 00 12 03 00 00 <128b data> 00 F7"
      inject:
        corrupt_checksum: true
      delay_ms: 10
```

## Chaos Switches (per cassette or env)
- `drop_pct`, `dup_pct`, `max_jitter_ms`, `truncate_at_byte`, `stall_ms_before_response`, `interleave_realtime`
