# Crosscutting Concepts

## Domain Model

An error message consists of an error code, zero or more message fragments, zero or more mitigations, and a set of named parameter definitions. A placeholder contains a reference and a quoting mode. A parameter definition contains a name, optional value, and optional catalog description.

## Configuration

There is no runtime configuration. Quoting is selected in placeholder text and error-code lifecycle configuration belongs to consuming projects.

## Error Handling

Missing placeholders become visible diagnostic text rather than exceptions. Null values become `<null>`. The builder does not validate error-code syntax.

An important design rule in this project is that it is always better to have incomplete error output than missing output. Especially, if the missing part is highlighted.

## Logging and Observability

The library emits no logs, metrics, traces, or telemetry. Its observable result is the returned string.

## Security and Privacy

The library has no authentication, authorization, storage, or network boundary. Parameter values are inserted into returned strings; callers remain responsible for avoiding secrets or sensitive data in error messages.
