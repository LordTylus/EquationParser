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
 * This Demo shows the performance when parsing a bunch of Equations using in
 * a multithreaded manner utilizing parallel streams. This example just shows an
 * IntStream but there's nothing stopping you doing the same with a large collection.
 */
public class MultiThreadedParsingPerformanceDemo {

    public static void main(String[] args) {

        String input = "(7+3)*(6-3)+216/3^3+[x]";

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 1_000_000).parallel()
                    .forEach((i) -> Equation.parse(input));

            // 333 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("1 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 10_000_000).parallel()
                    .forEach((i) -> Equation.parse(input));

            // 1,858 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("10 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            IntStream.range(0, 100_000_000).parallel()
                    .forEach((i) -> Equation.parse(input));

            // 17,854 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("100 million passes: " + duration + " ms");
        }
    }
}
