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

import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.StandardFunctions;
import io.github.lordtylus.jep.options.CustomParsingOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * This demo shows how one would go about if they wanted to add a new function.
 * The given example shows a function taking a radius number and calculating the area of a circle with it.
 * <p>
 * First the function is defined, then a set with all supported functions is created.
 * We take the standard functions and add this one extra.
 * <p>
 * Then a {@link CustomParsingOptions} object is created with all these functions.
 */
public class CustomFunctionParsersDemo {

    public static void main(String[] args) {

        MathFunction areaCircle = new MathFunction("area",
                number -> number.doubleValue() * number.doubleValue() * Math.PI);

        Set<MathFunction> functions = new HashSet<>(StandardFunctions.all());
        functions.add(areaCircle);

        CustomParsingOptions parserOptions = CustomParsingOptions.defaultWithFunctions(functions);

        Equation equation = Equation.parse("2+area(10)", parserOptions).get();

        System.out.println(equation.evaluate().asDouble()); //316.15926535...
    }
}
