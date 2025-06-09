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

import io.github.lordtylus.jep.equation.Variable;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * This parser implementation parses the given input string and extracts the variable name from it.
 * <p>
 * Supported are variables in brackets [] such as [My Fancy Variable]. the variable name is
 * case-sensitive, and spaces will remain in place.
 * <p>
 * If the string does not begin with [ or end with ] (after trimming) parsing fails and an
 * empty optional is returned. This means Strings such as [hey]+[you] can be parsed to hey]+[you.
 * The order in which the parsers are executed ensures this does not happen. And if it does,
 * this suggests there is an error in the setup when custom parsers are used.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VariableParser implements EquationParser {

    /**
     * Singleton immutable instance of the {@link VariableParser}
     */
    public static final VariableParser INSTANCE = new VariableParser();

    @Override
    public ParseResult parse(
            @NonNull List<Token> tokenizedEquation,
            int startIndex,
            int endIndex,
            @NonNull ParsingOptions options) {

        try {

            if (endIndex - startIndex != 0)
                return ParseResult.notMine();

            Token token = tokenizedEquation.get(startIndex);

            if (!(token instanceof ValueToken))
                return ParseResult.notMine();

            String trimmedEquation = token.getString().trim();
            VariablePattern variablePattern = options.getVariablePattern();

            if (variablePattern.isEscaped()) {

                if (trimmedEquation.charAt(0) != variablePattern.openingCharacter())
                    return ParseResult.notMine();

                if (trimmedEquation.charAt(trimmedEquation.length() - 1) != variablePattern.closingCharacter())
                    return ParseResult.notMine();

                trimmedEquation = trimmedEquation.substring(1, trimmedEquation.length() - 1);
            }

            return ParseResult.ok(new Variable(trimmedEquation));

        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
