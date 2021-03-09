package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An iterator over a text that may contain place holders.
 *
 */
class PlaceHolderIterator {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final Matcher matcher;
    private final String text;

    /**
     * Create a new instance of {@link PlaceHolderIterator}
     *
     * @param text containing place holders to iterate
     */
    PlaceHolderIterator(final String text) {
        this.text = text;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.text);
    }

    /**
     * @return true if there's a next place holder. If so, it moves the position forward one step
     */
    boolean findNext() {
        return this.matcher.find();
    }

    /**
     * @return the current {@link PlaceHolder}
     */
    PlaceHolder getPlaceHolder() {
        return new PlaceHolder(this.matcher.group(1), this.matcher.start(), this.matcher.end());
    }

    /**
     * A place holder.
     */
    static class PlaceHolder {
        private static final String UNQUOTED_SUFFIX = "|uq";
        private final String text;
        private final int startPosition;
        private final int endPosition;

        /**
         * Create a new instance of {@link PlaceHolder}
         *
         * @param text          the place holder text
         * @param startPosition
         * @param endPosition
         */
        PlaceHolder(final String text, final int startPosition, final int endPosition) {
            this.text = text;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        /**
         * @return the place holder name
         */
        String getName() {
            if (this.isUnquoted()) {
                return this.parseUnquotedPlaceHolderName();
            }
            return this.text;
        }

        private String parseUnquotedPlaceHolderName() {
            return this.text.substring(0, this.text.length() - UNQUOTED_SUFFIX.length());
        }

        /**
         * @return true if the place holder is unquoted
         */
        boolean isUnquoted() {
            return this.text.endsWith(UNQUOTED_SUFFIX);
        }

        /**
         * @return the place holder start position in the text
         */
        int getStartPosition() {
            return this.startPosition;
        }

        /**
         * @return the place holder end position in the text
         */
        int getEndPosition() {
            return this.endPosition;
        }
    }
}