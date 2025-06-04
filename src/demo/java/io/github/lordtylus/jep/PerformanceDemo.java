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
 * This is my Dev File I edit sometimes to test for different scenarios.
 * Sometimes when doing optimizations I want something I can check in to use for a while.
 * <p>
 * Don't mind it :-)
 */
public class PerformanceDemo {

    public static void main(String[] args) {

        String input = "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3";

        {
            long start = System.nanoTime();

            for (int i = 0; i < 1_000_000; i++)
                Equation.parse(input).get();

            long end = System.nanoTime();

            // 2.558 ms
            System.out.println(TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        }
    }
}
