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
 * This demo shows parsing of a very simple A + B equation.
 * The longer an equation is, the more expensive parsing becomes.
 * <p>
 * This is pretty much the fastest you can get.
 */
public class SingleThreadedSimpleParsingPerformanceDemo {

    public static void main(String[] args) {

        String input = "[number 1]+[number 2]";

        // AMD Ryzen 7 3700X 8-Core Processor
        {
            long start = System.nanoTime();

            for (int i = 0; i < 1_000_000; i++)
                Equation.parse(input);

            // 544 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("1 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {

            long start = System.nanoTime();

            for (int i = 0; i < 10_000_000; i++)
                Equation.parse(input);

            // 3,077 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("10 million passes: " + duration + " ms");
        }

        // AMD Ryzen 7 3700X 8-Core Processor
        {

            long start = System.nanoTime();

            for (int i = 0; i < 100_000_000; i++)
                Equation.parse(input);

            // 30,339 ms
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            System.out.println("100 million passes: " + duration + " ms");
        }
    }
}
