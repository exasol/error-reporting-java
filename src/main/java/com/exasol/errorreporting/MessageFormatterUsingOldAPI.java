package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFormatterUsingOldAPI {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");// Pattern.compile("\\{([^\\}]*)\\}");
    private static final String UNQUOTED_SUFFIX = "|uq";
    private final StringBuilder resultBuilder = new StringBuilder();
    private final Matcher matcher;
    private int argumentIndex = 0;
    private int placeholderEndPosition = 0;
    private final Object[] arguments;
    private final String messagePattern;
    private final ErrorMessageBuilder errorMessageBuilder;

    /**
     * Format a given message pattern with place holders, filling them with the arguments passed in the specified form.
     *
     * @param messagePattern message with place holders
     * @param arguments      arguments to fill the place holders
     * @return formatted message as String
     */
    public static String formatMessage(final String messagePattern, final Object[] arguments,
            final ErrorMessageBuilder errorMessageBuilder) {
        return new MessageFormatterUsingOldAPI(messagePattern, arguments, errorMessageBuilder).format();
    }

    private MessageFormatterUsingOldAPI(final String messagePattern, final Object[] arguments,
            final ErrorMessageBuilder errorMessageBuilder) {
        this.arguments = arguments;
        this.messagePattern = messagePattern;
        this.errorMessageBuilder = errorMessageBuilder;
        this.matcher = MessageFormatterUsingOldAPI.PLACEHOLDER_PATTERN.matcher(this.messagePattern);
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
        this.appendSectionBeforePlaceHolder();
        this.appendPlaceholder();
        this.appendArgument();
    }

    private void appendPlaceholder() {
        final String placeholder = this.getCurrentPlaceHolder();
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.resultBuilder.append("{{" + parameterName + "}}");
    }

    private void appendSectionBeforePlaceHolder() {
        this.resultBuilder.append(this.getSectionBeforePlaceHolder());
    }

    private String getSectionBeforePlaceHolder() {
        return this.messagePattern.substring(this.placeholderEndPosition, this.matcher.start());
    }

    private void appendArgument() {
        final String placeholder = this.getCurrentPlaceHolder();
        if (this.isArgumentFound()) {
            this.appendFoundArgument(placeholder);
        }
    }

    private String getCurrentPlaceHolder() {
        return this.matcher.group(1);
    }

    private boolean isArgumentFound() {
        return (this.arguments != null) && (this.argumentIndex < this.arguments.length);
    }

    private void appendFoundArgument(final String placeholder) {
        if (this.isNullArgument()) {
            this.appendNullArgument(placeholder);
        } else {
            this.appendRegularArgument(placeholder);
        }
    }

    private boolean isNullArgument() {
        return this.isArgumentFound() && (this.getCurrentArgument() == null);
    }

    private void appendNullArgument(final String placeholder) {
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.errorMessageBuilder.parameter(parameterName, null);
    }

    private void appendRegularArgument(final String placeholder) {
        if (this.isUnquotedParameter(placeholder)) {
            this.appendUnquotedArgument(placeholder);
        } else {
            this.appendQuotedArgument(placeholder);
        }
    }

    private boolean isUnquotedParameter(final String placeholder) {
        return placeholder.endsWith(UNQUOTED_SUFFIX);
    }

    private void appendUnquotedArgument(final String placeholder) {
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.errorMessageBuilder.unquotedParameter(parameterName, this.getCurrentArgument());
    }

    private Object getCurrentArgument() {
        if (this.isArgumentFound()) {
            return this.arguments[this.argumentIndex];
        }
        return null;
    }

    private void appendQuotedArgument(final String placeholder) {
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.errorMessageBuilder.parameter(parameterName, this.getCurrentArgument());
    }

    private String parserParameterNameFrom(final String placeholder) {
        if (this.isUnquotedParameter(placeholder)) {
            return placeholder.substring(0, placeholder.length() - 3);
        }
        return placeholder;
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
