package com.exasol.errorreporting;

import java.util.*;

/**
 * Builder for Exasol error messages.
 */
public class ErrorMessageBuilder {
    private final String errorCode;
    private final StringBuilder messageBuilder = new StringBuilder();
    private final List<String> mitigations = new ArrayList<>();
    private final Map<String, Object> parameterMapping = new HashMap<>();
    private final Map<String, Object> explicitlyUnquotedParameterMapping = new HashMap<>();

    /**
     * Create a new instance of
     *
     * @param errorCode Exasol error code
     */
    ErrorMessageBuilder(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Format a given message pattern with placeholders, filling them with the arguments passed in the specified form.
     * <p>
     * Placeholders are defined in the message pattern by using double curly brackets {@code {{}}}. By default,
     * arguments are formatted with simple quotes unless specified other wise with the 'unquoted' format, defined by
     * {@code {{|uq}}}.
     * </p>
     * <p>
     * You should always define names in the placeholders. This name will be shown in case no argument is missing, by
     * {@code {{argumentName}}} or {@code {{argumentName|uq}}}.
     * </p>
     * <p>
     * Below you can find examples on how to use it.
     * </p>
     * <p>
     * Example for quoted arguments:
     * </p>
     * <p>
     * {@code ErrorMessageBuilder("ERROR_CODE").message("Message with {{namedQuotedArgument}}, {{}} and
     * {{missingQuotedArgument}}, "named", "unnamed")}
     * </p>
     * <p>
     * returns "ERROR_CODE: Message with 'named', 'unnamed' and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
     * </p>
     * <p>
     * Example for unquoted arguments:
     * </p>
     * <p>
     * {@code ErrorMessageBuilder("ERROR_CODE").message("Message with {{namedUnquotedArgument|uq}}, {{|uq}} and
     * {{missingUnquotedArgument|uq}}, "named", "unnamed")}
     * </p>
     * <p>
     * returns "ERROR_CODE: Message with named, unnamed and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
     * </p>
     *
     * @param message   message that may contain placeholders
     * @param arguments arguments to fill the placeholders
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
        this.parameterMapping.put(placeholder, value);
        return this;
    }

    /**
     * Add a parameter.
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
     * <p>
     * This method is deprecated. You can define that a parameter is unquoted by adding '|uq' to its correspondent
     * placeholder. For more information, see {@link ErrorMessageBuilder#message(String, Object...)}.
     * </p>
     *
     * @deprecated As of release 3.0.0
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @return self for fluent programming
     */
    @Deprecated(since = "3.0.0", forRemoval = true)
    public ErrorMessageBuilder unquotedParameter(final String placeholder, final Object value) {
        this.explicitlyUnquotedParameterMapping.put(placeholder, value);
        this.parameter(placeholder, value);
        return this;
    }

    /**
     * Add a parameter without quotes.
     *
     * <p>
     * This method is deprecated. You can define that a parameter is unquoted by adding '|uq' to its correspondent
     * placeholder. For more information, see {@link ErrorMessageBuilder#message(String, Object...)}.
     * </p>
     *
     * @deprecated As of release 3.0.0
     * @param placeholder placeholder without parentheses
     * @param value       value to insert
     * @param description description for the error catalog
     * @return self for fluent programming
     */
    @Deprecated(since = "3.0.0", forRemoval = true)
    public ErrorMessageBuilder unquotedParameter(final String placeholder, final Object value,
            final String description) {
        return this.unquotedParameter(placeholder, value);
    }

    /**
     * Add a mitigation. Explain here what users can do to resolve or avoid this error.
     * <p>
     * For learning about the format rules, see {@link ErrorMessageBuilder#message(String, Object...)}.
     * </p>
     *
     * @param mitigation mitigation message that may contain placeholders
     * @param arguments  arguments to fill the placeholders
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
        return PlaceholdersFiller.fillPlaceholders(subject, this.parameterMapping,
                this.explicitlyUnquotedParameterMapping);
    }
}
