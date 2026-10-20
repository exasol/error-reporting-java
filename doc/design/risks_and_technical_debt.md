# Risks and Technical Debt

## Risks

* Placeholder syntax is parsed by a regular expression and may produce surprising results for malformed or nested braces.
* Error messages can expose any supplied value because the library performs no redaction or sensitive-data policy enforcement.
* Exact text output is a compatibility surface; changing automatic quoting for a Java type can break consumers that parse messages.
