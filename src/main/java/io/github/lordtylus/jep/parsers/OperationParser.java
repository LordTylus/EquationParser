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
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import io.github.lordtylus.jep.tokenizer.tokens.OperatorToken;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.TokenPair;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private final int lowest;
    private final Map<Character, Operator> characterToOperatorMap = new HashMap<>();

    /**
     * Creates a new Parser instance with the {@link Operator operators} to use for parsing.
     *
     * @param relevantOperators operators this parser should recognize.
     */
    public OperationParser(
            @NonNull Collection<Operator> relevantOperators) {

        int lowestOperatorOrder = Integer.MAX_VALUE;

        for (Operator relevantOperator : relevantOperators) {

            char pattern = relevantOperator.toPattern();

            characterToOperatorMap.put(pattern, relevantOperator);

            lowestOperatorOrder = Math.min(lowestOperatorOrder, relevantOperator.order());
        }

        this.lowest = lowestOperatorOrder;
    }

    @Override
    public ParseResult parse(
            @NonNull List<Token> tokenizedEquation,
            int startIndex,
            int endIndex,
            @NonNull ParsingOptions options) {

        try {

            if (endIndex - startIndex < 2)
                return ParseResult.notMine();

            int operatorIndex = -1;
            int order = Integer.MAX_VALUE;
            Operator operator = null;

            for (int i = endIndex; i >= startIndex; i--) {

                Token token = tokenizedEquation.get(i);

                /*
                 * Since we are reading from right to left, depth will be negative when
                 * we encounter ). If this happens we jump to the ( one to save calculation time.
                 * If ( is not found, we have a parenthesis mismatch.
                 *
                 * Furthermore, if we ever encounter a ( it will also not have an opening one,
                 * also meaning a parenthesis mismatch, because we should have jumped over it
                 * once we encountered the closing one.
                 *
                 * Instead of checking for ParenthesisToken, Token pair is checked, as it ensures
                 * this parser can handle custom TokenPairs also.
                 */
                if (token instanceof TokenPair tokenPair) {

                    TokenPair opening = tokenPair.getOpening();

                    if (opening == null)
                        return ParseResult.error("Token pair mismatch! Could not find opening!");

                    i = opening.getIndex();
                    continue;
                }

                if (token instanceof OperatorToken operatorToken) {

                    Operator currentOperator = characterToOperatorMap.get(operatorToken.operator());

                    if (currentOperator == null)
                        return ParseResult.error("Operator '" + operatorToken.operator() + "' not recognized!");

                    int thisOrder = currentOperator.order();

                    /* If this operator is lower than all others we found so far, then this operator is the new lowest. */
                    if (thisOrder < order) {
                        order = thisOrder;
                        operatorIndex = i;
                        operator = currentOperator;
                    }

                    /* we reached the lowest order possible, we can stop. We won't find anything that's lower. */
                    if (order == lowest)
                        break;
                }
            }

            /* If no operator was found, we are done here. */
            if (operatorIndex == -1)
                return ParseResult.notMine();

            int endLeft = operatorIndex - 1;
            int startRight = operatorIndex + 1;

            if (endLeft - startIndex < 0)
                return ParseResult.error("Left operand is empty!");

            if (endIndex - startRight < 0)
                return ParseResult.error("Right operand is empty!");

            ParseResult leftEquation = EquationParser.parseEquation(tokenizedEquation, startIndex, endLeft, options);

            if (leftEquation.getParseType() != ParseType.OK)
                return leftEquation;

            ParseResult rightEquation = EquationParser.parseEquation(tokenizedEquation, startRight, endIndex, options);

            if (rightEquation.getParseType() != ParseType.OK)
                return rightEquation;

            return ParseResult.ok(new Operation(leftEquation.getNullableEquation(), rightEquation.getNullableEquation(), operator));

        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }

    /**
     * This method figures out the symbols of all operators to be recognized by this parser.
     *
     * @return Set of characters for operator symbols.
     */
    public Set<Character> getOperatorCharacters() {
        return new HashSet<>(characterToOperatorMap.keySet());
    }
}
