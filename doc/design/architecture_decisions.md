# Architecture Decisions

## In-Memory Fluent Builder

### How Are Error Messages Composed?

The library uses a mutable fluent builder that defers rendering until `toString()`.

#### Defer Rendering Until Builder Output
`dsn~defer-rendering-until-output~1`

The system stores raw fragments and parameter definitions and performs placeholder replacement when the final string is requested.

Rationale:

Callers can define parameters before or after message fragments, while the final output remains deterministic.

Status: draft

Covers:
- `constr~java-11-module-packaging~1`

Needs: impl

## Open Issues

* The rationale for using `toString()` as the primary rendering operation is inferred from the public API rather than explicitly documented.
