package com.exasol.errorreporting;

import com.exasol.errorreporting.PlaceHolderIterator.PlaceHolder;

/**
 * Class for parsing a text with place holders and setting the right parameter mapping in an {@link ErrorMessageBuilder}
 */
class ParametersMapper {
    private final String text;
    private final Object[] parameters;
    private int parameterIndex;
    private final ErrorMessageBuilder errorMessageBuilder;

    /**
     * Given a text that may contain place holders, and an array of parameters, and an {@link ErrorMessageBuilder},
     * generates a map with the place holders and the parameters by adding them to the {@link ErrorMessageBuilder}
     *
     * @param text                text that may contain place holders
     * @param parameters          parameters to be mapped
     * @param errorMessageBuilder {@link ErrorMessageBuilder} that holds the mapped parameters
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

    private void mapParameters() {
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
