package com.exasol.errorreporting;

public class PlaceHolderMapper extends MessagePatternProcessor {
    private static final String UNQUOTED_SUFFIX = "|uq";
    private int argumentIndex = 0;
    private final Object[] arguments;
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
        return new PlaceHolderMapper(messagePattern, arguments, errorMessageBuilder).replacePlaceholders();
    }

    public PlaceHolderMapper(final String messagePattern, final Object[] arguments,
            final ErrorMessageBuilder errorMessageBuilder) {
        super(messagePattern);
        this.arguments = arguments;
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @Override
    protected void processPlaceHolder(final String placeholder) {
        this.appendPlaceholder(placeholder);
        this.appendArgument(placeholder);
        this.argumentIndex++;
    }

    private void appendPlaceholder(final String placeholder) {
        final String parameterName = this.parserParameterNameFrom(placeholder);
        this.append("{{" + parameterName + "}}");
    }

    private void appendArgument(final String placeholder) {
        if (this.isArgumentFound()) {
            this.appendFoundArgument(placeholder);
        }
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
            return placeholder.substring(0, placeholder.length() - UNQUOTED_SUFFIX.length());
        }
        return placeholder;
    }
}
