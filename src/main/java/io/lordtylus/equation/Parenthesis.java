package io.lordtylus.equation;

import io.lordtylus.Equation;
import io.lordtylus.Result;
import io.lordtylus.Storage;
import io.lordtylus.equation.functions.MathFunction;
import lombok.NonNull;

import java.util.Locale;

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
    }
}
