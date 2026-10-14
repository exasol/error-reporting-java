# Open Issues

## Requirements And Design Mismatches

* Crawler integration tests are intentionally excluded to avoid coupling this project to the error-code crawler; see [Independence From The Error-Code Crawler](architecture_decisions.md#independence-from-the-error-code-crawler).
* The Java module requirement is supported by `module-info.java`, but no modular runtime test is present.

## Implemented Behavior Without Requirement

* `ParameterDefinitionList` intentionally returns the first duplicate definition; this fault-tolerance detail is documented in code and tests but is not currently a separate user-level requirement.
* `PlaceholderMatcher` is a public iterable API beyond the primary builder workflow; its iterator contract is tested but only indirectly represented in the system requirements.

## Requirement Without Observed Implementation

* None of the drafted runtime scenarios lacks corresponding implementation evidence.
