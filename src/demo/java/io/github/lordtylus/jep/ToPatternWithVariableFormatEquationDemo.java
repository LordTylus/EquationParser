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

import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;

import java.util.Locale;

/**
 * This demo is an extension of {@link ToPatternEquationDemo} as instead of just the
 * language, it is possible to also set the format to be used for variables.
 * <p>
 * This is useful as depending on your use case printing the variable names in a different
 * format may be necessary. However, keep in mind that a different symbol may result in an unparsable string.
 */
public class ToPatternWithVariableFormatEquationDemo {

    public static void main(String[] args) {

        String input = "(7.5+3)*(6-3.2)+[variable]/3^3";

        Equation equation = Equation.parse(input).get();

        VariablePattern brackets = StandardVariablePatterns.BRACKETS;
        VariablePattern braces = StandardVariablePatterns.BRACES;
        VariablePattern percent = new VariablePattern(true, '%', '%');

        System.out.println(equation.toPattern(Locale.ENGLISH, brackets)); //(7.5+3)*(6-3.2)+[variable]/3^3
        System.out.println(equation.toPattern(Locale.ENGLISH, braces)); //(7.5+3)*(6-3.2)+{variable}/3^3
        System.out.println(equation.toPattern(Locale.ENGLISH, percent)); //(7.5+3)*(6-3.2)+%variable%/3^3
    }
}
