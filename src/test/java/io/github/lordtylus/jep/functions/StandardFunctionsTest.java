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

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardFunctionsTest {

    @Test
    void evaluatesNop() {

        /* Given */

        MathFunction sut = StandardFunctions.NOP;

        /* When */

        double actual = sut.evaluate(-14.4).doubleValue();

        /* Then */

        assertEquals(-14.4, actual, 0.0001);
    }

    @Test
    void evaluatesAbs() {

        /* Given */

        MathFunction sut = StandardFunctions.ABS;

        /* When */

        double actual = sut.evaluate(-14.4).doubleValue();

        /* Then */

        assertEquals(14.4, actual, 0.0001);
    }

    @Test
    void evaluatesSin() {

        /* Given */

        MathFunction sut = StandardFunctions.SIN;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(0.5000001943375613, actual, 0.0001);
    }

    @Test
    void evaluatesASin() {

        /* Given */

        MathFunction sut = StandardFunctions.ASIN;

        /* When */

        double actual = sut.evaluate(0.5000001943375613).doubleValue();

        /* Then */

        assertEquals(0.523599, actual, 0.0001);
    }

    @Test
    void evaluatesSinH() {

        /* Given */

        MathFunction sut = StandardFunctions.SINH;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(0.5478537297594726, actual, 0.0001);
    }

    @Test
    void evaluatesCos() {

        /* Given */

        MathFunction sut = StandardFunctions.COS;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(0.8660252915835662, actual, 0.0001);
    }

    @Test
    void evaluatesACos() {

        /* Given */

        MathFunction sut = StandardFunctions.ACOS;

        /* When */

        double actual = sut.evaluate(0.8660252915835662).doubleValue();

        /* Then */

        assertEquals(0.523599, actual, 0.0001);
    }

    @Test
    void evaluatesCosH() {

        /* Given */

        MathFunction sut = StandardFunctions.COSH;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(1.140238444015709, actual, 0.0001);
    }

    @Test
    void evaluatesTan() {

        /* Given */

        MathFunction sut = StandardFunctions.TAN;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(0.5773505683919328, actual, 0.0001);
    }

    @Test
    void evaluatesATan() {

        /* Given */

        MathFunction sut = StandardFunctions.ATAN;

        /* When */

        double actual = sut.evaluate(0.5773505683919328).doubleValue();

        /* Then */

        assertEquals(0.523599, actual, 0.0001);
    }

    @Test
    void evaluatesTanH() {

        /* Given */

        MathFunction sut = StandardFunctions.TANH;

        /* When */

        double actual = sut.evaluate(0.523599).doubleValue();

        /* Then */

        assertEquals(0.4804729507540835, actual, 0.0001);
    }

    @Test
    void evaluatesExp() {

        /* Given */

        MathFunction sut = StandardFunctions.EXP;

        /* When */

        double actual = sut.evaluate(14.4).doubleValue();

        /* Then */

        assertEquals(1794074.772606215, actual, 0.0001);
    }

    @Test
    void evaluatesLog() {

        /* Given */

        MathFunction sut = StandardFunctions.LOG;

        /* When */

        double actual = sut.evaluate(14.4).doubleValue();

        /* Then */

        assertEquals(2.66722, actual, 0.0001);
    }

    @Test
    void evaluatesLog10() {

        /* Given */

        MathFunction sut = StandardFunctions.LOG10;

        /* When */

        double actual = sut.evaluate(14.4).doubleValue();

        /* Then */

        assertEquals(1.15836, actual, 0.0001);
    }

    @Test
    void evaluatesFloor() {

        /* Given */

        MathFunction sut = StandardFunctions.FLOOR;

        /* When */

        double actual = sut.evaluate(14.4).doubleValue();

        /* Then */

        assertEquals(14, actual, 0.0001);
    }

    @ParameterizedTest
    @CsvSource({
            "14.001, 14",
            "14.1, 14",
            "14.2, 14",
            "14.3, 14",
            "14.4, 14",
            "14.499, 14",
            "14.5, 15",
            "14.6, 15",
            "14.7, 15",
            "14.8, 15",
            "14.9, 15",
            "14.999, 15",
    })
    void evaluatesRound(double value, double expected) {

        /* Given */

        MathFunction sut = StandardFunctions.ROUND;

        /* When */

        double actual = sut.evaluate(value).doubleValue();

        /* Then */

        assertEquals(expected, actual, 0.0001);
    }

    @Test
    void evaluatesCeil() {

        /* Given */

        MathFunction sut = StandardFunctions.CEIL;

        /* When */

        double actual = sut.evaluate(14.4).doubleValue();

        /* Then */

        assertEquals(15, actual, 0.0001);
    }

    @Test
    void evaluatesSqrt() {

        /* Given */

        MathFunction sut = StandardFunctions.SQRT;

        /* When */

        double actual = sut.evaluate(9).doubleValue();

        /* Then */

        assertEquals(3, actual, 0.0001);
    }

    @Test
    void evaluatesCbrt() {

        /* Given */

        MathFunction sut = StandardFunctions.CBRT;

        /* When */

        double actual = sut.evaluate(8).doubleValue();

        /* Then */

        assertEquals(2, actual, 0.0001);
    }

    @Test
    void evaluatesRad() {

        /* Given */

        MathFunction sut = StandardFunctions.RAD;

        /* When */

        double actual = sut.evaluate(65).doubleValue();

        /* Then */

        assertEquals(1.1344640137963142, actual, 0.0001);
    }

    @Test
    void evaluatesDeg() {

        /* Given */

        MathFunction sut = StandardFunctions.DEG;

        /* When */

        double actual = sut.evaluate(1.1344640137963142).doubleValue();

        /* Then */

        assertEquals(65, actual, 0.0001);
    }

    @Test
    void allContainsCorrectFunctions() {

        /* Given / When */

        List<MathFunction> actual = StandardFunctions.all();

        /* Then */

        List<MathFunction> expected = List.of(
                StandardFunctions.NOP,
                StandardFunctions.ABS,
                StandardFunctions.SIN,
                StandardFunctions.ASIN,
                StandardFunctions.SINH,
                StandardFunctions.COS,
                StandardFunctions.ACOS,
                StandardFunctions.COSH,
                StandardFunctions.TAN,
                StandardFunctions.ATAN,
                StandardFunctions.TANH,
                StandardFunctions.EXP,
                StandardFunctions.LOG,
                StandardFunctions.LOG10,
                StandardFunctions.FLOOR,
                StandardFunctions.ROUND,
                StandardFunctions.CEIL,
                StandardFunctions.SQRT,
                StandardFunctions.CBRT,
                StandardFunctions.RAD,
                StandardFunctions.DEG
        );

        assertEquals(expected, actual);
    }
}