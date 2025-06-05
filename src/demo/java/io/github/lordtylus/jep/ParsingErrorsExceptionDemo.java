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

import io.github.lordtylus.jep.options.CustomParserOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.ParseException;

/**
 * This Demo shows how to set up the {@link ParsingOptions} in order to receive
 * exceptions instead of empty optionals.
 * <p>
 * This is only possible using {@link CustomParserOptions} and can easily enable using the appropriate setter.
 */
public class ParsingErrorsExceptionDemo {

    public static void main(String[] args) {

        String input = "(7+3*2+12";

        CustomParserOptions parserOptions = CustomParserOptions.withDefaults();
        parserOptions.setThrowsExceptionsOnError(true);

        try {

            Equation.parse(input, parserOptions);

        } catch (ParseException e) {
            System.out.println(e.getMessage()); // Parsing failed with error: Parenthesis mismatch, ')' missing!
        }
    }
}
