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
 * This demo shows how to work with negative numbers.
 * <p>
 * Similar to many other programs out there, negative numbers have to be put in parentheses to be parsed correctly.
 * <p>
 * In that form they behave like any other number.
 */
public class NegativeNumbersDemo {

    public static void main(String[] args) {

        String input = "2*(-3)^3";

        Equation equation = Equation.parse(input).get();

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //-54.0
    }
}
