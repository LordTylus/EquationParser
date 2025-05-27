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

/**
 * Unless a custom implementation is provided this framework can only work with square and cube roots.
 * <p>
 * If possible and feasible for the required use case you can use a fractional power for higher level roots.
 * <p>
 * This example shows how the 5th root will be calculated.
 */
public class HigherRootsDemo {

    public static void main(String[] args) {

        String input = "32^(1/5)";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //2.0
    }
}
