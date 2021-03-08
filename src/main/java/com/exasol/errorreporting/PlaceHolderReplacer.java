package com.exasol.errorreporting;

import java.util.Map;

public class PlaceHolderReplacer extends MessagePatternProcessor {
    private final Map<String, String> parameterMapping;

    public static String replacePlaceHolders(final String messagePattern, final Map<String, String> parameterMapping) {
        return new PlaceHolderReplacer(messagePattern, parameterMapping).replacePlaceholders();
    }

    public PlaceHolderReplacer(final String messagePattern, final Map<String, String> parameterMapping) {
        super(messagePattern);
        this.parameterMapping = parameterMapping;
    }

    @Override
    protected void processPlaceHolder(final String placeholder) {
        this.append(resolvePlaceholder(placeholder));
    }

    private String resolvePlaceholder(final String placeholder) {
        if (this.parameterMapping.containsKey(placeholder)) {
            return this.parameterMapping.get(placeholder);
        } else {
            return "UNKNOWN PLACEHOLDER('" + placeholder + "')";
        }
    }
}
