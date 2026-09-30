# Runtime View

## Builder Rendering

### Render A Code And Message
`dsn~render-code-and-message~1`

**Given** a builder contains an error code and accumulated message text
**When** `toString()` is called
**Then** the builder emits the code, an optional `: ` separator, and the rendered message.

Status: draft

Covers:
- `scn~render-code-without-message~1`
- `scn~render-appended-message-text~1`

Needs: impl, utest

### Resolve Explicit And Inline Parameters
`dsn~resolve-parameters~1`

**Given** message or mitigation text contains placeholders
**When** explicit definitions and/or inline arguments are mapped
**Then** definitions are stored by reference and inline arguments are assigned in placeholder order.

Status: draft

Covers:
- `scn~substitute-explicit-parameter~1`
- `scn~substitute-inline-arguments-in-order~1`

Needs: impl, utest

### Report Unknown And Null Values
`dsn~report-unknown-and-null-values~1`

**Given** a placeholder has no definition or has a null value
**When** the text is rendered
**Then** the output contains either `UNKNOWN PLACEHOLDER('<reference>')` or `<null>`.

Status: draft

Covers:
- `scn~identify-unknown-placeholder~1`
- `scn~render-null-parameter~1`

Needs: impl, utest

### Apply Scalar Quoting
`dsn~apply-scalar-quoting~1`

**Given** a non-null scalar parameter and a placeholder quoting mode
**When** the value is rendered
**Then** `Quoter` applies automatic type-based quoting or the selected explicit mode.

Status: draft

Covers:
- `scn~automatically-quote-string~1`
- `scn~apply-explicit-quoting-modes~1`

Needs: impl, utest

### Apply Recursive Collection Quoting
`dsn~apply-collection-quoting~1`

**Given** a collection parameter
**When** the value is rendered
**Then** `Quoter` renders bracketed elements separated by comma-space and recursively applies the mode.

Status: draft

Covers:
- `scn~render-collection~1`

Needs: impl, utest

### Render Mitigation Advice
`dsn~render-mitigations~1`

**Given** zero, one, or multiple mitigations have been added
**When** the builder is rendered
**Then** zero adds nothing, one is appended inline, and multiple use the ordered `Known mitigations` list format.

Status: draft

Covers:
- `scn~render-single-mitigation~1`
- `scn~render-multiple-mitigations~1`

Needs: impl, utest

### Render Ticket Advice
`dsn~render-ticket-mitigation~1`

**Given** a builder with a message
**When** `ticketMitigation()` is called
**Then** it adds the fixed internal-error GitHub issue advice through the normal mitigation pipeline.

Status: draft

Covers:
- `scn~render-ticket-mitigation~1`

Needs: impl, utest

## Public Integration

### Expose Parameter Metadata
`dsn~expose-parameter-metadata~1`

**Given** a `ParameterDefinition` has a description
**When** a catalog tool queries it
**Then** the name, value, and description are available through the public model.

Status: draft

Covers:
- `scn~expose-parameter-description~1`

Needs: impl, utest

### Export The Java Module Package
`dsn~export-java-module-package~1`

**Given** a modular client resolves the published artifact
**When** it reads module metadata
**Then** module `error.reporting.java` exports `com.exasol.errorreporting`.

Status: draft

Covers:
- `scn~resolve-java-module-export~1`
- `constr~java-11-module-packaging~1`

Needs: impl, utest

## Open Issues

* Implementation and test coverage markers have not yet been added; this pass drafts requirements and design only.
