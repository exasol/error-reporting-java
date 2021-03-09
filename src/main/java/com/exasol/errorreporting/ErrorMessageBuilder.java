package com.exasol.errorreporting;

import java.util.*;

/**
 * Builder for Exasol error messages.
 */
public class ErrorMessageBuilder {
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
     * @param message   message that may contain place holders
     * @param arguments arguments to fill the place holders
     * @return self for fluent programming
     */
    public ErrorMessageBuilder message(final String message, final Object... arguments) {
        this.messageBuilder.append(message);
        this.addParameters(message, arguments);
        return this;
    }

    private void addParameters(final String text, final Object[] arguments) {
        final Object[] patternArguments = this.getPatternArguments(arguments);
        ParametersMapper.mapParametersByName(text, patternArguments, this);
    }

    private Object[] getPatternArguments(final Object[] arguments) {
        if (arguments == null) {
            return new Object[] { null };
        }
        return arguments;
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
     * For learning about the format rules, see {@link ErrorMessageBuilder#formatMessage(String, Object...)}}
     *
     * @param mitigation mitigation message that may contain place holders
     * @param arguments  arguments to fill the place holders
     * @return self for fluent programming
     */
    public ErrorMessageBuilder mitigation(final String mitigation, final Object... arguments) {
        this.mitigations.add(mitigation);
        this.addParameters(mitigation, arguments);
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
        return PlaceHoldersFiller.fillPlaceHolders(subject, this.parameterMapping);
    }
}
