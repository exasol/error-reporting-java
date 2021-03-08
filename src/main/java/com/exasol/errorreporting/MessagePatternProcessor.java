package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessagePatternProcessor {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final Matcher matcher;
    private int placeholderEndPosition;
    private final String messagePattern;
    private final StringBuilder result;

    protected MessagePatternProcessor(final String messagePattern) {
        this.messagePattern = messagePattern;
        this.result = new StringBuilder();
        this.placeholderEndPosition = 0;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.messagePattern);
    }

    public String replacePlaceholders() {
        while (this.isPlaceHolderFound()) {
            this.appendSectionBeforePlaceHolder();
            this.processPlaceHolder(this.getCurrentPlaceHolder());
            this.moveToNextPlaceHolder();
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    protected abstract void processPlaceHolder(final String placeholder);

    private boolean isPlaceHolderFound() {
        return this.matcher.find();
    }

    protected String getCurrentPlaceHolder() {
        return this.matcher.group(1);
    }

    private void appendSectionBeforePlaceHolder() {
        this.append(this.getSectionBeforePlaceHolder());
    }

    private String getSectionBeforePlaceHolder() {
        return this.messagePattern.substring(this.placeholderEndPosition, this.matcher.start());
    }

    protected void append(final String text) {
        this.result.append(text);
    }

    private void appendRestOfTheMessage() {
        this.append(this.messagePattern.substring(this.placeholderEndPosition));
    }

    private void moveToNextPlaceHolder() {
        this.placeholderEndPosition = this.matcher.end();
    }

    private String getFullMessage() {
        return this.result.toString();
    }
}
