# Risks and Technical Debt

## Risks

* Placeholder syntax is parsed by a regular expression and may produce surprising results for malformed or nested braces.
* Error messages can expose any supplied value because the library performs no redaction or sensitive-data policy enforcement.
* Exact text output is a compatibility surface; changing automatic quoting for a Java type can break consumers that parse messages.

## Technical Debt

* The README contains output typos and inconsistent examples.
* The repository has no integration test against the error-code crawler despite documenting that workflow.
* Runtime module resolution and the Java 11 minimum are not covered by automated tests in this repository.
* OFT implementation and test coverage markers are not yet present.

## Open Issues

* Decide whether malformed placeholders should remain best-effort text processing or receive explicit validation behavior.
