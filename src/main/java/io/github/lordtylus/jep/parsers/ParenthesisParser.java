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
import io.github.lordtylus.jep.equation.Parenthesis;
import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.MathFunctionParser;
import io.github.lordtylus.jep.functions.StandardFunctions;
import io.github.lordtylus.jep.options.ParsingOptions;
import lombok.NonNull;

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
            @NonNull List<MathFunction> relevantFunctions) {

        this.mathFunctionParser = new MathFunctionParser(relevantFunctions);
    }

    @Override
    public Optional<Parenthesis> parse(
            @NonNull String equation,
            @NonNull ParsingOptions options) {

        try {

            String trimmedEquation = equation.trim();

            if (!trimmedEquation.endsWith(")"))
                return Optional.empty();

            int index = trimmedEquation.indexOf("(");
            if (index == -1)
                return Optional.empty();

            int depth = 0;
            for (int i = trimmedEquation.length() - 1; i >= 0; i--) {

                char c = trimmedEquation.charAt(i);

                switch (c) {

                    case ')':
                        depth++;
                        continue;

                    case '(':

                        depth--;

                        if (depth == 0 && i != index)
                            return Optional.empty();
                }
            }

            String innerString = trimmedEquation.substring(index + 1, trimmedEquation.length() - 1);
            String functionName = trimmedEquation.substring(0, index);

            functionName = functionName.replace(" ", "");

            Optional<MathFunction> function = mathFunctionParser.parse(functionName);
            if (function.isEmpty())
                return Optional.empty();

            Optional<Equation> inner = Equation.parse(innerString, options);

            return inner.map(e -> new Parenthesis(function.get(), e));

        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
