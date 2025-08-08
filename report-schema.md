# Report Schema (JSON)

```json
{
  "version": 1,
  "passed": true,
  "interactions_total": 3,
  "matched": 3,
  "unmatched_requests": [],
  "errors": [],
  "violations": [
    {
      "type": "checksum_mismatch",
      "offset": 27,
      "expected": "sum7=0x3C",
      "got": "0x00",
      "decoded": { "cmd":"DT1", "address":"0x030000" },
      "raw_hex": "F0 41 ..."
    }
  ],
  "coverage": ["rq1:010000/32"],
  "timing": { "avg_ms": 18, "max_ms": 44 }
}
```

- **violations** may be present even when `passed=true` if running in non‑strict mode (warnings).
- JUnit XML mirrors pass/fail per interaction for CI dashboards.
