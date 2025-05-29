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

/**
 * This demo contains a simple performance test, which parses the input 1 Million,
 * 10 Million and 100 Million times.
 * This framework is not optimized for parsing speed, but for calculation performance
 * with different variables.
 */
public class SingleThreadedParsingPerformanceDemo {

    public static void main(String[] args) {

        String input = "(7+3)*(6-3)+216/3^3+[x]";

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            for (int i = 0; i < 1_000_000; i++)
                Equation.parse(input);

            // 1.633 ms
            System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {

            long start = System.nanoTime();

            for (int i = 0; i < 10_000_000; i++)
                Equation.parse(input);

            // 13.861 ms
            System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {

            long start = System.nanoTime();

            for (int i = 0; i < 100_000_000; i++)
                Equation.parse(input);

            // 138.553 ms
            System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " ms");
        }
    }
}
