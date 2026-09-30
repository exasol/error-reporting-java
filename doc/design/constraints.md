# Architecture Constraints

## Technical Constraints

The implementation is a Java library with no runtime dependencies declared in `pom.xml`. The build targets Java 11 and publishes the named module `error.reporting.java`.

### Java 11 Module Packaging
`constr~java-11-module-packaging~1`

The library remains usable as a Java 11 module and exports `com.exasol.errorreporting` from module `error.reporting.java`.

Rationale:

The generated parent sets `java.version` to 11 and `module-info.java` declares the module and export.

Status: draft

Needs: dsn

## Organizational Constraints

No additional intentional organizational constraints were found in the repository.

## Assumptions

* The library is embedded in a consuming application rather than run as a standalone process.
* Error-code lifecycle and catalog generation are coordinated by consuming projects and the error-code crawler.

## Open Issues

* The repository does not state whether Java 11 is a minimum runtime guarantee or only the current build target.
