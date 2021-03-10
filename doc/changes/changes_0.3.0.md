# error-reporting-java 0.3.0, released 2021-03-10

Code Name: New API for simpler formatting.

## Summary

In this release we add the possibility to define parameters for place holders directly in the `ErrorMessageBuilder.message` and ``ErrorMessageBuilder.mitigation` methods.

When calling `ErrorMessageBuilder.message` place holders are defined in the message pattern by using double curly brackets `{{}}`.
By default, arguments are formatted with simple quotes unless specified other wise with `{{|uq}}`.

You should always define names in the place holders. This name will be shown in case the argument is missing, by
`{{argumentName}}` or `{{argumentName|uq}}`.

Below you can find examples on how to use this new feature.

Example for quoted arguments:

`ErrorMessageBuilder("ERROR_CODE").message("Message with {{namedQuotedArgument}}, {{}} and {{missingQuotedArgument}}, "named", "unnamed")`
returns "ERROR_CODE: Message with 'named', 'unnamed' and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".

Example for unquoted arguments:

`ErrorMessageBuilder("ERROR_CODE").message("Message with {{namedUnquotedArgument|uq}}, {{|uq}} and {{missingUnquotedArgument|uq}}, "named", "unnamed")`
returns "ERROR_CODE: Message with named, unnamed and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".

The same examples apply for the `ErrorMessageBuilder.mitigation` method.

The `ErrorMessageBuilder.unquotedParameter` API method is also deprecated. To define that a parameter is unquoted you should use the `|uq` suffix in the correspondent placeholder as described above. 

## Features

* #20: Make the API less verbose.
* #23: Deprecate `ErrorMessageBuilder.unquotedParameter` API method.