# System Requirements

## Introduction

Java Error Reporting is a small library for constructing Exasol error messages in application code. A caller starts with an error code, adds a message, supplies named or inline parameter values, and optionally adds one or more mitigations. The resulting string contains predictable placeholder substitution and quoting and can be consumed by users or by the error-code crawler Maven plugin.

## Goals

* Construct consistent, readable Exasol error messages.
* Make parameterized messages concise while retaining an explicit named-parameter API.
* Quote values according to their type or an explicit placeholder switch.
* Communicate one or more possible mitigations, including a standard ticket message.
* Provide a public Java API that can be used from modular applications and tooling.

## Notation

This document uses OpenFastTrace specification items to express product features, user requirements, and acceptance scenarios. Each specification item has a unique identifier in the form `<artifact-type>~<name>~<revision>`.

Feature items use `feat`, user requirements use `req`, and acceptance scenarios use `scn`. Design items under `doc/design/` cover the scenarios with `dsn`.

## Out of Scope

This library does not enforce unique error IDs in any way. It is the responsibility of the caller to assign and maintain IDs.

Also, applying the [crawler](https://github.com/exasol/error-code-crawler-java) and publishing to an [error catalog](https://github.com/exasol/error-code-crawler-maven-plugin) are outside the scope of this project.

## Terms and Abbreviations

###### Error Code

The stable identifier at the beginning of a generated error message, for example `E-TEST-1`.

###### Placeholder

A double-curly-bracket expression such as `{{input}}` that identifies a value to insert into text.

###### Mitigation

Advice appended to an error message that explains how a user can resolve or avoid the error.

###### Automatic Quoting

Quoting selected from the runtime type of a parameter value.

## User Roles

###### Java Application Developer

Uses the fluent API to define and render error messages in application code.

###### Error Catalog Maintainer

Uses parameter descriptions and stable error codes as inputs to the error-code crawler and catalog lifecycle.

###### Application User

Reads the rendered error message and its mitigation advice.

## Features

### Error Message Construction
`feat~error-message-construction~1`

The library constructs a rendered message from an Exasol error code, message text, parameters, and optional mitigation advice.

Needs: req

### Parameter Substitution
`feat~parameter-substitution~1`

The library replaces named placeholders with supplied values in messages and mitigations.

Needs: req

### Value Quoting
`feat~value-quoting~1`

The library presents parameter values with automatic or explicitly selected quoting.

Needs: req

### Mitigation Advice
`feat~mitigation-advice~1`

The library appends one or more mitigation messages, including a standard internal-error ticket mitigation.

Needs: req

### Public Java Integration
`feat~public-java-integration~1`

The library exposes its builder and supporting value types as a reusable Java module and preserves parameter metadata for catalog tooling.

Needs: req

## User Requirements

### Start A Message With Its Error Code
`req~start-message-with-error-code~1`

The caller can create a builder with an error code, and rendering a builder without message text returns the error code unchanged.

Rationale:

Every error must retain a stable code, including messages that contain no additional text.

Covers:
- `feat~error-message-construction~1`

Needs: scn

### Append Message Text Fluently
`req~append-message-text-fluently~1`

The caller can append message fragments through repeated fluent `message` calls, and the rendered result places the complete message after the error code separated by `: `.

Rationale:

The README presents the builder as a fluent API and the implementation accumulates message fragments.

Covers:
- `feat~error-message-construction~1`

Needs: scn

### Define Named Parameters Explicitly
`req~define-named-parameters~1`

The caller can associate a name with a value through `parameter`, and the same name can be referenced from message or mitigation text.

Rationale:

The explicit API supports readable code and an optional catalog description argument.

Covers:
- `feat~parameter-substitution~1`

Needs: scn

### Map Inline Arguments By Placeholder Order
`req~map-inline-arguments-by-order~1`

The caller can pass values directly to `message` or `mitigation`; values map to placeholders in textual order, including unnamed placeholders.

Rationale:

This convenience API was introduced in version 0.3.0.

Covers:
- `feat~parameter-substitution~1`

Needs: scn

### Render Unknown Placeholders Explicitly
`req~render-unknown-placeholders-explicitly~1`

When no value is defined for a placeholder, rendering preserves an explicit `UNKNOWN PLACEHOLDER('<reference>')` diagnostic in its place.

Rationale:

The behavior is asserted for named and unnamed placeholders and avoids silently producing an incomplete error message.

Covers:
- `feat~parameter-substitution~1`

Needs: scn

### Render Null Values
`req~render-null-values~1`

When a referenced value is null or absent from a `ParameterDefinition`, rendering uses `<null>`.

Rationale:

Visualizing null values is better in an error reporting library than rising `NullPointerExceptions`.

Covers:
- `feat~parameter-substitution~1`

Needs: scn

### Apply Automatic Quoting By Type
`req~apply-automatic-quoting~1`

With no quoting switch,

1. strings, characters, paths, files, URLs, and URIs are enclosed in single quotes
2. other non-null values use their string representation
3. null uses `<null>`.

Rationale:

The README defines automatic quoting as the default and lists the supported types.

Covers:
- `feat~value-quoting~1`

Needs: scn

### Support Explicit Quoting Switches
`req~support-explicit-quoting-switches~1`

The placeholder switches `u`, `q`, and `d` select unquoted, forced single-quoted, and forced double-quoted output respectively; when switches conflict, `u` has precedence over `q`, which has precedence over `d`.

Rationale:

Explicit switches let callers control presentation independently of the runtime type, while preserving the legacy `uq` behavior.

Covers:
- `feat~value-quoting~1`

Needs: scn

### Render Collections Recursively
`req~render-collections-recursively~1`

When a parameter is a collection, rendering encloses the elements in brackets, separates them with comma-space, and applies the selected quoting mode to each element.

Covers:
- `feat~value-quoting~1`

Needs: scn

### Append A Single Mitigation
`req~append-single-mitigation~1`

The caller can append one mitigation, and rendering places it after the message separated by a space, with its placeholders resolved using the same parameter rules as the message.

Covers:
- `feat~mitigation-advice~1`

Needs: scn

### Format Multiple Mitigations As A List
`req~format-multiple-mitigations~1`

When multiple mitigations are appended, rendering adds ` Known mitigations:` followed by one `* ` list item per mitigation in insertion order.

Covers:
- `feat~mitigation-advice~1`

Needs: scn

### Provide A Ticket Mitigation
`req~provide-ticket-mitigation~1`

The caller can append the standard internal-error mitigation through `ticketMitigation`.

Rationale:

The convenience API was introduced specifically for errors whose only mitigation is opening a ticket.

Covers:
- `feat~mitigation-advice~1`

Needs: scn

### Preserve Parameter Metadata
`req~preserve-parameter-metadata~1`

The parameter model exposes a name, value, and optional description so catalog tooling can inspect parameter descriptions independently of rendered output.

Covers:
- `feat~public-java-integration~1`

Needs: scn

### Expose The Library As A Java Module
`req~expose-java-module~1`

The published library exposes the `com.exasol.errorreporting` package from the `error.reporting.java` module.

Covers:
- `feat~public-java-integration~1`

Needs: scn

## Acceptance Scenarios

### Render A Code Without Message Text
`scn~render-code-without-message~1`

**Given** a builder created with `E-ERJ-TEST-1`
**When** the builder is rendered without message text
**Then** the result is `E-ERJ-TEST-1`

Covers:
- `req~start-message-with-error-code~1`

Needs: dsn

### Render Appended Message Text
`scn~render-appended-message-text~1`

**Given** a builder created with `E-ERJ-TEST-1`
**When** the caller appends `Test ` and then `message.`
**Then** the result is `E-ERJ-TEST-1: Test message.`

Covers:
- `req~append-message-text-fluently~1`

Needs: dsn

### Substitute An Explicit Parameter
`scn~substitute-explicit-parameter~1`

**Given** message text `Test message {{name}}` and an explicit parameter `name` with value `Ada`
**When** the builder is rendered
**Then** the placeholder is replaced with the quoted value `'Ada'`

Covers:
- `req~define-named-parameters~1`

Needs: dsn

### Substitute Inline Arguments In Order
`scn~substitute-inline-arguments-in-order~1`

**Given** message text `{{first}} and {{second}}`
**When** the caller passes `one` and `2` directly to `message`
**Then** the result contains `'one' and 2` in that order

Covers:
- `req~map-inline-arguments-by-order~1`

Needs: dsn

### Identify An Unknown Placeholder
`scn~identify-unknown-placeholder~1`

**Given** message text `test {{unknown}}` with no parameter named `unknown`
**When** the builder is rendered
**Then** the result contains `UNKNOWN PLACEHOLDER('unknown')`

Covers:
- `req~render-unknown-placeholders-explicitly~1`

Needs: dsn

### Render A Null Parameter
`scn~render-null-parameter~1`

**Given** a referenced parameter whose value is null
**When** the builder is rendered
**Then** the placeholder is replaced with `<null>`

Covers:
- `req~render-null-values~1`

Needs: dsn

### Automatically Quote A String
`scn~automatically-quote-string~1`

**Given** a string parameter with value `value` and no switch
**When** the placeholder is rendered
**Then** the value appears as `'value'`

Covers:
- `req~apply-automatic-quoting~1`

Needs: dsn

### Apply Explicit Quoting Modes
`scn~apply-explicit-quoting-modes~1`

**Given** a numeric parameter with value `42`
**When** it is rendered once with `|u`, once with `|q`, and once with `|d`
**Then** the outputs are `42`, `'42'`, and `"42"` respectively

Covers:
- `req~support-explicit-quoting-switches~1`

Needs: dsn

### Render A Collection
`scn~render-collection~1`

**Given** a collection containing `1` and the string `test`
**When** it is rendered with automatic quoting
**Then** the result is `[1, 'test']`

Covers:
- `req~render-collections-recursively~1`

Needs: dsn

### Render A Single Mitigation
`scn~render-single-mitigation~1`

**Given** message `Something went wrong.` and mitigation `Fix it.`
**When** the builder is rendered
**Then** the result ends with `Something went wrong. Fix it.`

Covers:
- `req~append-single-mitigation~1`

Needs: dsn

### Render Multiple Mitigations
`scn~render-multiple-mitigations~1`

**Given** mitigations `Fix it.` and `Contact support.` in that order
**When** the builder is rendered
**Then** the result contains `Known mitigations:` and two ordered `* ` list items

Covers:
- `req~format-multiple-mitigations~1`

Needs: dsn

### Render The Ticket Mitigation
`scn~render-ticket-mitigation~1`

**Given** a builder with a message
**When** the caller invokes `ticketMitigation`
**Then** the standard internal-error instruction to report a GitHub issue is appended

Covers:
- `req~provide-ticket-mitigation~1`

Needs: dsn

### Expose Parameter Description Metadata
`scn~expose-parameter-description~1`

**Given** a parameter definition built with description `small blue thing`
**When** its description is queried
**Then** `getDescription()` returns that description

Covers:
- `req~preserve-parameter-metadata~1`

Needs: dsn

### Resolve The Java Module Export
`scn~resolve-java-module-export~1`

**Given** the packaged library is used as a Java module
**When** a client resolves module `error.reporting.java`
**Then** package `com.exasol.errorreporting` is exported

Covers:
- `req~expose-java-module~1`

Needs: dsn

## Open Issues

### README Example Output Typos

Source evidence:

* `README.md` parameter examples show stray backticks and one inconsistent value in the displayed result.
* `ErrorMessageBuilderTest.java` asserts the implementation output without those typos.

Issue:

The user guide examples are not fully consistent with the tested behavior.

Decision needed:

Correct the README examples during documentation review; this draft follows the tests and implementation for exact output.

### Error-Code Lifecycle Guidance Is Not Enforced Here

Source evidence:

* `README.md` instructs maintainers not to reuse error codes and to preserve `highest-index` in `error_code_config.yml`.
* No such configuration file or enforcement code exists in this repository.

Issue:

The guidance belongs to consuming projects or the crawler workflow, not to this library’s observable runtime behavior.

Decision needed:

Confirm whether lifecycle guidance should remain documentation-only or be linked to a separate repository requirement.

