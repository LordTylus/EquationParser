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
package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.operators.Operator;
import io.github.lordtylus.jep.operators.OperatorParser;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.ParsingOptions;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This parser parses operations such as 1+2 by extracting the operator and splitting the string in two expressions.
 * <p>
 * To do that, the parsers reads the string from right to left ignoring everything inside parentheses or brackets and split at the lowest order operator first.
 * Since the lowest order operators such as addition and subtraction are the last to be evaluated, they need to be added to the tree first.
 * The order of operations in mathematics dictates that if two operators have the same value, they are to be solved from left to right,
 * meaning the right most operator needs to be added to the tree before the left ones do.
 * <p>
 * 1+2+3+4 would therefore translate to the following binary tree:
 * <pre>
 *       +
 *      / \
 *     +   4
 *    / \
 *   +   3
 *  / \
 * 1   2
 *
 * It is solved bottom up:
 *     +
 *    / \
 *   +   4
 *  / \
 * 3   3
 *
 *   +
 *  / \
 * 6   4
 *
 * 10
 * </pre>
 * For operators on the same level the order usually doesn't matter. But since this is the convention, it is to be respected.
 * <p>
 * After extracting the operator, the two resulting expressions left and right of it, are passed to the {@link Equation} for parsing to build the binary tree.
 * If parsing is not possible, an empty optional is returned.
 */
public final class OperationParser implements EquationParser {

    /**
     * Default instance of the {@link OperationParser} using all {@link StandardOperators}
     */
    public static final OperationParser DEFAULT = new OperationParser(StandardOperators.all());

    private final List<Integer> relevantOperatorOrders;
    private final Map<Integer, OperatorInformation> operatorsMap = new HashMap<>();

    /**
     * Creates a new Parser instance with the {@link Operator operators} to use for parsing.
     *
     * @param relevantOperators operators this parser should recognize.
     */
    public OperationParser(
            @NonNull List<Operator> relevantOperators) {

        this.relevantOperatorOrders = Operator.getRelevantOrders(relevantOperators);

        for (int operatorOrder : relevantOperatorOrders) {

            List<Operator> operators = Operator.getRelevantOperatorsForOrder(operatorOrder, relevantOperators);
            Map<Character, Operator> operatorMap = new HashMap<>();

            for (Operator operator : operators)
                operatorMap.put(operator.getPattern(), operator);

            operatorsMap.put(operatorOrder,
                    new OperatorInformation(operatorMap));
        }
    }

    @Override
    public Optional<Operation> parse(
            @NonNull String equation,
            @NonNull ParsingOptions options) {

        try {

            String trimmedEquation = equation.trim();

            if (trimmedEquation.isEmpty())
                return Optional.empty();

            for (int relevantLevel : relevantOperatorOrders) {

                OperatorInformation operatorInformation = operatorsMap.get(relevantLevel);

                Optional<Operation> operation = tryParse(
                        trimmedEquation,
                        options,
                        operatorInformation.operators,
                        operatorInformation.operators::containsKey);

                if (operation.isPresent())
                    return operation;
            }

            return Optional.empty();

        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }

    private static Optional<Operation> tryParse(
            String equation,
            ParsingOptions options,
            Map<Character, Operator> relevantOperators,
            CheckFunction checkFunction) {

        int depth = 0;

        for (int i = equation.length() - 1; i >= 0; i--) {

            char c = equation.charAt(i);

            if (c == ')' || c == ']') {
                depth++;
                continue;
            }

            if (c == '(' || c == '[') {
                depth--;
                continue;
            }

            if (depth == 0 && checkFunction.check(c)) {

                String left = equation.substring(0, i);
                String right = equation.substring(i + 1);

                Optional<Equation> leftEquation = Equation.parse(left, options);
                Optional<Equation> rightEquation = Equation.parse(right, options);
                Operator parsedOperator = OperatorParser.parse(relevantOperators, c).orElseThrow();

                if (leftEquation.isEmpty() || rightEquation.isEmpty())
                    return Optional.empty();

                return Optional.of(new Operation(leftEquation.get(), rightEquation.get(), parsedOperator));
            }
        }

        return Optional.empty();
    }

    private interface CheckFunction {

        boolean check(char c);

    }

    private record OperatorInformation(
            Map<Character, Operator> operators
    ) {
    }
}
