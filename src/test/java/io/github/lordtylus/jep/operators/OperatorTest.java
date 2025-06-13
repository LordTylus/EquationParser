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

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorTest {

    @Test
    void evaluates() {

        /* Given */

        Operator sut = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());

        /* When */

        double actual = sut.evaluate(20, 25.5).doubleValue();

        /* Then */

        assertEquals(45.5, actual, 0.0001);
    }
}