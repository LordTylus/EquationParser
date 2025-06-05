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
 * This demo shows that if in front of an open parenthesis is an unrecognized function that
 * an error is reported as to why it couldn't be parsed.
 * <p>
 * For that, the parse Method returns an EquationOptional, which functions like a Java
 * Optional and either contains the equation, the error or even a throwable.
 */
public class ParsingErrorsFunctionsDemo {

    public static void main(String[] args) {

        String input = "mod(7+3*2)+12";

        EquationOptional equation = Equation.parse(input);

        System.out.println(equation.isPresent()); //False
        System.out.println(equation.hasError()); //True
        System.out.println(equation.getErrorMessage()); //Unknown function 'mod'!
    }
}
