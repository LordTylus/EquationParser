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

import java.util.Locale;

/**
 * This demo provides an example of how a parsed equation can be converted back into
 * a parsable string. This is useful if you want to store the equation in a database
 * for later use, or just generate some debug output.
 * <p>
 * When returning equations back to the string of your choice, you do have to provide a locale for the number format.
 * Supported when parsing are decimal points as period and as comma which should cover most languages.
 * <p>
 * A string using any other format is not parsable at this point in time, but it's something that can be added in the future.
 */
public class ToPatternEquationDemo {

    public static void main(String[] args) {

        String input = "(7.5+3)*(6-3.2)+[variable]/3^3";

        Equation equation = Equation.parse(input).get();

        System.out.println(equation.toPattern(Locale.ENGLISH)); //(7.5+3)*(6-3.2)+[variable]/3^3
        System.out.println(equation.toPattern(Locale.GERMAN)); //(7,5+3)*(6-3,2)+[variable]/3^3
    }
}
