package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessagePatternProcessor {
    private final StringBuilder result;
    private int previousPlaceHolderEndPosition;
    private final String messagePattern;
    private final PlaceHolderIterator iterator;

    protected MessagePatternProcessor(final String messagePattern) {
        this.result = new StringBuilder();
        this.previousPlaceHolderEndPosition = 0;
        this.messagePattern = messagePattern;
        this.iterator = new PlaceHolderIterator(this.messagePattern);
    }

    public String replacePlaceholders() {
        while (this.iterator.findNext()) {
            this.appendSectionBeforePlaceHolder();
            this.processPlaceHolder(this.iterator.getPlaceHolder());
            this.moveToNextPlaceHolder();
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    protected abstract void processPlaceHolder(final String placeholder);

    private void appendSectionBeforePlaceHolder() {
        this.append(this.getSectionBeforePlaceHolder());
    }

    protected void append(final String text) {
        this.result.append(text);
    }

    private void appendRestOfTheMessage() {
        this.append(this.getRestOfTheMessage());
    }

    public String getSectionBeforePlaceHolder() {
        return this.messagePattern.substring(this.previousPlaceHolderEndPosition,
                this.iterator.getPlaceHolderStartPosition());
    }

    public String getRestOfTheMessage() {
        return this.messagePattern.substring(this.previousPlaceHolderEndPosition);
    }

    public void moveToNextPlaceHolder() {
        this.previousPlaceHolderEndPosition = this.iterator.getPlaceHolderEndPosition();
    }

    private String getFullMessage() {
        return this.result.toString();
    }

    private static class PlaceHolderIterator {
        private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
        private final Matcher matcher;
        private final String messagePattern;

        protected PlaceHolderIterator(final String messagePattern) {
            this.messagePattern = messagePattern;
            this.matcher = PLACEHOLDER_PATTERN.matcher(this.messagePattern);
        }

        public boolean findNext() {
            return this.matcher.find();
        }

        public String getPlaceHolder() {
            return this.matcher.group(1);
        }

        public int getPlaceHolderStartPosition() {
            return this.matcher.start();
        }

        public int getPlaceHolderEndPosition() {
            return this.matcher.end();
        }
    }
}
