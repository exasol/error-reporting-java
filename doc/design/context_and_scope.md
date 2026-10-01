# Context and Scope

## System Boundary

Java Error Reporting includes the fluent error-message builder, placeholder parsing and matching, parameter metadata, value quoting, mitigation formatting, and the Java module declaration. It does not own error-code allocation, catalog persistence, application logging, exception transport, or user-interface presentation.

## Users and Neighboring Systems

* Java application code creates and renders error messages.
* Application users read the resulting strings.
* The error-code crawler Maven plugin parses builder invocations and parameter descriptions.
* Maven builds, tests, and publishes the library.

## Supported Environment

The project is built as a Java 11 Maven artifact and can be consumed on Java 11 or newer supported runtimes, either on the class path or through the declared Java module. Supported-runtime maintenance follows the [Eclipse Adoptium OpenJDK lifecycle](https://adoptium.net/support/).

## External Interfaces

The primary interface is the public API in `com.exasol.errorreporting`: `ExaError`, `ErrorMessageBuilder`, `ParameterDefinition`, `ParameterDefinitionList`, `Placeholder`, `PlaceholderMatcher`, and `Quoting`.

## State and Persistence

Builders hold message fragments, mitigations, and parameter definitions in memory. Rendering has no database, file, network, cache, or telemetry dependency.

## Explicit Non-Goals

* Allocating or validating globally unique error codes.
* Persisting an error catalog.
* Sending errors or tickets to remote services.
* Escaping arbitrary message syntax beyond the documented placeholder and quoting rules.
