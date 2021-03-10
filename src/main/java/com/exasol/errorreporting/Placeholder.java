package com.exasol.errorreporting;

/**
 * A placeholder.
 */
public class Placeholder {
    private static final String UNQUOTED_SUFFIX = "|uq";
    private final String text;
    private final int startPosition;
    private final int endPosition;

    /**
     * Create a new instance of {@link Placeholder}.
     *
     * @param text          the placeholder text
     * @param startPosition position of first character of the placeholder
     * @param endPosition   position of last character of the placeholder
     */
    public Placeholder(final String text, final int startPosition, final int endPosition) {
        this.text = text;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * @return the placeholder name.
     */
    public String getName() {
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
    public boolean isUnquoted() {
        return this.text.endsWith(UNQUOTED_SUFFIX);
    }

    /**
     * @return the placeholder start position in the text.
     */
    public int getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return the placeholder end position in the text.
     */
    public int getEndPosition() {
        return this.endPosition;
    }

    @Override
    public String toString() {
        return "Placeholder [text=" + this.text + ", startPosition=" + this.startPosition + ", endPosition="
                + this.endPosition + "]";
    }
}
