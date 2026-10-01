# Solution Strategy

## Main Technical Approach

The library uses a fluent, mutable `ErrorMessageBuilder`. It accumulates message text and mitigations, stores parameter definitions in insertion order, and renders only when `toString()` is called. `PlaceholderMatcher` identifies placeholders, `Placeholder` stores their reference and quoting mode, and `PlaceholdersFiller` substitutes values. `Quoter` centralizes scalar and collection formatting.

## Key Quality Drivers

* Small API surface and fluent use in error-definition code.
* Deterministic text output suitable for users, tests, and catalog tooling.
* Fault tolerance for missing, duplicate, unnamed, and null parameters.
* No runtime service or persistence dependencies.
* Java-module compatibility.
* Low runtime overhead when rendering messages.

## Reuse of Existing Facilities

The implementation uses the Java standard library for collections, regular expressions, URLs, URIs, files, paths, and module packaging. JUnit Jupiter and Hamcrest verify behavior in tests; Maven and Project Keeper drive the build.

## Data and Control Flow Strategy

Message and mitigation calls append raw text and map inline arguments by placeholder order. Explicit `parameter` calls add definitions. At render time, each text fragment is scanned independently, placeholders are parsed, values are looked up by name, and the selected quoting strategy produces the final string.
