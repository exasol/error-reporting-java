package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderIterator {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private static final String UNQUOTED_SUFFIX = "|uq";
    private final Matcher matcher;
    private final String text;

    protected PlaceHolderIterator(final String text) {
        this.text = text;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.text);
    }

    public boolean findNext() {
        return this.matcher.find();
    }

    public PlaceHolder getPlaceHolder() {
        final String placeHolder = this.matcher.group(1);
        return new PlaceHolder(parserParameterNameFrom(placeHolder), this.isUnquotedParameter(placeHolder));
    }

    private String parserParameterNameFrom(final String placeholder) {
        if (this.isUnquotedParameter(placeholder)) {
            return this.parseUnquotedPlaceHolderNameFrom(placeholder);
        }
        return placeholder;
    }

    private String parseUnquotedPlaceHolderNameFrom(final String placeholder) {
        return placeholder.substring(0, placeholder.length() - UNQUOTED_SUFFIX.length());
    }

    private boolean isUnquotedParameter(final String placeholder) {
        return placeholder.endsWith(UNQUOTED_SUFFIX);
    }

    public int getPlaceHolderStartPosition() {
        return this.matcher.start();
    }

    public int getPlaceHolderEndPosition() {
        return this.matcher.end();
    }
}