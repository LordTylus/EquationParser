package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import lombok.NonNull;

import java.util.Locale;

public record Variable(
        @NonNull String name
) implements Equation {

    @Override
    public VariableResult evaluate(@NonNull Storage storage) {

        Number result = storage.evaluate(name);

        return new VariableResult(name, result);
    }

    @Override
    public String toPattern(@NonNull Locale locale) {
        return "[" + name + "]";
    }

    public record VariableResult(
            @NonNull String name,
            Number result) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(name)
                    .append(" = ")
                    .append(result)
                    .append("\n");
        }
    }

}
