package com.exasol.errorreporting;

import java.util.*;

/**
 * Builder for Exasol error messages.
 */
public class ErrorMessageBuilder {
    private final String errorCode;
    private final StringBuilder messageBuilder = new StringBuilder();
    private final List<String> mitigations = new ArrayList<>();
    private final ParameterDefinitionList parameterDefinitions = new ParameterDefinitionList();

    /**
     * Create a new instance of {@link ErrorMessageBuilder}.
     *
     * @param errorCode Exasol error code
     */
    ErrorMessageBuilder(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Define an error message.
     * <p>
     * Error messages can optionally contain placeholders that can be replaced by arguments.
     * </p>
     * @param message   message that may contain placeholders
     * @param arguments arguments to fill the placeholders
     * @return self for fluent programming
     */
    public ErrorMessageBuilder message(final String message, final Object... arguments) {
        messageBuilder.append(message);
        addParameters(message, arguments);
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
     * @param name parameter name
     * @param value value to insert
     * @return self for fluent programming
     */
    public ErrorMessageBuilder parameter(final String name, final Object value) {
        parameterDefinitions.add(ParameterDefinition.builder(name).value(value).build());
        return this;
    }

    /**
     * Add a parameter.
     * <p>
     * You can use the parameter in message and mitigation using {@code {{parameter}}}.
     * </p>
     * <p>
     * Note that the last parameter exists only as a means to add a description in the error catalog. It is not used
     * when displaying error messages to the application users.
     * </p>
     *
     * @param placeholder placeholder without parentheses
     * @param value value to insert
     * @param ignoredDescription description for the error catalog
     * @return self for fluent programming
     */
    public ErrorMessageBuilder parameter(final String placeholder, final Object value,
            final String ignoredDescription) {
        return parameter(placeholder, value);
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
        mitigations.add(mitigation);
        addParameters(mitigation, arguments);
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
        return PlaceholdersFiller.fillPlaceholders(subject, this.parameterDefinitions);
    }
}
