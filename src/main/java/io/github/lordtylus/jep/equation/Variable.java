package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import io.github.lordtylus.jep.parsers.VariableParser;
import lombok.NonNull;

import java.util.Locale;

/**
 * This part of an {@link Equation} represents a variable which pulls
 * its current value from the passed {@link Storage} during evaluation.
 * <p>
 * To not interfere with over parts of the equation variable names are
 * passed in [] and extracted during parsing using {@link VariableParser}
 */
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
