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

import io.github.lordtylus.jep.equation.Constant;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser implementation parses the given input string as a double.
 * <p>
 * Supported are both positive and negative numbers, such as 23 or -34.
 * Decimal Numbers are also supported, and it will use common decimal separators (, and .) when parsing.
 * That way bow 23.45 and -34,23 can be parsed correctly.
 * <p>
 * In the event any other decimal separator, letter or symbol reaches this class, parsing fails and an empty optional is returned.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantParser implements EquationParser {

    /**
     * Singleton immutable instance of the {@link ConstantParser}
     */
    public static final ConstantParser INSTANCE = new ConstantParser();

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+([.,]\\d+)?$");

    @Override
    public Optional<Constant> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register) {

        try {

            equation = WHITESPACE_PATTERN.matcher(equation)
                    .replaceAll("");

            Matcher matcher = DECIMAL_PATTERN.matcher(equation);

            if (!matcher.matches())
                return Optional.empty();

            equation = equation.replace(",", ".");

            return Optional.of(new Constant(Double.parseDouble(equation)));

        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
