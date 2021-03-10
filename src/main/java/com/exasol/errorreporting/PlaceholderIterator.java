package com.exasol.errorreporting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An iterator over a text that may contain placeholders.
 *
 */
class PlaceholderIterator {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
    private final Matcher matcher;
    private final String text;

    /**
     * Create a new instance of {@link PlaceholderIterator}.
     *
     * @param text containing placeholders to iterate.
     */
    PlaceholderIterator(final String text) {
        this.text = text;
        this.matcher = PLACEHOLDER_PATTERN.matcher(this.text);
    }

    /**
     * @return true if there's a next placeholder. If so, it moves the position forward one step.
     */
    boolean findNext() {
        return this.matcher.find();
    }

    /**
     * @return the current {@link Placeholder}.
     */
    Placeholder getPlaceholder() {
        return new Placeholder(this.matcher.group(1), this.matcher.start(), this.matcher.end());
    }

    /**
     * A placeholder.
     */
    static class Placeholder {
        private static final String UNQUOTED_SUFFIX = "|uq";
        private final String text;
        private final int startPosition;
        private final int endPosition;

        /**
         * Create a new instance of {@link Placeholder}.
         *
         * @param text          the placeholder text
         * @param startPosition
         * @param endPosition
         */
        Placeholder(final String text, final int startPosition, final int endPosition) {
            this.text = text;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        /**
         * @return the placeholder name.
         */
        String getName() {
            if (this.isUnquoted()) {
                return this.parseUnquotedPlaceholderName();
            }
            return this.text;
        }

        private String parseUnquotedPlaceholderName() {
            return this.text.substring(0, this.text.length() - UNQUOTED_SUFFIX.length());
        }

        /**
         * @return true if the placeholder is unquoted.
         */
        boolean isUnquoted() {
            return this.text.endsWith(UNQUOTED_SUFFIX);
        }

        /**
         * @return the placeholder start position in the text.
         */
        int getStartPosition() {
            return this.startPosition;
        }

        /**
         * @return the placeholder end position in the text.
         */
        int getEndPosition() {
            return this.endPosition;
        }

        @Override
        public String toString() {
            return "Placeholder [text=" + this.text + ", startPosition=" + this.startPosition + ", endPosition="
                    + this.endPosition + "]";
        }
    }
}
