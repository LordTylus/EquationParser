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

import io.github.lordtylus.jep.equation.Parenthesis;
import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.MathFunctionParser;
import io.github.lordtylus.jep.functions.StandardFunctions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.tokens.ParenthesisToken;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This parser takes care of parenthesis in the equation and also matches functions directly in front of the opening parenthesis.
 * <p>
 * A valid expression for this parser would be sqrt(7+2), as the string needs to start with a function name, followed by the opening parenthesis, and end with the closing parenthesis.
 * the function name can be empty, for as long as empty matches to a {@link MathFunction} passed in the constructor.
 * <p>
 * The parts of the string inside the parentheses remain unaltered (except trimming) and are passed down the Equation for parsing, building the composite pattern of the Equation.
 * <p>
 * If the string could not be parsed, an empty optional is returned.
 */
public final class ParenthesisParser implements EquationParser {

    /**
     * Default instance of the {@link ParenthesisParser} using all {@link StandardFunctions}
     */
    public static final ParenthesisParser DEFAULT = new ParenthesisParser(StandardFunctions.all());

    private final MathFunctionParser mathFunctionParser;

    /**
     * Creates a new Parser instance with the {@link MathFunction functions} to use for parsing.
     *
     * @param relevantFunctions functions this parser should recognize.
     */
    public ParenthesisParser(
            @NonNull Collection<MathFunction> relevantFunctions) {

        this.mathFunctionParser = new MathFunctionParser(relevantFunctions);
    }

    @Override
    public ParseResult parse(
            @NonNull List<Token> tokenizedEquation,
            int startIndex,
            int endIndex,
            @NonNull ParsingOptions options) {

        try {

            if (endIndex - startIndex < 2)
                return ParseResult.notMine();

            Token first = tokenizedEquation.get(startIndex);

            if (first instanceof ParenthesisToken openingToken) {
                if (!openingToken.isOpening())
                    return ParseResult.error("");
            } else {
                return ParseResult.notMine();
            }

            Token last = tokenizedEquation.get(endIndex);

            if (last instanceof ParenthesisToken closingToken) {
                if (!closingToken.isClosing())
                    return ParseResult.error("");
            } else {
                return ParseResult.notMine();
            }

            if (openingToken.getClosing() == null)
                return ParseResult.error("");

            if (openingToken.getClosing() != closingToken)
                return ParseResult.notMine();

            String functionName = openingToken.getFunction();
            functionName = functionName.replace(" ", "");

            Optional<MathFunction> function = mathFunctionParser.parse(functionName);
            if (function.isEmpty())
                return ParseResult.error("");

            ParseResult inner = EquationParser.parseEquation(tokenizedEquation,
                    startIndex + 1, endIndex - 1, options);

            return switch (inner.getParseType()) {
                case ERROR -> inner;
                case NOT_MINE -> ParseResult.error("");
                default -> ParseResult.ok(new Parenthesis(function.get(), inner.getNullableEquation()));
            };

        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
