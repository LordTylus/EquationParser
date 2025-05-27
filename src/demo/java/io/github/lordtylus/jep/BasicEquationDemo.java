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
 * This demo shows the basic function how to input and
 * parse a basic equation without any variables or special cases.
 * <p>
 * You will notice that parsing and evaluating the equation is quite simple.
 */
public class BasicEquationDemo {

    public static void main(String[] args) {

        String input = "(7+3)*(6-3)+216/3^3";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //38.0
    }
}
