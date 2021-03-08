package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderIterator {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final Matcher matcher;
    private int placeholderEndPosition;
    private final String messagePattern;

    protected PlaceHolderIterator(final String messagePattern) {
        this.messagePattern = messagePattern;
        this.placeholderEndPosition = 0;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.messagePattern);
    }

    public String getNextPlaceHolder() {
        if (this.findNext()) {
            return this.getCurrentPlaceHolder();
        }
        return null;
    }

    private boolean findNext() {
        return this.matcher.find();
    }

    protected String getCurrentPlaceHolder() {
        return this.matcher.group(1);
    }

    public String getSectionBeforePlaceHolder() {
        return this.messagePattern.substring(this.placeholderEndPosition, this.matcher.start());
    }

    private void appendRestOfTheMessage() {
//        this.append(this.messagePattern.substring(this.placeholderEndPosition));
    }

    public void moveToNextPlaceHolder() {
        this.placeholderEndPosition = this.matcher.end();
    }
}
