package com.exasol.errorreporting;

/**
 * This enumeration contains options for quoting parameter values.
 */
public enum Quoting {
    /** Automatic quoting based on the type of the parameter value */
    AUTOMATIC,
    /** Forced single quotes */
    SINGLE_QUOTES,
    /** Forced double quotes */
    DOUBLE_QUOTES,
    /** Forced unquoted */
    UNQUOTED
}
