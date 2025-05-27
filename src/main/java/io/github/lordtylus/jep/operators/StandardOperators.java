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
package io.github.lordtylus.jep.operators;

import io.github.lordtylus.jep.Equation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class contains a set of default operators to be used for solving {@link Equation eqations}.
 * All the operators provided convert the number object to a double first before performing the calculations.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class StandardOperators {

    /**
     * Adds number B to A (A+B)
     */
    public static final Operator ADD = new Operator(0, '+', fromDouble(Double::sum));
    /**
     * Subtracts number A from B (A-B)
     */
    public static final Operator SUB = new Operator(0, '-', fromDouble((a, b) -> a - b));
    /**
     * Multiplies number A with B (A*B)
     */
    public static final Operator MULT = new Operator(1, '*', fromDouble((a, b) -> a * b));
    /**
     * Divides number A with B (A/B)
     */
    public static final Operator DIV = new Operator(1, '/', fromDouble((a, b) -> a / b));
    /**
     * Raises number A to the power of B (A^B)
     */
    public static final Operator POW = new Operator(2, '^', fromDouble(Math::pow));

    private static final List<Operator> ALL = List.of(ADD, SUB, MULT, DIV, POW);

    /**
     * @return all standard {@link Operator operators} defined in this class.
     */
    public static List<Operator> all() {
        return ALL;
    }

    private static BiFunction<Number, Number, Number> fromDouble(BiFunction<Double, Double, Double> function) {
        return (a, b) -> function.apply(a.doubleValue(), b.doubleValue());
    }
}
