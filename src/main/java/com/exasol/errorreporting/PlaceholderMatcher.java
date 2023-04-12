package com.exasol.errorreporting;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An iterable for placeholder in a given text.
 *
 */
public class PlaceholderMatcher implements Iterable<Placeholder> {
    private final String text;

    private PlaceholderMatcher(final String text) {
        this.text = text;
    }

    /**
     * Find placeholders in a given string.
     * 
     * @param text string containing placeholders
     * @return iterable for placeholders
     */
    public static Iterable<Placeholder> findPlaceholders(final String text) {
        return new PlaceholderMatcher(text);
    }

    @Override
    public Iterator<Placeholder> iterator() {
        return new PlaceholderIterator(this.text);
    }

    private static class PlaceholderIterator implements Iterator<Placeholder> {
        private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([^}]*)}+");
        private final Matcher matcher;
        private boolean hasNext;

        /**
         * Create a new instance of {@link PlaceholderMatcher}.
         *
         * @param text containing placeholders to iterate.
         */
        private PlaceholderIterator(final String text) {
            this.matcher = PLACEHOLDER_PATTERN.matcher(text);
            this.hasNext = this.matcher.find();
        }

        /**
         * Get the current placeholder.
         *
         * @return current {@link Placeholder}.
         */
        private Placeholder getPlaceholder() {
            final String placeholderString = this.matcher.group(1);
            final Placeholder.Builder builder = Placeholder.parse(placeholderString);
            return builder.startIndex(this.matcher.start()).endIndex(this.matcher.end()).build();
        }

        @Override
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override
        public Placeholder next() {
            if (this.hasNext()) {
                final Placeholder placeholder = getPlaceholder();
                this.hasNext = this.matcher.find();
                return placeholder;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
