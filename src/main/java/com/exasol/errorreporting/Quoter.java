package com.exasol.errorreporting;

import java.util.Collection;
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
     * @param object  object to quote
     * @param quoting quoting style to be used
     * @return quoted object
     */
    // [impl->dsn~apply-scalar-quoting~1]
    // [impl->dsn~apply-collection-quoting~1]
    static String quoteObject(final Object object, final Quoting quoting) {
        if (object == null) {
            return "<null>";
        } else if (object instanceof Collection) {
            final Collection<?> collection = (Collection<?>) object;
            return "[" + collection.stream().map(item -> quoteObject(item, quoting))
                    .collect(Collectors.joining(", ")) + "]";
        } else {
            switch (quoting) {
            case SINGLE_QUOTES:
                return "'" + object + "'";
            case DOUBLE_QUOTES:
                return "\"" + object + "\"";
            case UNQUOTED:
                return object.toString();
            default:
                if (object instanceof String || object instanceof Character || object instanceof java.nio.file.Path
                        || object instanceof java.io.File || object instanceof java.net.URL
                        || object instanceof java.net.URI) {
                    return "'" + object + "'";
                }
                else {
                    return object.toString();
                }
            }
        }
    }
}
