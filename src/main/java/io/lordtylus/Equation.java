package io.lordtylus;

import io.lordtylus.equation.Variable;
import io.lordtylus.equation.parsers.EquationParser;
import io.lordtylus.equation.parsers.ParseException;
import io.lordtylus.equation.registers.EquationParserRegister;
import io.lordtylus.equation.storages.EmptyStorage;
import lombok.NonNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * This object represents a parsed equation string. Such as 2*(2+3)^[x]
 * <p>
 * The parsed equation will represent a binary tree and can be solved using the {@link #evaluate(Storage)} method.
 * <p>
 * The tree for the example equation will look like this:
 * <pre>
 *     *
 *    / \
 *   2   ^
 *      / \
 *     +   x
 *    / \
 *   2   3
 * </pre>
 * The tree will be solved bottom up to get the value. If x is 2 the result will be 50.
 */
public interface Equation {

    /**
     * Evaluates the parsed {@link Equation} using an {@link EmptyStorage empty storage} implementation.
     * <p>
     * If the equation consists of any {@link Variable} implementations, these variables will be solved with the value 0.0
     *
     * @return Result object with the result of the equation.
     */
    default Result evaluate() {
        return evaluate(EmptyStorage.INSTANCE);
    }

    /**
     * Evaluates the parsed {@link Equation} using the provided {@link Storage}.
     * <p>
     * The Storage can be used to evaluate variables if any are present.
     *
     * @param storage the {@link Storage} to be used to solve variables.
     * @return Result object with the result of the equation.
     * @throws NullPointerException If any given argument is null.
     */
    Result evaluate(
            @NonNull Storage storage);

    /**
     * Converts the parsed {@link Equation} back into a localized String pattern.
     * <p>
     * The Locale may be used by implementations for purposes of formatting numbers to the correct decimal points.
     *
     * @param locale the Locale to be used.
     * @throws NullPointerException If any given argument is null.
     */
    String toPattern(
            @NonNull Locale locale);

    /**
     * Parses the given equation String using the default {@link EquationParserRegister}.
     *
     * @param equation The equation to be parsed.
     * @return Optional with parsed {@link Equation} or null if equation could not be parsed.
     * @throws ParseException       If a {@link EquationParser parser} in the {@link EquationParserRegister register} encounters an unhandled exception. Under normal circumstances when something cannot be parsed, an empty Optional is returned. However, if the {@link EquationParser} implementation throws an exception for whatever reason, this exception will be returned instead.
     * @throws NullPointerException If any given argument is null.
     * @see EquationParserRegister#defaultRegister()
     */
    static Optional<Equation> parse(
            @NonNull String equation) {

        return parse(equation, EquationParserRegister.defaultRegister());
    }

    /**
     * Parses the given equation String using the specified {@link EquationParserRegister}.
     *
     * @param equation       The equation to be parsed.
     * @param parserRegister The {@link EquationParserRegister Register} containing the {@link EquationParser parsers} to be used to parse the given equation.
     * @return Optional with parsed {@link Equation} or null if equation could not be parsed.
     * @throws ParseException       If a {@link EquationParser parser} in the {@link EquationParserRegister register} encounters an unhandled exception. Under normal circumstances when something cannot be parsed, an empty Optional is returned. However, if the {@link EquationParser} implementation throws an exception for whatever reason, this exception will be returned instead.
     * @throws NullPointerException If any given argument is null.
     */
    static Optional<Equation> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister parserRegister) {

        for (EquationParser registeredParser : parserRegister.getRegisteredParsers()) {

            Optional<Equation> parsed = registeredParser.parse(equation, parserRegister)
                    .map(Function.identity());

            if (parsed.isPresent())
                return parsed;
        }

        return Optional.empty();
    }
}
