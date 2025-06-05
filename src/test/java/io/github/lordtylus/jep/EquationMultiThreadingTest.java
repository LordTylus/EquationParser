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

import io.github.lordtylus.jep.storages.SimpleStorage;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EquationMultiThreadingTest {

    @Test
    void canParseMultiThreaded() {

        /* Given */

        String input = "5+abs(-100+[x])";

        /* When */

        Map<Integer, Double> resultMap = new ConcurrentHashMap<>();

        IntStream.range(0, 10_000).parallel()
                .forEach((i) -> {

                    Equation equation = Equation.parse(input).get();

                    SimpleStorage storage = new SimpleStorage();
                    storage.putValue("x", i);

                    double result = equation.evaluate(storage).asDouble();

                    resultMap.put(i, result);
                });

        /* Then */

        for (Entry<Integer, Double> entry : resultMap.entrySet()) {

            int x = entry.getKey();

            double expected = 5 + Math.abs(-100 + x);
            double actual = entry.getValue();

            assertEquals(expected, actual);
        }
    }

    @Test
    void canCalcMultiThreaded() {

        /* Given */

        String input = "5+abs(-100+[x])";

        /* When */

        Map<Integer, Double> resultMap = new ConcurrentHashMap<>();
        Equation equation = Equation.parse(input).get();

        IntStream.range(0, 10_000).parallel()
                .forEach((i) -> {

                    SimpleStorage storage = new SimpleStorage();
                    storage.putValue("x", i);

                    double result = equation.evaluate(storage).asDouble();

                    resultMap.put(i, result);
                });

        /* Then */

        for (Entry<Integer, Double> entry : resultMap.entrySet()) {

            int x = entry.getKey();

            double expected = 5 + Math.abs(-100 + x);
            double actual = entry.getValue();

            assertEquals(expected, actual);
        }
    }
}
