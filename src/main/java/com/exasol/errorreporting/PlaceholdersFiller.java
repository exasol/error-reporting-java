package com.exasol.errorreporting;

import java.util.Map;

import com.exasol.errorreporting.PlaceholderIterator.Placeholder;

/**
 * Class for filling the placeholders of texts.
 */
class PlaceholdersFiller {
    private final StringBuilder result;
    private final String text;
    private final Map<String, String> parameters;
    private int previousPlaceholderEndPosition;

    /**
     * Fill the placeholders, if any, of the passed text with the passed parameters.
     *
     * @param text       text that may contain placeholders
     * @param parameters parameters to fill the placeholders in the passed text
     * @return text with its placeholders filled
     */
    static String fillPlaceholders(final String text, final Map<String, String> parameters) {
        return new PlaceholdersFiller(text, parameters).fillPlaceholders();
    }

    private PlaceholdersFiller(final String text, final Map<String, String> parameters) {
        this.parameters = parameters;
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
        return this.parameters.getOrDefault(placeholder.getName(), this.getUnknownPlaceholderTextFor(placeholder));
    }

    private String getUnknownPlaceholderTextFor(final Placeholder placeholder) {
        return "UNKNOWN PLACEHOLDER('" + placeholder.getName() + "')";
    }
}
