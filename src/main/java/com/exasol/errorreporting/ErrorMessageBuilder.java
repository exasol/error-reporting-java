package com.exasol.errorreporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builder for Exasol error messages.
 */
class ErrorMessageBuilder {
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
        this.parameterMapping.put(placeholder, value.toString());
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
            result.append(this.mitigations.get(0));
        } else if (this.mitigations.size() > 1) {
            result.append(" Known mitigations:");
            this.mitigations.forEach(mitigation -> {
                result.append("\n* ");
                result.append(mitigation);
            });
        }
        return result.toString();
    }

    private String replacePlaceholders(final String subject) {
        final Matcher matcher = PLACEHOLDER_PATTERN.matcher(subject);
        final StringBuilder resultBuilder = new StringBuilder();
        while (matcher.find()) {
            final String placeholder = matcher.group(1);
            matcher.appendReplacement(resultBuilder, resolvePlaceholder(placeholder));
        }
        matcher.appendTail(resultBuilder);
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
