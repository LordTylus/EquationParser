package io.lordtylus.equation.parsers;

import io.lordtylus.Equation;

/**
 * A ParseException is thrown during parsing of an {@link Equation}
 * if something unexpected happens.
 * <p>
 * Parsers are supposed to just return an empty optional if they cannot parse a given string,
 * if a string should be parsable yet an edge case occurs the parser cannot handle this exception
 * should be thrown instead.
 * <p>
 * Long story short: Normally nobody should ever encounter this exception, and its only purpose is
 * to signal that something went horribly wrong indicating a bug within the parsers.
 */
public class ParseException extends RuntimeException {

    public ParseException(Throwable cause) {
        super(cause);
    }
}
