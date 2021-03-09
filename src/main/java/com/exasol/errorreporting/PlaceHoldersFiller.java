package com.exasol.errorreporting;

import java.util.Map;

import com.exasol.errorreporting.PlaceHolderIterator.PlaceHolder;

/**
 * Class for filling the place holders of texts.
 */
class PlaceHoldersFiller {
    private final StringBuilder result;
    private final String text;
    private final Map<String, String> parameters;
    private int previousPlaceHolderEndPosition;

    /**
     * Fill the place holders, if any, of the passed text with the passed parameters.
     *
     * @param text       text that may contain place holders
     * @param parameters parameters to fill the place holders in the passed text
     * @return text with its place holders filled
     */
    static String fillPlaceHolders(final String text, final Map<String, String> parameters) {
        return new PlaceHoldersFiller(text, parameters).fillPlaceHolders();
    }

    private PlaceHoldersFiller(final String text, final Map<String, String> parameters) {
        this.parameters = parameters;
        this.result = new StringBuilder();
        this.text = text;
        this.previousPlaceHolderEndPosition = 0;
    }

    private String fillPlaceHolders() {
        final PlaceHolderIterator iterator = new PlaceHolderIterator(this.text);
        while (iterator.findNext()) {
            final PlaceHolder placeHolder = iterator.getPlaceHolder();
            this.appendSectionBeforePlaceHolder(placeHolder);
            this.fillPlaceHolder(placeHolder);
            this.moveToNextPlaceHolder(placeHolder);
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    private void appendSectionBeforePlaceHolder(final PlaceHolder placeHolder) {
        this.append(this.getSectionBeforePlaceHolder(placeHolder));
    }

    private void fillPlaceHolder(final PlaceHolder placeHolder) {
        this.append(this.getPlaceHolderFilling(placeHolder));
    }

    private void append(final String text) {
        this.result.append(text);
    }

    private void appendRestOfTheMessage() {
        this.append(this.getRestOfTheMessage());
    }

    private String getSectionBeforePlaceHolder(final PlaceHolder placeHolder) {
        return this.text.substring(this.previousPlaceHolderEndPosition, placeHolder.getStartPosition());
    }

    private String getRestOfTheMessage() {
        return this.text.substring(this.previousPlaceHolderEndPosition);
    }

    private void moveToNextPlaceHolder(final PlaceHolder placeHolder) {
        this.previousPlaceHolderEndPosition = placeHolder.getEndPosition();
    }

    private String getFullMessage() {
        return this.result.toString();
    }

    private String getPlaceHolderFilling(final PlaceHolder placeHolder) {
        return this.parameters.getOrDefault(placeHolder.getName(), this.getUnknownPlaceHolderTextFor(placeHolder));
    }

    private String getUnknownPlaceHolderTextFor(final PlaceHolder placeholder) {
        return "UNKNOWN PLACEHOLDER('" + placeholder.getName() + "')";
    }
}
