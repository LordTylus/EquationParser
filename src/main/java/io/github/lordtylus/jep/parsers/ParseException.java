/*
  Copyright 2025 Martin Rökker

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;

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

    /**
     * Creates a new ParseException with the given cause
     *
     * @param cause cause of the exception during parsing.
     */
    public ParseException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new ParseException with the given message
     *
     * @param message reason of the exception during parsing.
     */
    public ParseException(String message) {
        super(message);
    }
}
