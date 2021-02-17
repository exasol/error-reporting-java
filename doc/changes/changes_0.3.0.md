# error-reporting-java 0.3.0, released 2021-??-??

Code Name: New API for simpler formatting.

In this release we introduce the new `ErrorMessageBuilder.format` method in the public API to format messages in a simpler way.

When calling `ErrorMessageBuilder.format` place holders are defined in the message pattern by using curly brackets `{}`.
By default, arguments are formatted with simple quotes unless specified other wise with `{|uq}`.

You can also define names in the place holders. This name will be shown in case no argument is missing, by
`{argumentName}` or `{argumentName|uq}`.

Below you can find examples on how to use the new `ErrorMessageBuilder.format` API method.

Example for quoted arguments:

`ErrorMessageBuilder("ERROR_CODE").format("Message with {namedQuotedArgument}, {} and {missingQuotedArgument}, "named", "unnamed")`
returns "ERROR_CODE: Message with 'named', 'unnamed' and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".

Example for unquoted arguments:

`ErrorMessageBuilder("ERROR_CODE").format("Message with {namedUnquotedArgument|uq}, {|uq} and {missingUnquotedArgument|uq}, "named", "unnamed")`
returns "ERROR_CODE: Message with named, unnamed and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".

## Features

* # 20: Make the API less verbose.