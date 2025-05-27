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
package io.github.lordtylus.jep.functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathFunctionParserTest {

    @Test
    void parsesFunction() {

        /* Given */

        MathFunction function1 = new MathFunction("abc", Function.identity());
        MathFunction function2 = new MathFunction("bcd", Function.identity());
        MathFunction function3 = new MathFunction("cde", Function.identity());

        List<MathFunction> functions = List.of(function1, function2, function3);

        /* When */

        MathFunction actual = MathFunctionParser.parse(functions, "bcd").orElseThrow();

        /* Then */

        assertSame(function2, actual);
    }

    @Test
    void parsesFunctionConflictFirstWins() {

        /* Given */

        MathFunction function1 = new MathFunction("abc", Function.identity());
        MathFunction function2 = new MathFunction("bcd", Function.identity());
        MathFunction function3 = new MathFunction("bcd", Function.identity());

        List<MathFunction> functions = List.of(function1, function2, function3);

        /* When */

        MathFunction actual = MathFunctionParser.parse(functions, "bcd").orElseThrow();

        /* Then */

        assertSame(function2, actual);
    }

    @Test
    void parsesFunctionToLowercase() {

        /* Given */

        MathFunction function1 = new MathFunction("abc", Function.identity());
        MathFunction function2 = new MathFunction("bcd", Function.identity());
        MathFunction function3 = new MathFunction("cde", Function.identity());

        List<MathFunction> functions = List.of(function1, function2, function3);

        /* When */

        MathFunction actual = MathFunctionParser.parse(functions, "BCD").orElseThrow();

        /* Then */

        assertSame(function2, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "a, abc",
            "ab, abc",
            "b, bcd",
            "c, cde",
            "cd, cde",
    })
    void parsesFunctionByAlias(String pattern, String expected) {

        /* Given */

        MathFunction function1 = new MathFunction("abc", List.of("a", "ab"), Function.identity());
        MathFunction function2 = new MathFunction("bcd", List.of("b"), Function.identity());
        MathFunction function3 = new MathFunction("cde", List.of("c", "cd"), Function.identity());

        List<MathFunction> functions = List.of(function1, function2, function3);

        /* When */

        MathFunction actual = MathFunctionParser.parse(functions, pattern).orElseThrow();

        /* Then */

        assertEquals(expected, actual.getPattern());
    }

    @Test
    void doesNotParse() {

        /* Given */

        MathFunction function1 = new MathFunction("abc", List.of("a", "ab"), Function.identity());
        MathFunction function2 = new MathFunction("bcd", List.of("b"), Function.identity());
        MathFunction function3 = new MathFunction("cde", List.of("c", "cd"), Function.identity());

        List<MathFunction> functions = List.of(function1, function2, function3);

        /* When */

        Optional<MathFunction> actual = MathFunctionParser.parse(functions, "d");

        /* Then */

        assertTrue(actual.isEmpty());
    }
}