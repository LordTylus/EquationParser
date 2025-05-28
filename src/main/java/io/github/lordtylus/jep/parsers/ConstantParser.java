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
import io.github.lordtylus.jep.options.ParsingOptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

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

    @Override
    public Optional<Constant> parse(
            @NonNull String equation,
            @NonNull ParsingOptions options) {

        try {

            String trimmedEquation = equation.replace(" ", "");

            if (trimmedEquation.isEmpty())
                return Optional.empty();

            int length = trimmedEquation.length();

            boolean encounteredDecimal = false;

            for (int i = 0; i < length; i++) {

                char c = trimmedEquation.charAt(i);

                if (c == '-' && i == 0)
                    continue;

                if (c == '.' || c == ',') {

                    if (encounteredDecimal)
                        return Optional.empty();

                    encounteredDecimal = true;
                    continue;
                }

                switch (c) {
                    case '1', '2', '3', '4', '5', '6', '7', '8', '9', '0':
                        continue;
                }

                return Optional.empty();
            }

            trimmedEquation = trimmedEquation.replace(",", ".");

            return Optional.of(new Constant(Double.parseDouble(trimmedEquation)));

        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
