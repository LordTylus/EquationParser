package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.equation.Variable;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser implementation parses the given input string and extracts the variable name from it.
 * <p>
 * Supported are variables in brackets [] such as [My Fancy Variable]. the variable name is
 * case-sensitive, and spaces will remain in place.
 * <p>
 * If the string does not begin with [ or end with ] (after trimming) parsing fails and an
 * empty optional is returned. This means Strings such as [hey]+[you] can be parsed to hey]+[you.
 * The order in which the parsers are executed ensures this does not happen. And if it does,
 * this suggests there is an error in the setup when custom parsers are used.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VariableParser implements EquationParser {

    /**
     * Singleton immutable instance of the {@link VariableParser}
     */
    public static final VariableParser INSTANCE = new VariableParser();

    private static final Pattern NAME_PATTERN = Pattern.compile("^\\[(?<name>.+)]$", Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<Variable> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register) {

        try {

            String trimmedEquation = equation.trim();

            Matcher matcher = NAME_PATTERN.matcher(trimmedEquation);

            if (!matcher.matches())
                return Optional.empty();

            return Optional.of(new Variable(matcher.group("name")));

        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
