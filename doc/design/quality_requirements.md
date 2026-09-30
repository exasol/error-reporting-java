# Quality Requirements

## Requirement Quality

Requirements use `feat` → `req` → `scn` and design uses `dsn`. The draft scenarios are intended to be directly verifiable from exact rendered strings or public API observations.

## Code Quality

The project uses Maven, Project Keeper, JavaDoc-style API documentation, and a Java module descriptor. Source is organized by focused package-private collaborators around the public builder API.

## Test Quality

JUnit Jupiter parameterized tests and Hamcrest assertions cover builder output, placeholder iteration, parameter metadata, and all quoting modes across representative Java types and collections.

## Dependency Policy

Runtime dependencies are absent from the project POM. Test dependencies are JUnit Jupiter Params and Hamcrest; build plugins provide verification, analysis, packaging, and catalog tooling.

## Static Analysis and Security Gates

Project Keeper configures Maven verification, quality summarization, dependency/security checks, JaCoCo, and Sonar-related build tooling through the generated parent. Exact gate thresholds are not stated in this repository.

## Testability and Coverage

The deterministic, in-memory design supports unit testing without external services. No integration or system-test suite is present; crawler compatibility and module resolution are currently evidenced by configuration and source rather than dedicated tests.

## Open Issues

* Exact coverage thresholds and release-blocking quality gates are inherited from the generated parent and should be confirmed if they become normative requirements.
