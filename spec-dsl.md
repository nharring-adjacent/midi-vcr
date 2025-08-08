# Spec DSL (YAML)

## Meta

```yaml
meta:
  manufacturer: Roland
  models: [D-110]
  id: roland.d110
  version: 1
```

## Messages

```yaml
messages:
  - name: rq1
    match: { start: 0xF0, mfr: 0x41, cmd: 0x11, end: 0xF7 }
    schema:
      - const: 0xF0
      - const: 0x41            # manufacturer id
      - u7: device_id
      - bytes: model_id ; length: 2
      - const: 0x11            # RQ1
      - bytes: address ; length: 3
      - u14: size
      - u7: checksum
      - const: 0xF7
    validate:
      - rule: "checksum == roland_sum7(address_bytes + size_bytes)"
      - rule: "all_bytes_7bit"

  - name: dt1
    match: { start: 0xF0, mfr: 0x41, cmd: 0x12, end: 0xF7 }
    schema:
      - const: 0xF0
      - const: 0x41
      - u7: device_id
      - bytes: model_id ; length: 2
      - const: 0x12            # DT1
      - bytes: address ; length: 3
      - bytes: data   ; until: "before(checksum,end)"
      - u7: checksum
      - const: 0xF7
    validate:
      - rule: "checksum == roland_sum7(address_bytes + data)"
      - rule: "all_bytes_7bit"
```

### Length & Control
- `length: N` — fixed bytes
- `u14` — two 7‑bit bytes MSB‑first
- `until: before(checksum,end)` — consume until checksum field

### Choice example
```yaml
- choice: cmd
  cases:
    0x11: { ref: rq1_body }
    0x12: { ref: dt1_body }
```

## Tables (address/length validation)

```yaml
tables:
  - name: d110_ranges
    rows:
      - range: { start: 0x010000, end: 0x01001F } ; data_len: 32
      - range: { start: 0x030000, end: 0x03007F } ; data_len: 128
```

## Responders (optional emulator logic)

```yaml
responders:
  - on: rq1
    send: dt1
    map:
      device_id: "$req.device_id"
      model_id:  "$req.model_id"
      address:   "$req.address"
      data:      "zeros(len=lookup('d110_ranges', address).data_len)"
```

## Built‑in functions
- `roland_sum7(bytes)`, `xor(bytes)`, `twos_compl(bytes)`, `yamaha_xor(bytes)`
- `sum(bytes)`, `slice(bytes, start, len)`, `concat(a,b)`, `zeros(len)`
