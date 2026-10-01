# Open Issues

## Requirements And Design Mismatches

* The requirement for crawler-compatible parameter descriptions has no crawler integration test in this repository.
* The Java module requirement is supported by `module-info.java`, but no modular runtime test is present.

## Implemented Behavior Without Requirement

* `ParameterDefinitionList` intentionally returns the first duplicate definition; this fault-tolerance detail is documented in code and tests but is not currently a separate user-level requirement.
* `PlaceholderMatcher` is a public iterable API beyond the primary builder workflow; its iterator contract is tested but only indirectly represented in the system requirements.

## Requirement Without Observed Implementation

* None of the drafted runtime scenarios lacks corresponding implementation evidence.

## Contradictions Between Sources

* README output examples contain stray backticks and inconsistent sample values, while tests define the exact implementation output.
* README lifecycle guidance mentions `error_code_config.yml`, but that file and enforcement logic are outside this repository.

## Decisions Needed

* Confirm whether crawler integration and module resolution need dedicated integration tests.
* OFT implementation and unit-test markers are now present for the covered design items; the module export item still lacks a modular runtime test.
