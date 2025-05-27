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
package io.github.lordtylus.jep.operators;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardOperatorsTest {

    @Test
    void evaluateAddition() {

        /* Given */

        Operator sut = StandardOperators.ADD;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(30.5, actual, 0.0001);
    }

    @Test
    void evaluateSubtraction() {

        /* Given */

        Operator sut = StandardOperators.SUB;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(-9.5, actual, 0.0001);
    }

    @Test
    void evaluateMultiplication() {

        /* Given */

        Operator sut = StandardOperators.MULT;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(210, actual, 0.0001);
    }

    @Test
    void evaluateDivision() {

        /* Given */

        Operator sut = StandardOperators.DIV;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(0.525, actual, 0.0001);
    }

    @Test
    void evaluatePower() {

        /* Given */

        Operator sut = StandardOperators.POW;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(265329770514442013394.54307, actual, 0.0001);
    }

    @Test
    void allContainsCorrectOperators() {

        /* Given / When */

        List<Operator> actual = StandardOperators.all();

        /* Then */

        List<Operator> expected = List.of(
                StandardOperators.ADD,
                StandardOperators.SUB,
                StandardOperators.MULT,
                StandardOperators.DIV,
                StandardOperators.POW
        );

        assertEquals(expected, actual);
    }
}