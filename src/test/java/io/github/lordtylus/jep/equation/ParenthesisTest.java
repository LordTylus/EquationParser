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
package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.StandardFunctions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParenthesisTest {

    @Test
    void canOutputPatternEnglish() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        String actual = sut.toPattern(Locale.ENGLISH);

        /* Then */

        String expected = "sqrt(72.25)";

        assertEquals(expected, actual);
    }

    @Test
    void canOutputPatternGerman() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        String actual = sut.toPattern(Locale.GERMAN);

        /* Then */

        String expected = "sqrt(72,25)";

        assertEquals(expected, actual);
    }

    @Test
    void canEvaluateDouble() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asDouble();

        assertEquals(8.5, value, 0.00001);
    }

    @Test
    void canEvaluateFloat() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asFloat();

        assertEquals(8.5F, value, 0.00001);
    }

    @Test
    void canEvaluateInt() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        int value = actual.asInt();

        assertEquals(8, value);
    }

    @Test
    void canEvaluateLong() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        long value = actual.asLong();

        assertEquals(8, value);
    }

    @Test
    void printsPattern() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        String expected = """
                sqrt ( 72.25 ) = 8.5
                  72.25 = 72.25
                """;

        assertEquals(expected, sb.toString());
    }

    @Test
    void toStaticEquationDisplaysCorrectString() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.toStaticEquation(sb);

        /* Then */

        assertEquals("sqrt(72.25)", sb.toString());
    }

    @Test
    void toDisplayStringIsCorrect() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        Result result = sut.evaluate();

        /* When */

        String actual = result.toDisplayString();

        /* Then */

        assertEquals("sqrt(72.25)=8.5", actual);
    }

    @Test
    void toStringIsCorrect() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        Result result = sut.evaluate();

        /* When */

        String actual = result.toString();

        /* Then */

        assertEquals("sqrt(72.25)=8.5", actual);
    }
}