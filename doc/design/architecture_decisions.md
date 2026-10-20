# Architecture Decisions

## In-Memory Fluent Builder

### How Are Error Messages Composed?

The library uses a mutable fluent builder that defers rendering until `toString()`.

#### Defer Rendering Until Builder Output
`dsn~defer-rendering-until-output~1`

The system stores raw fragments and parameter definitions and performs placeholder replacement when the final string is requested.

Rationale:

Callers can define parameters before or after message fragments, while the final output remains deterministic. The builder makes the code more readable. It is functionally similar to Java's built-in `StringBuilder` which has the advantage of being well-known in the Java developer community. 

Covers:
- `constr~java-11-module-packaging~1`

Needs: impl

## Independence From The Error-Code Crawler

### Should This Project Test Crawler Integration?

This project intentionally does not add an integration test with the error-code crawler.

Rationale:

The library should remain independent of the error-code crawler. An integration test would couple this project's test suite to the crawler and its changes, while applying the crawler and publishing error catalogs are outside this project's scope.

Consequences:

The public fluent API remains readable by source-based catalog tooling, while crawler integration is outside this project's test scope. This decision does not resolve whether module resolution needs a dedicated modular runtime test.
