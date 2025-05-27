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
package io.github.lordtylus.jep.options;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.tokenizer.EquationTokenizer;

import java.util.List;

/**
 * This Options object holds references to all {@link EquationParser EquationParsers},
 * as well as the mandatory {@link EquationTokenizer} objects to be used for parsing.
 * <p>
 * It can be passed directly into {@link Equation#parse(String, ParsingOptions)} and
 * therefore can influence if and how an equation is parsed.
 */
public interface ParsingOptions {

    /**
     * Returns a list of {@link EquationParser} classes to be used for solving mathematical equations.
     * <p>
     * See {@link DefaultParserOptions}
     *
     * @return {@link DefaultParserOptions} instance
     */
    static ParsingOptions defaultOptions() {
        return DefaultParserOptions.INSTANCE;
    }

    /**
     * Returns a list of {@link EquationParser} objects to be used for parsing.
     * The order is important for parsing to prevent unnecessary checks and prevent
     * incorrect matches in case the EquationParsers must be executed in order to succeed.
     * This depends on the implementation of the {@link EquationParser} classes themselves.
     *
     * @return List of {@link EquationParser} to be used for parsing
     */
    List<EquationParser> getRegisteredParsers();

    /**
     * Returns a List of {@link EquationTokenizer} objects to be used for tokenizing the
     * equation string before parsing. The order of the list is important as it may impact
     * how equation is later parsed.
     *
     * @return List of {@link EquationTokenizer} objects to prepare the string for parsing.
     */
    List<EquationTokenizer> getRegisteredTokenizers();

}
