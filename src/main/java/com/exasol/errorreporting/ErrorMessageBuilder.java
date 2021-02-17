package com.exasol.errorreporting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builder for Exasol error messages.
 */
public class ErrorMessageBuilder {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final String errorCode;
    private final StringBuilder messageBuilder = new StringBuilder();
    private final List<String> mitigations = new ArrayList<>();
    private final Map<String, String> parameterMapping = new HashMap<>();

    /**
     * Create a new instance of
     *
     * @param errorCode Exasol error code
     */
    ErrorMessageBuilder(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Add exception message.
     * <p>
     * If this method is called multiple times, the message is appended.
     * </p>
     *
     * @param message exception message
     * @return self for fluent programming
     */
    public ErrorMessageBuilder message(final String message) {
        this.messageBuilder.append(message);
        return this;
    }

    /**
     * Format a given message pattern with place holders, filling them with the arguments passed in the specified form.
     *
     * Place holders are defined in the message pattern by using curly brackets `{}`. By default, arguments are
     * formatted with simple quotes unless specified other wise with the 'unquoted' format, defined by `{|uq}`.
     *
     * You can also define names in the place holders. This name will be shown in case no argument is missing, by
     * `{argumentName}` or `{argumentName|uq}`.
     *
     * Below you can find examples on how to use it.
     *
     * Example for quoted arguments:
     *
     * `ErrorMessageBuilder("ERROR_CODE").format("Message with {namedQuotedArgument}, {} and {missingQuotedArgument},
     * "named", "unnamed")`
     *
     * returns "ERROR_CODE: Message with 'named', 'unnamed' and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
     *
     * Example for unquoted arguments:
     *
     * `ErrorMessageBuilder("ERROR_CODE").format("Message with {namedUnquotedArgument|uq}, {|uq} and
     * {missingUnquotedArgument|uq}, "named", "unnamed")`
     *
     * returns "ERROR_CODE: Message with named, unnamed and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
     *
     * @param messagePattern message pattern with place holders
     * @param arguments      arguments to fill the place holders
     * @return self for fluent programming
     */
    public ErrorMessageBuilder formatMessage(final String messagePattern, final Object... arguments) {
        this.messageBuilder.append(MessageFormatter.formatMessage(messagePattern, this.getPatternArguments(arguments)));
        return this;
    }

    private Object[] getPatternArguments(final Object[] arguments) {
        if (arguments == null) {
            return new Object[] { null };
        }
        return arguments;
    }

    /**
     * Add a mitigation. Explain here what users can do to resolve or avoid this error.
     *
     * For learning about the format rules, see {@link ErrorMessageBuilder#formatMessage(String, Object...)}}
     *
     * @param mitigationPattern mitigation message pattern with place holders
     * @param arguments         arguments to fill the place holders
     * @return self for fluent programming
     */
    public ErrorMessageBuilder formatMitigation(final String mitigationPattern, final Object... arguments) {
        this.mitigations.add(MessageFormatter.formatMessage(mitigationPattern, this.getPatternArguments(arguments)));
        return this;
    }

    /**
     * Add a parameter. This method quotes the parameter.
     * <p>
     * You can use the parameter in message and mitigation using {@code {{parameter}}}.
     * </p>
     *
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @return self for fluent programming
     */
    public ErrorMessageBuilder parameter(final String placeholder, final Object value) {
        return this.unquotedParameter(placeholder, Quoter.quoteObject(value));
    }

    /**
     * Add a parameter. This method quotes the parameter.
     * <p>
     * You can use the parameter in message and mitigation using {@code {{parameter}}}.
     * </p>
     *
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @param description description for the error catalog
     * @return self for fluent programming
     */
    public ErrorMessageBuilder parameter(final String placeholder, final Object value, final String description) {
        return this.parameter(placeholder, value);
    }

    /**
     * Add a parameter without quotes.
     *
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @return self for fluent programming
     */
    public ErrorMessageBuilder unquotedParameter(final String placeholder, final Object value) {
        this.parameterMapping.put(placeholder, Objects.requireNonNullElse(value, "<null>").toString());
        return this;
    }

    /**
     * Add a parameter without quotes.
     *
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @param description description for the error catalog
     * @return self for fluent programming
     */
    public ErrorMessageBuilder unquotedParameter(final String placeholder, final Object value,
            final String description) {
        return this.unquotedParameter(placeholder, value);
    }

    /**
     * Add a mitigation. Explain here what users can do to resolve or avoid this error.
     *
     * @param mitigation explanation
     * @return self for fluent programming
     */
    public ErrorMessageBuilder mitigation(final String mitigation) {
        this.mitigations.add(mitigation);
        return this;
    }

    /**
     * Add a mitigation for cases in which the only thing a user can do is opening a ticket.
     *
     * @return self for fluent programming
     */
    public ErrorMessageBuilder ticketMitigation() {
        mitigation("This is an internal error that should not happen. Please report it by opening a GitHub issue.");
        return this;
    }

    /**
     * Build the error message.
     *
     * @return built error massage string
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(this.errorCode);
        if (this.messageBuilder.length() > 0) {
            result.append(": ");
            result.append(replacePlaceholders(this.messageBuilder.toString()));
        }
        if (this.mitigations.size() == 1) {
            result.append(" ");
            result.append(replacePlaceholders(this.mitigations.get(0)));
        } else if (this.mitigations.size() > 1) {
            result.append(" Known mitigations:");
            this.mitigations.forEach(mitigation -> {
                result.append("\n* ");
                result.append(replacePlaceholders(mitigation));
            });
        }
        return result.toString();
    }

    private String replacePlaceholders(final String subject) {
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(subject);
        final StringBuilder resultBuilder = new StringBuilder();
        int lastMatchEnd = 0;
        while (matcher.find()) {
            final String placeholder = matcher.group(1);
            resultBuilder.append(subject.substring(lastMatchEnd, matcher.start()));
            resultBuilder.append(resolvePlaceholder(placeholder));
            lastMatchEnd = matcher.end();
        }
        resultBuilder.append(subject.substring(lastMatchEnd));
        return resultBuilder.toString();
    }

    private String resolvePlaceholder(final String placeholder) {
        if (this.parameterMapping.containsKey(placeholder)) {
            return this.parameterMapping.get(placeholder);
        } else {
            return "UNKNOWN PLACEHOLDER('" + placeholder + "')";
        }
    }
}
