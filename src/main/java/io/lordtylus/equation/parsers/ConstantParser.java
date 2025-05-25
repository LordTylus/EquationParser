package io.lordtylus.equation.parsers;

import io.lordtylus.equation.Constant;
import io.lordtylus.equation.registers.EquationParserRegister;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser implementation parses the given input string as a double.
 * <p>
 * Supported are both positive and negative numbers, such as 23 or -34.
 * Decimal Numbers are also supported, and it will use common decimal separators (, and .) when parsing.
 * That way bow 23.45 and -34,23 can be parsed correctly.
 * <p>
 * In the event any other decimal separator, letter or symbol reaches this class, parsing fails and an empty optional is returned.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantParser implements EquationParser {

    public static final ConstantParser INSTANCE = new ConstantParser();

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+([.,]\\d+)?$");

    @Override
    public Optional<Constant> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register) {

        try {

            equation = WHITESPACE_PATTERN.matcher(equation)
                    .replaceAll("");

            Matcher matcher = DECIMAL_PATTERN.matcher(equation);

            if (!matcher.matches())
                return Optional.empty();

            equation = equation.replace(",", ".");

            return Optional.of(new Constant(Double.parseDouble(equation)));

        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
