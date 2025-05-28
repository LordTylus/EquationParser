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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParenthesisParserTest {

    @ParameterizedTest
    @CsvSource(value = {
            "(1+2);(1+2)",
            "abs(1+2);abs(1+2)",
            "sin(1+2);sin(1+2)",
            "asin(1+2);asin(1+2)",
            "sinh(1+2);sinh(1+2)",
            "SiNh(1+2);sinh(1+2)",
            "cos(1+2);cos(1+2)",
            "acos(1+2);acos(1+2)",
            "cosh(1+2);cosh(1+2)",
            "tan(1+2);tan(1+2)",
            "atan(1+2);atan(1+2)",
            "tanh(1+2);tanh(1+2)",
            "exp(1+2);exp(1+2)",
            "log(1+2);log(1+2)",
            "log10(1+2);log10(1+2)",
            "floor(1+2);floor(1+2)",
            "round(1+2);round(1+2)",
            " r o u n d ( 1 + 2 );round(1+2)",
            "ceil(1+2);ceil(1+2)",
            "sqrt(1+2);sqrt(1+2)",
            "cbrt(1+2);cbrt(1+2)",
            "rad(1+2);rad(1+2)",
            "deg(1+2);deg(1+2)",
            "((1+2)*(2+3));((1+2)*(2+3))",
            "(([hallo]+2)*(2+[Hal lo]));(([hallo]+2)*(2+[Hal lo]))"
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        /* When */

        Equation actual = ParenthesisParser.DEFAULT.parse(equation, options).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "1+1",
            "(1+1",
            "lol(123+333)",
            "a#c",
            "[hallo]",
    })
    void doesNotParse(String equation) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        /* When */

        Optional<? extends Equation> actual = ParenthesisParser.DEFAULT.parse(equation, options);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}