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
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.EquationStringTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantParserTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1;1",
            "-1;-1",
            "1.2;1.2",
            "1,2;1.2",
            "-1.2;-1.2",
            "124.345;124.345",
            "-1234,2345;-1234.2345",
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Equation actual = ConstantParser.INSTANCE.parse(tokenized, 0, tokenized.size() - 1, options).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "abc",
            "1.2.3",
            "1,2.3",
            "+1",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    }, delimiter = ';')
    void doesNotParse(String equation) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Optional<? extends Equation> actual = ConstantParser.INSTANCE.parse(tokenized, 0, tokenized.size() - 1, options);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}