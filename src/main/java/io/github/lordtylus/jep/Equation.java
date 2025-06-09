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
package io.github.lordtylus.jep;

import io.github.lordtylus.jep.equation.Variable;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.options.ParsingOptions.ErrorBehavior;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.parsers.ParseException;
import io.github.lordtylus.jep.parsers.ParseResult;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import io.github.lordtylus.jep.storages.EmptyStorage;
import io.github.lordtylus.jep.tokenizer.EquationStringTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import lombok.NonNull;

import java.util.List;
import java.util.Locale;

/**
 * This object represents a parsed equation string. Such as 2*(2+3)^[x]
 * <p>
 * The parsed equation will (usually) represent a binary tree and can be solved using the {@link #evaluate(Storage)} method.
 * However, the implementation of this interface could also have more than two children, if a custom implementation doing so is provided.
 * <p>
 * The tree for the example equation will look like this:
 * <pre>
 *     *
 *    / \
 *   2   ^
 *      / \
 *     +   x
 *    / \
 *   2   3
 * </pre>
 * The tree will be solved bottom up to get the value. If x is 2 the result will be 50.
 */
public interface Equation {

    /**
     * Evaluates the parsed {@link Equation} using an {@link EmptyStorage empty storage} implementation.
     * <p>
     * If the equation consists of any {@link Variable} implementations, these variables will be solved with the value 0.0
     *
     * @return Result object with the result of the equation.
     */
    default Result evaluate() {
        return evaluate(EmptyStorage.INSTANCE);
    }

    /**
     * Evaluates the parsed {@link Equation} using the provided {@link Storage}.
     * <p>
     * The Storage can be used to evaluate variables if any are present.
     *
     * @param storage the {@link Storage} to be used to solve variables.
     * @return Result object with the result of the equation.
     * @throws NullPointerException If any given argument is null.
     */
    Result evaluate(
            @NonNull Storage storage);

    /**
     * Converts the parsed {@link Equation} back into a localized String pattern.
     * <p>
     * The Locale may be used by implementations for purposes of formatting numbers to the correct decimal points.
     *
     * @param locale the Locale to be used.
     * @return parsable string pattern of the given equation
     * @throws NullPointerException If any given argument is null.
     */
    String toPattern(
            @NonNull Locale locale);

    /**
     * Parses the given equation String using the default {@link ParsingOptions}.
     *
     * @param equation The equation to be parsed.
     * @return Optional with parsed {@link Equation} or null if equation could not be parsed.
     * @throws ParseException       If a {@link EquationParser parser} in the {@link ParsingOptions options} encounters an unhandled exception. Under normal circumstances this exception would be returned as part of the {@link EquationOptional}, but if {@link ParsingOptions#getErrorBehavior()} returns true, the exception will be thrown instead.
     * @throws NullPointerException If any given argument is null.
     * @see ParsingOptions#defaultOptions()
     */
    static EquationOptional parse(
            @NonNull String equation) {

        return parse(equation, ParsingOptions.defaultOptions());
    }

    /**
     * Parses the given equation String using the specified {@link ParsingOptions}.
     *
     * @param equation       The equation to be parsed.
     * @param parsingOptions The {@link ParsingOptions} containing the {@link EquationParser parsers} to be used to parse the given equation.
     * @return Optional with parsed {@link Equation} or null if equation could not be parsed.
     * @throws ParseException       If a {@link EquationParser parser} in the {@link ParsingOptions options} encounters an unhandled exception. Under normal circumstances this exception would be returned as part of the {@link EquationOptional}, but if {@link ParsingOptions#getErrorBehavior()} returns true, the exception will be thrown instead.
     * @throws NullPointerException If any given argument is null.
     */
    static EquationOptional parse(
            @NonNull String equation,
            @NonNull ParsingOptions parsingOptions) {

        try {

            List<Token> tokenized = EquationStringTokenizer.tokenize(equation, parsingOptions);

            ParseResult parseResult = EquationParser.parseEquation(tokenized, 0, tokenized.size() - 1, parsingOptions);

            if (parsingOptions.getErrorBehavior() == ErrorBehavior.EXCEPTION && parseResult.getParseType() == ParseType.ERROR)
                throw new ParseException("Parsing failed with error: " + parseResult.getErrorMessage());

            return EquationOptional.of(parseResult);

        } catch (Throwable e) {

            if (parsingOptions.getErrorBehavior() == ErrorBehavior.EXCEPTION)
                throw e;

            return EquationOptional.of(e);
        }
    }
}
