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
import io.github.lordtylus.jep.operators.Operator;
import io.github.lordtylus.jep.operators.StandardOperators;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTest {

    @Test
    void canOutputPatternEnglish() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.ADD;

        Operation sut = new Operation(left, right, operator);

        /* When */

        String actual = sut.toPattern(Locale.ENGLISH);

        /* Then */

        String expected = "123.13+111.23";

        assertEquals(expected, actual);
    }

    @Test
    void canOutputPatternGerman() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.SUB;

        Operation sut = new Operation(left, right, operator);

        /* When */

        String actual = sut.toPattern(Locale.GERMAN);

        /* Then */

        String expected = "123,13-111,23";

        assertEquals(expected, actual);
    }

    @Test
    void canEvaluateDouble() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.ADD;

        Operation sut = new Operation(left, right, operator);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asDouble();

        assertEquals(234.36, value, 0.00001);
    }

    @Test
    void canEvaluateFloat() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.ADD;

        Operation sut = new Operation(left, right, operator);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asFloat();

        assertEquals(234.36, value, 0.00001);
    }

    @Test
    void canEvaluateInt() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.ADD;

        Operation sut = new Operation(left, right, operator);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        int value = actual.asInt();

        assertEquals(234, value);
    }

    @Test
    void canEvaluateLong() {

        /* Given */

        Constant left = new Constant(123.13);
        Constant right = new Constant(111.23);
        Operator operator = StandardOperators.ADD;

        Operation sut = new Operation(left, right, operator);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        long value = actual.asLong();

        assertEquals(234, value);
    }

    @Test
    void printsPattern() {

        /* Given */

        Constant left = new Constant(2.5);
        Constant right = new Constant(5);
        Operator operator = StandardOperators.MULT;

        Operation sut = new Operation(left, right, operator);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        String expected = """
                2.5 * 5.0 = 12.5
                  2.5 = 2.5
                  5.0 = 5.0
                """;

        assertEquals(expected, sb.toString());
    }

    @Test
    void toStaticEquationDisplaysCorrectString() {

        /* Given */

        Constant left = new Constant(2.5);
        Constant right = new Constant(5);
        Operator operator = StandardOperators.MULT;

        Operation sut = new Operation(left, right, operator);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.toStaticEquation(sb);

        /* Then */

        assertEquals("2.5*5.0", sb.toString());
    }

    @Test
    void toStringIsCorrect() {

        /* Given */

        Constant left = new Constant(2.5);
        Constant right = new Constant(5);
        Operator operator = StandardOperators.MULT;

        Operation sut = new Operation(left, right, operator);

        Result result = sut.evaluate();

        /* When */

        String actual = result.toDisplayString();

        /* Then */

        assertEquals("2.5*5.0=12.5", actual);
    }
}