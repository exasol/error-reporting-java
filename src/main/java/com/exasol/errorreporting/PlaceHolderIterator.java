package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderIterator {
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
