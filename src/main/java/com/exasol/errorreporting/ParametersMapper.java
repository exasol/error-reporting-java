package com.exasol.errorreporting;

class ParametersMapper {
    private final String text;
    private final Object[] parameters;
    private int parameterIndex;
    private final ErrorMessageBuilder errorMessageBuilder;

    /**
     * Format a given text with place holders, filling them with the arguments passed in the specified form.
     *
     * @param text       message with place holders
     * @param parameters arguments to fill the place holders
     * @return formatted message as String
     */
    static void mapParametersByName(final String text, final Object[] parameters,
            final ErrorMessageBuilder errorMessageBuilder) {
        new ParametersMapper(text, parameters, errorMessageBuilder).mapParameters();
    }

    private ParametersMapper(final String text, final Object[] arguments,
            final ErrorMessageBuilder errorMessageBuilder) {
        this.parameters = arguments;
        this.parameterIndex = 0;
        this.errorMessageBuilder = errorMessageBuilder;
        this.text = text;
    }

    void mapParameters() {
        final PlaceHolderIterator iterator = new PlaceHolderIterator(this.text);
        while (iterator.findNext()) {
            this.mapParameter(iterator.getPlaceHolder());
            this.moveToNextParameter();
        }
    }

    private void mapParameter(final PlaceHolder placeholder) {
        if (this.isParameterPresent()) {
            this.appendParameter(placeholder);
        }
    }

    private boolean isParameterPresent() {
        return (this.parameters != null) && (this.parameterIndex < this.parameters.length);
    }

    private void appendParameter(final PlaceHolder placeholder) {
        if (this.isNullParameter()) {
            this.appendNullParameter(placeholder.getName());
        } else {
            this.appendRegularParameter(placeholder);
        }
    }

    private boolean isNullParameter() {
        return this.isParameterPresent() && (this.getCurrentParameter() == null);
    }

    private void appendNullParameter(final String parameterName) {
        this.errorMessageBuilder.parameter(parameterName, null);
    }

    private void appendRegularParameter(final PlaceHolder placeholder) {
        if (placeholder.isUnquoted()) {
            this.appendUnquotedArgument(placeholder.getName());
        } else {
            this.appendQuotedArgument(placeholder.getName());
        }
    }

    private void appendUnquotedArgument(final String parameterName) {
        this.errorMessageBuilder.unquotedParameter(parameterName, this.getCurrentParameter());
    }

    private Object getCurrentParameter() {
        if (this.isParameterPresent()) {
            return this.parameters[this.parameterIndex];
        }
        return null;
    }

    private void appendQuotedArgument(final String parameterName) {
        this.errorMessageBuilder.parameter(parameterName, this.getCurrentParameter());
    }

    private void moveToNextParameter() {
        this.parameterIndex++;
    }
}
