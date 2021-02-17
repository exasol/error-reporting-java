package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formatter for messages with place holders.
 *
 * Place holders are defined in the message pattern by using curly brackets `{}`. By default, arguments are formatted
 * with simple quotes unless specified other wise with the 'unquoted' format, defined by `{|uq}`.
 *
 * You can also define names in the place holders. This name will be shown in case no argument is missing, by
 * `{argumentName}` or `{argumentName|uq}`.
 *
 * Below you can find examples on how to use it.
 *
 * Example for quoted arguments:
 *
 * `MessageFormatter.formatMessage("Message with {namedQuotedArgument}, {} and {missingQuotedArgument}, "named",
 * "unnamed")`
 *
 * is formatter to "Message with 'named', 'unnamed' and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
 *
 * Example for unquoted arguments:
 *
 * `MessageFormatter.formatMessage("Message with {namedUnquotedArgument|uq}, {|uq} and {missingUnquotedArgument|uq},
 * "named", "unnamed")`
 *
 * is formatter to "Message with named, unnamed and UNKNOWN PLACEHOLDER('anotherQuotedArgument')".
 *
 */
public class MessageFormatter {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^\\}]*)\\}");
    private final StringBuilder resultBuilder = new StringBuilder();
    private final Matcher matcher;
    private int argumentIndex = 0;
    private int placeholderEndPosition = 0;
    private final Object[] arguments;
    private final String messagePattern;

    /**
     * Format a given message pattern with place holders, filling them with the arguments passed in the specified form.
     *
     * @param messagePattern message with place holders
     * @param arguments      arguments to fill the place holders
     * @return formatted message as String
     */
    public static String formatMessage(final String messagePattern, final Object[] arguments) {
        return new MessageFormatter(messagePattern, arguments).format();
    }

    private MessageFormatter(final String messagePattern, final Object[] arguments) {
        this.arguments = arguments;
        this.messagePattern = messagePattern;
        this.matcher = MessageFormatter.PLACEHOLDER_PATTERN.matcher(this.messagePattern);
    }

    private String format() {
        while (this.isPlaceHolderFound()) {
            this.processPlaceHolder();
            this.moveToNextPlaceHolder();
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    private boolean isPlaceHolderFound() {
        return this.matcher.find();
    }

    private void processPlaceHolder() {
        this.appendSectionBeforeNextPlaceHolder();
        this.appendArgument();
    }

    private void appendArgument() {
        final String placeholder = this.getCurrentArgumentPlaceHolder();
        if (this.isArgumentFound()) {
            this.appendFoundArgument(placeholder);
        } else {
            this.appendNotFoundArgument(placeholder);
        }
    }

    private String getCurrentArgumentPlaceHolder() {
        return this.matcher.group(1);
    }

    private void appendFoundArgument(final String placeholder) {
        if (this.isNullArgument()) {
            this.appendNullArgument();
        } else {
            this.appendRegularArgument(placeholder);
        }
    }

    private void appendSectionBeforeNextPlaceHolder() {
        this.resultBuilder.append(this.getMessagePortionBeforeNextArgument());
    }

    private String getMessagePortionBeforeNextArgument() {
        return this.messagePattern.substring(this.placeholderEndPosition, this.matcher.start());
    }

    private void appendRegularArgument(final String placeholder) {
        if (this.isUnquotedParameter(placeholder)) {
            this.appendUnquotedArgument();
        } else {
            this.appendQuotedArgument();
        }
    }

    private void appendQuotedArgument() {
        this.resultBuilder.append(this.quoteArgument());
    }

    private Object quoteArgument() {
        return Quoter.quoteObject(this.getCurrentArgument());
    }

    private Object getCurrentArgument() {
        if (this.isArgumentProvided()) {
            return this.arguments[this.argumentIndex];
        }
        return null;
    }

    private void appendUnquotedArgument() {
        this.resultBuilder.append(this.getCurrentArgument());
    }

    private void appendNotFoundArgument(final String placeholder) {
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.resultBuilder.append("UNKNOWN PLACEHOLDER('" + parameterName + "')");
    }

    private void appendNullArgument() {
        this.resultBuilder.append("<null>");
    }

    private String parserParameterNameFrom(final String placeholder) {
        if (this.isUnquotedParameter(placeholder)) {
            return placeholder.substring(0, placeholder.length() - 3);
        }
        return placeholder;
    }

    private boolean isUnquotedParameter(final String placeholder) {
        return placeholder.endsWith("|uq");
    }

    private boolean isNullArgument() {
        return this.isArgumentFound() && (this.getCurrentArgument() == null);
    }

    /**
     * @return true if a single null argument was provided, or an actual argument in the arguments array is provided.
     */
    private boolean isArgumentFound() {
        return this.isSingleNullArgument() || this.isArgumentProvided();
    }

    private boolean isSingleNullArgument() {
        return this.arguments == null;
    }

    private boolean isArgumentProvided() {
        return !this.isSingleNullArgument() && (this.argumentIndex < this.arguments.length);
    }

    private void moveToNextPlaceHolder() {
        this.argumentIndex++;
        this.placeholderEndPosition = this.matcher.end();
    }

    private void appendRestOfTheMessage() {
        this.resultBuilder.append(this.messagePattern.substring(this.placeholderEndPosition));
    }

    private String getFullMessage() {
        return this.resultBuilder.toString();
    }
}
