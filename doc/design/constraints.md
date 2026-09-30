# Architecture Constraints

## Technical Constraints

The implementation is a Java library with no runtime dependencies declared in `pom.xml`. Java 11 is the minimum supported runtime and build target. Support follows the lifecycle of [Eclipse Adoptium OpenJDK releases](https://adoptium.net/support/). The library publishes the named module `error.reporting.java`.

### Java 11 Module Packaging
`constr~java-11-module-packaging~1`

The library remains usable on Java 11 and newer supported runtimes, and exports `com.exasol.errorreporting` from module `error.reporting.java`.

Rationale:

The generated parent sets `java.version` to 11 and `module-info.java` declares the module and export. The supported-runtime policy follows the [Adoptium support lifecycle](https://adoptium.net/support/) as it changes over time.

Status: draft

Needs: dsn

## Organizational Constraints

No additional intentional organizational constraints were found in the repository.

## Assumptions

* The library is embedded in a consuming application rather than run as a standalone process.
* Error-code lifecycle and catalog generation are coordinated by consuming projects and the error-code crawler.
