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
     * @param objectToQuote object to quote
     * @return quoted object
     */
    static String quoteObject(final Object objectToQuote) {
        if (objectToQuote == null) {
            return "<null>";
        } else if (objectToQuote instanceof String || objectToQuote instanceof Character) {
            return "'" + objectToQuote.toString() + "'";
        } else if (objectToQuote instanceof List) {
            final List<?> list = (List<?>) objectToQuote;
            return "[" + list.stream().map(Quoter::quoteObject).collect(Collectors.joining(", ")) + "]";
        } else {
            return objectToQuote.toString();
        }
    }
}
