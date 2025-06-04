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

import io.github.lordtylus.jep.operators.Operator;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.CustomParserOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * This demo shows how one would go about if they wanted to add a new operator.
 * The given example shows how the Modulo operator would be implemented manually.
 * <p>
 * First the operator is defined, then a set with all supported operators is created.
 * We take the standard operators and add this one extra.
 * <p>
 * Then a {@link CustomParserOptions} object is created with all these operators.
 */
public class CustomOperatorsParsersDemo {

    public static void main(String[] args) {

        /* Most programming languages place Modulo operator on the same order as multiplication and division.*/
        Operator modulo = new Operator(StandardOperators.MULT.getOrder(), '%',
                (a, b) -> a.doubleValue() % b.doubleValue());

        Set<Operator> operators = new HashSet<>(StandardOperators.all());
        operators.add(modulo);

        CustomParserOptions parserOptions = CustomParserOptions.defaultWithOperators(operators);

        /* operators with same order are solved from left to right. so 4*10 ist calculated first. Then Modulo 3 which is 1. */
        Equation equation = Equation.parse("2+4*10%3", parserOptions).get();

        System.out.println(equation.evaluate().asDouble()); //3.0
    }
}
