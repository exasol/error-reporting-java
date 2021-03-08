package com.exasol.errorreporting;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderReplacer {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final Matcher matcher;
    private final String messagePattern;
    private final StringBuilder resultBuilder = new StringBuilder();
    private int placeholderEndPosition = 0;
    private final Map<String, String> parameterMapping;

    public static String replacePlaceHolders(final String messagePattern, final Map<String, String> parameterMapping) {
        return new PlaceHolderReplacer(messagePattern, parameterMapping).replacePlaceholders();
    }

    private PlaceHolderReplacer(final String messagePattern, final Map<String, String> parameterMapping) {
        this.messagePattern = messagePattern;
        this.parameterMapping = parameterMapping;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.messagePattern);
    }

    public String replacePlaceholders() {
        while (this.isPlaceHolderFound()) {
            this.processPlaceHolder();
            this.moveToNextPlaceHolder();
        }
        this.appendRestOfTheMessage();
        return this.getFullMessage();
    }

    private void processPlaceHolder() {
        this.appendSectionBeforePlaceHolder();
        final String placeholder = this.getCurrentPlaceHolder();
        this.resultBuilder.append(resolvePlaceholder(placeholder));
    }

    private boolean isPlaceHolderFound() {
        return this.matcher.find();
    }

    private String getCurrentPlaceHolder() {
        return this.matcher.group(1);
    }

    private void appendSectionBeforePlaceHolder() {
        this.resultBuilder.append(this.getSectionBeforePlaceHolder());
    }

    private String getSectionBeforePlaceHolder() {
        return this.messagePattern.substring(this.placeholderEndPosition, this.matcher.start());
    }

    private void appendRestOfTheMessage() {
        this.resultBuilder.append(this.messagePattern.substring(this.placeholderEndPosition));
    }

    private void moveToNextPlaceHolder() {
        this.placeholderEndPosition = this.matcher.end();
    }

    private String getFullMessage() {
        return this.resultBuilder.toString();
    }

    private String resolvePlaceholder(final String placeholder) {
        if (this.parameterMapping.containsKey(placeholder)) {
            return this.parameterMapping.get(placeholder);
        } else {
            return "UNKNOWN PLACEHOLDER('" + placeholder + "')";
        }
    }
}
