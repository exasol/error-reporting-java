package com.exasol.errorreporting;

/**
 * A placeholder.
 */
public class Placeholder {
    private final String reference;
    private final int startIndex;
    private final int endIndex;
    private final Quoting quoting;

    /**
     * Parse a placeholder from a placeholder string.
     * <p>
     * Placeholders consist of a reference part and a part that contains switches that control how the placeholder is treated. This
     * parser takes the string representation and parses it into placeholder information.
     * </p>
     * <p>
     * This method returns a builder for a placeholder, because a complete placeholder has additional attributes that
     * come from outside of the parsed text.
     * </p>
     *
     * @param placeholderString string representation of the placeholder
     * @return placeholder builder, pre-initialized with the results of parsing
     */
    public static Builder parse(final String placeholderString) {
        final int separatorIndex = placeholderString.indexOf('|');
        if(separatorIndex >= 0) {
            final String reference = placeholderString.substring(0, separatorIndex);
            Placeholder.Builder builder = new Placeholder.Builder(reference);
            final String switches = placeholderString.substring(separatorIndex + 1);
            if(switches.contains("u")) {
                builder.quoting(Quoting.UNQUOTED);
            } else if(switches.contains("q")) {
                builder.quoting(Quoting.SINGLE_QUOTES);
            } else if(switches.contains("d")) {
                builder.quoting(Quoting.DOUBLE_QUOTES);
            } else {
                builder.quoting(Quoting.AUTOMATIC);
            }
            return builder;
        } else {
            return new Builder(placeholderString).quoting(Quoting.AUTOMATIC);
        }
    }

    private Placeholder(final Builder builder) {
        this.reference = builder.reference;
        this.startIndex = builder.startIndex;
        this.endIndex = builder.endIndex;
        this.quoting = builder.quoting;
    }

    /**
     * Get the reference that points to the parameter that is used to replace this placeholder.
     *
     * @return reference to parameter
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Get the quoting style used when replacing this placeholder.
     *
     * @return quoting style
     */
    public Quoting getQuoting() {
        return this.quoting;
    }

    /**
     * Get the index at which the placeholder starts in the text it is embedded in.
     *
     * @return placeholder start index in the text.
     */
    public int getStartIndex() {
        return this.startIndex;
    }

    /**
     * Get the index at which the placeholder ends in the text it is embedded in.
     *
     * @return placeholder end index in the text.
     */
    public int getEndIndex() {
        return this.endIndex;
    }

    @Override
    public String toString() {
        return "Placeholder [reference=" + this.reference + ", startIndex=" + this.startIndex + ", endIndex="
                + this.endIndex + ", quoting="+ this.quoting + "]";
    }

    /**
     * Builder for a {@link Placeholder}.
     */
    public static final class Builder {
        private final String reference;
        private int startIndex;
        private int endIndex;
        private Quoting quoting;

        /**
         * Create a new builder for a {@link Placeholder}.
         * @param reference reference that points to a parameter
         */
        public Builder(final String reference) {
            this.reference = reference;
        }

        /**
         * Build a new instance of a {@link Placeholder}.
         *
         * @return new placeholder instance
         */
        public Placeholder build() {
            return new Placeholder(this);
        }

        /**
         * Set the index at which the placeholder starts in the text it is embedded in.
         *
         * @param startIndex start index
         * @return self for fluent programming
         */
        public Builder startIndex(final int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        /**
         * Set the index at which the placeholder ends in the text it is embedded in.
         *
         * @param endIndex end index
         * @return self for fluent programming
         */
        public Builder endIndex(final int endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        /**
         * Set the quoting style that should be used when replacing this placeholder.
         *
         * @param quoting quoting style
         * @return self for fluent programming
         */
        public Builder quoting(final Quoting quoting) {
            this.quoting = quoting;
            return this;
        }
    }
}
