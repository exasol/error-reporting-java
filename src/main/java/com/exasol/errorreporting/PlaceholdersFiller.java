package com.exasol.errorreporting;

/**
 * This class replaces the placeholders in a text.
 */
class PlaceholdersFiller {
    private final StringBuilder result;
    private final String text;
    private final ParameterDefinitionList parameters;
    private int previousPlaceholderEndPosition;

    /**
     * Fill the placeholders, if any, of the passed text with the passed parameters.
     *
     * @param text               text that may contain placeholders
     * @param parameters         parameters to fill the placeholders in the text passed
     * @return text with its placeholders filled
     */
    static String fillPlaceholders(final String text, final ParameterDefinitionList parameters) {
        return new PlaceholdersFiller(text, parameters).fillPlaceholders();
    }

    private PlaceholdersFiller(final String text, final ParameterDefinitionList parameters) {
        this.parameters = parameters;
        this.result = new StringBuilder();
        this.text = text;
        this.previousPlaceholderEndPosition = 0;
    }

    private String fillPlaceholders() {
        final Iterable<Placeholder> placeholders = PlaceholderMatcher.findPlaceholders(this.text);
        for (final Placeholder placeholder : placeholders) {
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
        return this.text.substring(this.previousPlaceholderEndPosition, placeholder.getStartIndex());
    }

    private String getRestOfTheMessage() {
        return this.text.substring(this.previousPlaceholderEndPosition);
    }

    private void moveToNextPlaceholder(final Placeholder placeholder) {
        this.previousPlaceholderEndPosition = placeholder.getEndIndex();
    }

    private String getFullMessage() {
        return this.result.toString();
    }

    private String getPlaceholderFilling(final Placeholder placeholder) {
        if (this.isParameterPresent(placeholder)) {
            return getPresentParameterPlaceholderFilling(placeholder);
        } else {
            return getUnknownPlaceholderTextFor(placeholder);
        }
    }

    private boolean isParameterPresent(final Placeholder placeholder) {
        return this.parameters.containsKey(placeholder.getReference());
    }

    private String getPresentParameterPlaceholderFilling(final Placeholder placeholder) {
        return quoteParameterValue(placeholder);
    }

    private String quoteParameterValue(final Placeholder placeholder) {
        final ParameterDefinition parameter = this.parameters.get(placeholder.getReference());
        return Quoter.quoteObject(parameter.getValue(), placeholder.getQuoting());
    }

    private String getUnknownPlaceholderTextFor(final Placeholder placeholder) {
        return "UNKNOWN PLACEHOLDER('" + placeholder.getReference() + "')";
    }
}