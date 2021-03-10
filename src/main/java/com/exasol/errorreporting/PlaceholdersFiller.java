package com.exasol.errorreporting;

import java.util.Map;

import com.exasol.errorreporting.PlaceholderIterator.Placeholder;

/**
 * Class for filling the placeholders of texts.
 */
class PlaceholdersFiller {
    private final StringBuilder result;
    private final String text;
    private final Map<String, Object> parameters;
    private final Map<String, Object> explicitlyUnquotedParameters;
    private int previousPlaceholderEndPosition;

    /**
     * Fill the placeholders, if any, of the passed text with the passed parameters.
     *
     * @param text                         text that may contain placeholders
     * @param parameters                   parameters to fill the placeholders in the passed text
     * @param explicitlyUnquotedParameters a map with those parameters that should be unquoted, and where defined by
     *                                     calling {@link ErrorMessageBuilder#unquotedParameter(String, Object)}
     * @return text with its placeholders filled
     */
    static String fillPlaceholders(final String text, final Map<String, Object> parameters,
            final Map<String, Object> unquotedParameters) {
        return new PlaceholdersFiller(text, parameters, unquotedParameters).fillPlaceholders();
    }

    private PlaceholdersFiller(final String text, final Map<String, Object> parameters,
            final Map<String, Object> explicitlyUnquotedParameters) {
        this.parameters = parameters;
        this.explicitlyUnquotedParameters = explicitlyUnquotedParameters;
        this.result = new StringBuilder();
        this.text = text;
        this.previousPlaceholderEndPosition = 0;
    }

    private String fillPlaceholders() {
        final PlaceholderIterator iterator = new PlaceholderIterator(this.text);
        while (iterator.findNext()) {
            final Placeholder placeholder = iterator.getPlaceholder();
            this.appendSectionBeforePlaceholder(placeholder);
            this.fillPlaceholder(placeholder);
            this.moveToNextPlaceholder(placeholder);
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    private void appendSectionBeforePlaceholder(final Placeholder placeholder) {
        this.append(this.getSectionBeforePlaceholder(placeholder));
    }

    private void fillPlaceholder(final Placeholder placeholder) {
        this.append(this.getPlaceholderFilling(placeholder));
    }

    private void append(final String text) {
        this.result.append(text);
    }

    private void appendRestOfTheMessage() {
        this.append(this.getRestOfTheMessage());
    }

    private String getSectionBeforePlaceholder(final Placeholder placeholder) {
        return this.text.substring(this.previousPlaceholderEndPosition, placeholder.getStartPosition());
    }

    private String getRestOfTheMessage() {
        return this.text.substring(this.previousPlaceholderEndPosition);
    }

    private void moveToNextPlaceholder(final Placeholder placeholder) {
        this.previousPlaceholderEndPosition = placeholder.getEndPosition();
    }

    private String getFullMessage() {
        return this.result.toString();
    }

    private String getPlaceholderFilling(final Placeholder placeholder) {
        if (this.isParameterPresent(placeholder)) {
            return this.getPresentParameterPlaceholderFilling(placeholder);
        }
        return this.getUnknownPlaceholderTextFor(placeholder);
    }

    private boolean isParameterPresent(final Placeholder placeholder) {
        return this.parameters.containsKey(placeholder.getName());
    }

    private String getPresentParameterPlaceholderFilling(final Placeholder placeholder) {
        if (this.isNullParameter(placeholder)) {
            return "<null>";
        }
        if (this.isUnquotedParameter(placeholder)) {
            return this.parameters.get(placeholder.getName()).toString();
        }
        return this.quoteParameter(placeholder);
    }

    private boolean isUnquotedParameter(final Placeholder placeholder) {
        return placeholder.isUnquoted() || this.explicitlyUnquotedParameters.containsKey(placeholder.getName());
    }

    private String quoteParameter(final Placeholder placeholder) {
        return Quoter.quoteObject(this.parameters.get(placeholder.getName()));
    }

    private boolean isNullParameter(final Placeholder placeholder) {
        return this.parameters.get(placeholder.getName()) == null;
    }

    private String getUnknownPlaceholderTextFor(final Placeholder placeholder) {
        return "UNKNOWN PLACEHOLDER('" + placeholder.getName() + "')";
    }
}