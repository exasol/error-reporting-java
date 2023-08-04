# Java Error Reporting

[![Build Status](https://github.com/exasol/error-reporting-java/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/error-reporting-java/actions/workflows/ci-build.yml)
[![Maven Central &ndash; error-reporting-java](https://img.shields.io/maven-central/v/com.exasol/error-reporting-java)](https://search.maven.org/artifact/com.exasol/error-reporting-java)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aerror-reporting-java&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Aerror-reporting-java)

This project contains a Java-Builder for Exasol error messages.
The invocations of the Builder can be parsed by the [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin).

## Usage

### Simple Messages

```java
ExaError.messageBuilder("E-TEST-1").message("Something went wrong.").toString();
```

Result: `E-TEST-1: Something went wrong.`

### Parameters

You can specify placeholders in the message and replace them with parameters values, as follows:

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Unknown input: {{input}}.")
    .parameter("input", "unknown", "The illegal user input.").toString();
```

Result:

    E-TEST-2: Unknown input: 'unknown'.`

The optional third parameter for `parameter(placeholder, value, description)` is used by the [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin) to generate a parameter description.

From version `0.3.0` on you can achieve the same result by specifying the parameter values directly in the `message()` method. This is a convenience variant that is a little more compact, but lacks the chance to describe the parameter.

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Message with {{first-parameter}} and {{second-parameter}}.", "first value", "second value").toString();
```

Result:

    E-TEST-2: Message with 'q-value' and uq-value.

#### Automatic Quoting

When replacing placeholders in messages, `ExaError` quotes the values according to your choices. If you don't specify a quoting option in a placeholder, you get auto-quoting. In this mode values are quoted depending on their type.

| Type                 | Quoted with     | Example                    | Since version |
|----------------------|-----------------|----------------------------|--------------:|
| `String`             | single quotes   | `'Hello world!'`           |               |
| `Character` / `char` | single quotes   | `'A'`                      |               |
| `Path`               | single quotes   | `'/etc/cron.d'`            |         1.0.0 |
| `File`               | single quotes   | `'~/.bashrc'`              |         1.0.0 |
| `URI`                | single quotes   | `'URN:ISBN:0-330-28700-1'` |         1.0.0 |
| `URL`                | single quotes   | `'https://example.org'`    |         1.0.0 |
| null values          | pointy brackets | `<null>`                   |               |
| everything else      | not quoted      | `42`, `3.1415`, `true`     |               |

#### Manual Quoting

If you need a different quoting style, you can add switches to the placeholder definition:

`u`
: unquoted

`q`
: forced single quotes

`d`
: forced double quotes

If multiple conflicting switches are given, the one with the highest precedence (see list above) is taken.

Switches are separated with a pipe symbol `|` from the parameter name.

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Unknown input: {{input|u}}.")
    .parameter("input", "unknown", "The illegal user input.").toString();
```

Result:

    E-TEST-2: Unknown input: unknown.

### Mitigations

The mitigations describe actions the user can take to resolve the error. Here is an example of a mitigation definition:

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Not enough space on device.")
    .mitigation("Delete something.")
    .toString();
```

Result:

    E-TEST-2: Not enough space on device. Delete something.

You can use parameters in mitigations too.

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Not enough space on device {{device}}.")
    .mitigation("Delete something from {{device}}.")
    .parameter("device", "/dev/sda1", "name of the device")
    .toString();
```

Result: 

    E-TEST-2: Not enough space on device '/dev/sda1'. Delete something from '/dev/sda1'.`

You can chain `mitigation` definitions if you want to tell the users that there is more than one solution.

```java
ExaError.messageBuilder("E-TEST-2")
    .message("Not enough space on device.")
    .mitigation("Delete something.")
    .mitigation("Create larger partition.")
    .toString();
```

Result:

    E-TEST-2: Not enough space on device. Known mitigations:
    * Delete something.
    * Create larger partition.

### Never Change the Meaning of an Error

If you have an error that does not fit anymore or was wrong to begin with, don't reuse the error code. Instead,
remove the old one and create a new one with a new code.

What you can do is fix typos in error, improve the readability or make them more detailed. But you should never change
the meaning of an existing error.

### Removing Obsolete Error Codes

In order to ensure a linear history of the error codes, developers should not reuse old error codes.

So when you plan to remove an obsolete error code:

1. Remove it from the implementation
2. Leave the `highest-index` in the `error_code_config.yml` untouched.

   Even if you deleted the entry with the highest number. The whole purpose of that index is to help avoid reusing error codes.

3. Do not reuse the error code (see ["Never Change the Meaning of an Error"](#never-change-the-meaning-of-an-error)).

## Information for Users

- [Changelog](doc/changes/changelog.md)

## Information for Developers

- [Dependencies](dependencies.md)