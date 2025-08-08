# Task 01: Finalize IR Schema Documentation

**ID**: sprint1.task01
**Title**: Finalize IR schema (AST nodes, hooks, constraints) documentation
**Dependencies**: None

**Description**:
- Review the `Spec IR` section in the architecture doc and lock down the full IR contract.
- Ensure all AST node types, built-in validation hooks, and constraint rules are clearly documented.

**Deliverables**:
- Updated `architecture.md` under **Spec IR** with precise definitions of:
  - AST node types (Seq, Const, U7, U14, Bytes, Choice, Repeat)
  - Built-in hooks (`all_bytes_7bit`, checksum functions, expression engine)
  - Constraint rules for computed fields and validation
- Example snippets illustrating each IR node usage

**Constraints**:
- Maintain existing markdown style in `docs/architecture.md`
- Tie content to the minimal IR contract; do not introduce implementation details

**References**:
- Spec IR section:【F:architecture.md†L30-L37】