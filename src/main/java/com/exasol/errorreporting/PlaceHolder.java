package com.exasol.errorreporting;

class PlaceHolder {
    private final String name;
    private final boolean isUnquoted;
    private final int startPositionInText;
    private final int endPositionInText;

    PlaceHolder(final String name, final boolean isUnquoted, final int startPositionInText,
            final int endPositionInText) {
        this.name = name;
        this.isUnquoted = isUnquoted;
        this.startPositionInText = startPositionInText;
        this.endPositionInText = endPositionInText;
    }

    String getName() {
        return this.name;
    }

    boolean isUnquoted() {
        return this.isUnquoted;
    }

    int getStartPositionInText() {
        return this.startPositionInText;
    }

    int getEndPositionInText() {
        return this.endPositionInText;
    }
}