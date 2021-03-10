package com.exasol.errorreporting;

/**
 * Class for parsing a text with placeholders and setting the right parameter mapping in an {@link ErrorMessageBuilder}.
 */
class ParametersMapper {
    private final String text;
    private final Object[] parameters;
    private int parameterIndex;
    private final ErrorMessageBuilder errorMessageBuilder;

    /**
     * Given a text that may contain placeholders, and an array of parameters, and an {@link ErrorMessageBuilder},
     * generates a map with the placeholders and the parameters by adding them to the {@link ErrorMessageBuilder}.
     *
     * @param text                text that may contain placeholders
     * @param parameters          parameters to be mapped
     * @param errorMessageBuilder {@link ErrorMessageBuilder} to add the parameters to
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
        final Iterable<Placeholder> placeholders = PlaceholderMatcher.findPlaceholders(this.text);
        for (final Placeholder placeholder : placeholders) {
            this.mapParameter(placeholder);
            this.moveToNextParameter();
        }
    }

    private void mapParameter(final Placeholder placeholder) {
        if (this.isParameterPresent()) {
            this.appendParameter(placeholder);
        }
    }

    private boolean isParameterPresent() {
        return (this.parameters != null) && (this.parameterIndex < this.parameters.length);
    }

    private void appendParameter(final Placeholder placeholder) {
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

    private void appendRegularParameter(final Placeholder placeholder) {
        this.errorMessageBuilder.parameter(placeholder.getName(), this.getCurrentParameter());
    }

    private Object getCurrentParameter() {
        if (this.isParameterPresent()) {
            return this.parameters[this.parameterIndex];
        } else {
            return null;
        }
    }

    private void moveToNextParameter() {
        this.parameterIndex++;
    }
}
