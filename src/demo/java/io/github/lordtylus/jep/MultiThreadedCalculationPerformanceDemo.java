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

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * This demo shows that you can also run calculations on the same equation in parallel
 * However, it is important that each calculation has its own storage, since changing the storages
 * value during evaluation will lead to undesired results.
 * <p>
 * In this instance, since there is only one variable in the equation I just pass the value by
 * treating the Storage as a functional interface.
 */
public class MultiThreadedCalculationPerformanceDemo {

    public static void main(String[] args) {

        String input = "(7+3)*(6-3)+216/3^3+[x]";
        Equation equation = Equation.parse(input).get();

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 1_000_000).parallel()
                    .forEach((i) -> equation.evaluate(variable -> i));

            // 146 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("1 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 10_000_000).parallel()
                    .forEach((i) -> equation.evaluate(variable -> i));

            // 841 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("10 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 100_000_000).parallel()
                    .forEach((i) -> equation.evaluate(variable -> i));

            // 7,573 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("100 million passes: " + duration + " ms");
        }
    }
}
