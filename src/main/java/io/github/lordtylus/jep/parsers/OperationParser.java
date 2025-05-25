package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.operators.Operator;
import io.github.lordtylus.jep.operators.OperatorParser;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.registers.EquationParserRegister;
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
            List<Character> operatorPatterns = operators.stream()
                    .map(Operator::getPattern)
                    .toList();

            operatorsMap.put(operatorOrder,
                    new OperatorInformation(operators, operatorPatterns));
        }
    }

    @Override
    public Optional<Operation> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register) {

        try {

            String trimmedEquation = equation.trim();

            for (int relevantLevel : relevantOperatorOrders) {

                OperatorInformation operatorInformation = operatorsMap.get(relevantLevel);

                Optional<Operation> operation = tryParse(
                        trimmedEquation,
                        register,
                        operatorInformation.operators,
                        operatorInformation.patterns::contains);

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
            EquationParserRegister register,
            List<Operator> relevantOperators,
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

                Optional<Equation> leftEquation = Equation.parse(left, register);
                Optional<Equation> rightEquation = Equation.parse(right, register);
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
            List<Operator> operators,
            List<Character> patterns
    ) {
    }
}
