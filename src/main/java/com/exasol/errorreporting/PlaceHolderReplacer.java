package com.exasol.errorreporting;

import java.util.Map;

class PlaceHolderReplacer {
    private final StringBuilder result;
    private final String text;
    private final PlaceHolderIterator iterator;
    private final Map<String, String> parameters;
    private int previousPlaceHolderEndPosition;

    /**
     * Replace the place holders, if any, of the passed text with the passed parameters.
     *
     * @param text       text with place holders
     * @param parameters parameters for replacing the place holders in the text
     * @return text with its place holders replaced by the parameters
     */
    static String replacePlaceHolders(final String text, final Map<String, String> parameters) {
        return new PlaceHolderReplacer(text, parameters).processPlaceHolders();
    }

    private PlaceHolderReplacer(final String text, final Map<String, String> parameters) {
        this.parameters = parameters;
        this.result = new StringBuilder();
        this.text = text;
        this.iterator = new PlaceHolderIterator(this.text);
        this.previousPlaceHolderEndPosition = 0;
    }

    private String processPlaceHolders() {
        while (this.iterator.findNext()) {
            this.appendSectionBeforePlaceHolder();
            this.processPlaceHolder();
            this.moveToNextPlaceHolder();
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    private void appendSectionBeforePlaceHolder() {
        this.append(this.getSectionBeforePlaceHolder());
    }

    private void processPlaceHolder() {
        this.append(resolvePlaceholder());
    }

    private void append(final String text) {
        this.result.append(text);
    }

    private void appendRestOfTheMessage() {
        this.append(this.getRestOfTheMessage());
    }

    private String getSectionBeforePlaceHolder() {
        return this.text.substring(this.previousPlaceHolderEndPosition, this.iterator.getPlaceHolderStartPosition());
    }

    private String getRestOfTheMessage() {
        return this.text.substring(this.previousPlaceHolderEndPosition);
    }

    private void moveToNextPlaceHolder() {
        this.previousPlaceHolderEndPosition = this.iterator.getPlaceHolderEndPosition();
    }

    private String getFullMessage() {
        return this.result.toString();
    }

    private String resolvePlaceholder() {
        final String placeHolderName = this.getPlaceHolderName();
        if (this.parameters.containsKey(placeHolderName)) {
            return this.parameters.get(placeHolderName);
        }
        return this.getUnknownPlaceHolderTextFor(placeHolderName);
    }

    private String getPlaceHolderName() {
        return this.iterator.getPlaceHolder().getName();
    }

    private String getUnknownPlaceHolderTextFor(final String placeholder) {
        return "UNKNOWN PLACEHOLDER('" + placeholder + "')";
    }
}
