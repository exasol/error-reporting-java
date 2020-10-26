# Java Error Reporting

This project contains a Java-Builder for Exasol error messages.
The invocations of the Builder can be parsed by the [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin).

## Usage

### Simple Messages
```java
ExaError.messageBuilder("E-TEST-1").message("Something went wrong.").toString();
```
result: `E-TEST-1: Something went wrong.`

### Parameters

```java
ExaError.messageBuilder("E-TEST-2")
  .message("Unknown input {{input}}.")
  .parameter("input", 'unknown', "The illegal user input.").toString();
```
result: `E-TEST-2: Unknown input 'unknown'.`

The optional third parameter for `parameter(placeholder, value, description)` is used by the [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin) to generate a parameter description.

The builder automatically quotes parameters (depending on the type of the parameter).
If you don't want that use `unquotedParameter(placeholder, value, description)` instead.
 
### Mitigations

```java
ExaError.messageBuilder("E-TEST-2")
  .message("Too tew disk space.")
  .mitigation("Delete something.")
  .toString();
```
Result: `Too tew disk space. Delete something.`

<hr>

```java
ExaError.messageBuilder("E-TEST-2")
  .message("Too tew disk space.")
  .mitigation("Delete something.")
  .mitigation("Create larger partition.")
  .toString();
```
Result:

```text
Too tew disk space. Known mitigations:
* Delete something.
* Create larger partition.
```

## Additional Resources

* [Changelog](doc/changes/changelog.md)
* [Dependencies](NOTICE)

 
