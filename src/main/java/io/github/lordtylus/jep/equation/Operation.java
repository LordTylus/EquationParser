package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import io.github.lordtylus.jep.operators.Operator;
import lombok.NonNull;

import java.util.Locale;

public record Operation(
        @NonNull Equation left,
        @NonNull Equation right,
        @NonNull Operator operator) implements Equation {

    @Override
    public OperationResult evaluate(@NonNull Storage storage) {

        Result leftResult = left.evaluate(storage);
        Result rightResult = right.evaluate(storage);

        Number result = operator.evaluate(leftResult.result(), rightResult.result());

        return new OperationResult(operator, leftResult, rightResult, result);
    }

    @Override
    public String toPattern(@NonNull Locale locale) {
        return left.toPattern(locale) + operator.toPattern() + right.toPattern(locale);
    }

    public record OperationResult(
            @NonNull
            Operator operator,
            @NonNull
            Result leftResult,
            @NonNull
            Result rightResult,
            Number result
    ) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(leftResult.result())
                    .append(" ")
                    .append(operator.toPattern())
                    .append(" ")
                    .append(rightResult.result())
                    .append(" = ")
                    .append(result)
                    .append("\n");

            leftResult.print(sb, currentIndent + indent, indent);
            rightResult.print(sb, currentIndent + indent, indent);
        }
    }
}
