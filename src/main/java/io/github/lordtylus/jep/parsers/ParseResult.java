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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Optional;

/**
 * This class represents the result of parsing an equation.
 * It either can be successful which means an equation can be received from this class,
 * or it can be an error in which case an error message is provided.
 * <p>
 * This class is used by the parsers to also represent if they had anything to parse
 * from the passed in token list. In case the string was not erroneous but a parser could not really
 * do anything with it, it will return with {@link ParseType#NOT_MINE}
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParseResult {

    private static final ParseResult NOT_MINE = new ParseResult(null, null, ParseType.NOT_MINE);

    Equation equation;
    String errorMessage;

    @Getter
    ParseType parseType;

    /**
     * This method is used to either return the result of the entire parsing process,
     * or can be used by parsers to pass a partial parsing result upwards to the caller.
     * <p>
     * Such as an operation will need to parse its operants first.
     *
     * @return an optional with the parsed equation if parsing was successful
     */
    public Optional<Equation> getEquation() {
        return Optional.of(equation);
    }

    /**
     * This method is just for internal use of the parsers, to grant access to the
     * equation without wrapping it in an optional first.
     * <p>
     * The result therefore can be null.
     *
     * @return The parsed equation or null, if there wasn't any.
     */
    Equation getNullableEquation() {
        return equation;
    }

    /**
     * This is a factory method to create a new result containing the parsed equation.
     *
     * @param equation The successfully parsed equation.
     * @return the new Result
     */
    public static ParseResult ok(Equation equation) {
        return new ParseResult(equation, null, ParseType.OK);
    }

    /**
     * This is a factory method to create a new result containing an error message on failed parsing.
     *
     * @param reason the error message encountered while parsing.
     * @return the new Result
     */
    public static ParseResult error(String reason) {
        return new ParseResult(null, reason, ParseType.ERROR);
    }

    /**
     * This method is used by the parsers if it determined not to be the right parser
     * to parse this part of a string. That signals the {@link EquationParser} that the next
     * parser in line should be consulted.
     *
     * @return a singleton result signalling {@link ParseType#NOT_MINE}
     */
    public static ParseResult notMine() {
        return NOT_MINE;
    }

    /**
     * This enum represents the type of the Parse Result.
     */
    public enum ParseType {

        /**
         * Ok means success, and it is expected that the parse result contains a non-null {@link Equation}.
         */
        OK,
        /**
         * Error means that parsing failed, the Equation will be null, and an error message be provided.
         */
        ERROR,
        /**
         * Not mine is used by parsers to signalize they were unable to parse the string because they don*t match their contract. Meaning, the next parser in line should try its luck.
         */
        NOT_MINE
    }
}
