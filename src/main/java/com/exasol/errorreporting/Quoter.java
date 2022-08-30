package com.exasol.errorreporting;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class quotes objects for the use in error messages.
 */
class Quoter {

    private Quoter() {
        // empty on purpose
    }

    /**
     * Return a quoted string representation of the objectToQuote passed.
     * 
     * @param object object to quote
     * @return quoted object
     */
    static String quoteObject(final Object object) {
        if (object == null) {
            return "<null>";
        } else if (object instanceof String || object instanceof Character || object instanceof java.nio.file.Path
                || object instanceof java.io.File || object instanceof java.net.URL || object instanceof java.net.URI) {
            return "'" + object + "'";
        } else if (object instanceof List) {
            final List<?> list = (List<?>) object;
            return "[" + list.stream().map(Quoter::quoteObject).collect(Collectors.joining(", ")) + "]";
        } else {
            return object.toString();
        }
    }
}
