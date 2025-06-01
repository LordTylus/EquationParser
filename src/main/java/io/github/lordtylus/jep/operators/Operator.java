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
import io.github.lordtylus.jep.equation.Parenthesis;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

/**
 * An operator is the equivalent of an operator in mathematics such as the plus sign for addition or the minus sign for subtraction.
 * <p>
 * The operator has a symbol for parsing, an order in a hierarchy, and an evaluation function that will perform the operation using two numbers.
 * What calculation exactly is being performed is dependent on the passed {@link #evalFunction}
 * <p>
 * An operator can only have one char as a symbol such as + or - and during parsing of an {@link Equation Equation} its order is respected.
 * <p>
 * The smaller the order the later the operation is performed. In Mathematics the PEMDAS standard states that operators should be solved in order:
 * <ul>
 *     <li>Parentheses -> not an operator, see {@link Parenthesis Parenthesis}</li>
 *     <li>Exponents -> 2</li>
 *     <li>Multiplication/Division -> 1</li>
 *     <li>Addition/Subtraction -> 0</li>
 * </ul>
 * The standard implementations of this framework respect this order.
 * <p>
 * In case custom operators are created alongside the standard ones, it's important to think about how it would fit in the PEMDAS order to not result in unexpected results.
 */
@Value
public class Operator {

    int order;
    char pattern;

    @NonNull
    BiFunction<Number, Number, Number> evalFunction;

    /**
     * Returns the symbol of the operator.
     *
     * @return Character symbol of the Operator
     */
    public char toPattern() {
        return pattern;
    }

    /**
     * Evaluates the operation using the two given numbers according to the {@link #evalFunction} and returns the resulting number.
     * <p>
     * At this point it is important to note that the abstract concept of number will boil down to the concrete implementation of the operator itself.
     * The {@link StandardOperators} for example use double arithmetic, meaning that loss in precision can occur.
     * <p>
     * If precision is needed, using a custom implementation of an operator is advised.
     *
     * @param a first number of the operation
     * @param b second number of the equation edits the first
     * @return Resulting number of {@link #evalFunction}
     * @throws NullPointerException If any given argument is null.
     */
    public Number evaluate(
            @NonNull Number a,
            @NonNull Number b) {

        return evalFunction.apply(a, b);
    }

    /**
     * This method takes a list of operators and extracts its order information into a new list.
     * The result will be sorted ascending meaning that the lowest ranking order such as 0 for addition is first in the list.
     * <p>
     * Since the Iterable can contain any number of operators it is possible that the resulting List is not continuous, and gaps may occur.
     * For example {@link StandardOperators#ADD} and {@link StandardOperators#POW} would result in a List containing {0, 2}.
     * <p>
     * This method is used for parsing to ensure the set order of operation is respected and the resulting binary tree can be solved as expected.
     *
     * @param relevantOperators Iterable of operators
     * @return List containing the extract orders of operators, sorted ascending, without duplicates.
     * @throws NullPointerException If any given argument is null.
     */
    public static List<Integer> getRelevantOrders(
            @NonNull Iterable<Operator> relevantOperators) {

        Set<Integer> sortedSet = new TreeSet<>();

        for (Operator relevantOperator : relevantOperators)
            sortedSet.add(relevantOperator.getOrder());

        return new ArrayList<>(sortedSet);
    }

    /**
     * This method is a filter for the passed operators, to the given order.
     *
     * @param order             order to filter for.
     * @param relevantOperators Collection of operators to be filtered.
     * @return List of operators matching the given order, retaining same order as input list.
     * @throws NullPointerException If any given argument is null.
     */
    public static List<Operator> getRelevantOperatorsForOrder(
            int order,
            @NonNull Collection<Operator> relevantOperators) {

        return relevantOperators.stream()
                .filter(operator -> operator.getOrder() == order)
                .toList();
    }
}