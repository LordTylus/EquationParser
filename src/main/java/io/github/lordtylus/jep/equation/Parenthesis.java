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
package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.StandardFunctions;
import lombok.NonNull;

import java.util.Locale;

/**
 * This part of an equation serves the same function as the parentheses in mathematics.
 * It wraps a different equation called inner, which has to be evaluated first.
 * <p>
 * Additionally, this object may also have a {@link MathFunction} such as square
 * root to perform on the inner equation after its evaluation.
 * <p>
 * Unlike {@link Operation operations} who create a binary tree which only has exactly 2 children, this class only ever will have one child.
 * <p>
 * The {@link MathFunction} is required, but if no calculation is desired {@link StandardFunctions#NOP} can be provided.
 */
public record Parenthesis(
        @NonNull MathFunction function,
        @NonNull Equation inner
) implements Equation {

    @Override
    public ParenthesisResult evaluate(@NonNull Storage storage) {

        Result innerResult = inner.evaluate(storage);

        Number result = function.evaluate(innerResult.result());

        return new ParenthesisResult(function, innerResult, result);
    }

    @Override
    public String toPattern(@NonNull Locale locale) {
        return function.toPattern() + "(" + inner.toPattern(locale) + ")";
    }

    /**
     * This record represents the result of the evaluation of the {@link Parenthesis} class.
     */
    public record ParenthesisResult(
            @NonNull
            MathFunction function,
            @NonNull
            Result innerResult,
            Number result
    ) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(function.toPattern())
                    .append(" ( ")
                    .append(innerResult.result())
                    .append(" ) = ")
                    .append(result)
                    .append("\n");

            innerResult.print(sb, currentIndent + indent, indent);
        }

        @Override
        public void toStaticEquation(@NonNull StringBuilder sb) {
            sb.append(function.toPattern());
            sb.append("(");
            innerResult.toStaticEquation(sb);
            sb.append(")");
        }

        @Override
        public String toString() {
            return toDisplayString();
        }
    }
}
