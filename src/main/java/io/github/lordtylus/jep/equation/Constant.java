package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import io.github.lordtylus.jep.parsers.ConstantParser;
import lombok.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This part of an {@link Equation} represents a constant double value.
 * <p>
 * The constant value is parsed using {@link ConstantParser}
 */
public record Constant(
        double value
) implements Equation {

    private static final String DECIMAL_FORMAT = "0.#####";

    @Override
    public ConstantResult evaluate(@NonNull Storage storage) {
        return new ConstantResult(value);
    }

    @Override
    public String toPattern(@NonNull Locale locale) {

        NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT,
                DecimalFormatSymbols.getInstance(locale));

        return formatter.format(value);
    }

    public record ConstantResult(Number result) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(result)
                    .append(" = ")
                    .append(result)
                    .append("\n");
        }
    }
}
