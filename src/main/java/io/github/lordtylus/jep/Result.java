package io.github.lordtylus.jep;

import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import lombok.NonNull;

/**
 * This interface represents the Result of the evaluation of an {@link Equation}
 * <p>
 * It is expected that each implementation of {@link Equation} provides its own Result implementation such as {@link Operation Operation} and {@link Operation.OperationResult OperationResult}
 * <p>
 * The Results value is just a Number and the implementations of the equation determine which type of number it will be. The default implementations of this framework only work with doubles, but if the accuracy of floating point numbers proves insufficient, custom implementations of {@link Equation} could be added utilizing a custom {@link EquationParserRegister ParserRegister}.
 * <p>
 * It is recommended that the implementations of this interface utilize records to maintain immutability.
 */
public interface Result {

    /**
     * This value represents the value of the Result as a number
     *
     * @return the unaltered number result.
     */
    Number result();

    /**
     * The results value as int. If {@link #result()} would return a non-integer value, the number is converted to integer by omitting the decimal places.
     *
     * @return the result as int
     */
    default int asInt() {
        return result().intValue();
    }

    /**
     * The results value as long. If {@link #result()} would return a non-integer value, the number is converted to long by omitting the decimal places.
     *
     * @return the result as long
     */
    default long asLong() {
        return result().longValue();
    }

    /**
     * The results value as float. If {@link #result()} a conversion may lead to loss in precision.
     *
     * @return the result as float
     */
    default float asFloat() {
        return result().floatValue();
    }

    /**
     * The results value as float. If {@link #result()} a conversion may lead to loss in precision.
     * The default implementation of the {@link Equation Equations} in this framework work with doubles, meaning there shouldn't be any additional downsides using it, unless custom implementations with higher precision are used.
     *
     * @return the result as double
     */
    default double asDouble() {
        return result().doubleValue();
    }

    /**
     * This method offers a fast and simple way of getting debug information of the evaluation result.
     * <p>
     * An Equation  2*(2+[x])+[y]^2 would output something like:
     * <pre>
     *     10 + 9 = 19
     *       2 * 5 = 10
     *         2 = 2
     *         x = 3
     *         3 ^ 2 = 9
     *       y = 3
     *         2 = 2
     * </pre>
     * <p>
     * The more complex the equation is the bigger the indentation.
     *
     * @param sb StringBuilder the output should be printed to.
     * @throws NullPointerException If any given argument is null.
     */
    default void print(@NonNull StringBuilder sb) {
        print(sb, "");
    }

    /**
     * This method serves the same function as {@link #print(StringBuilder)}. This Method, however allows to adjust the initial indentation when printing.
     * Printing of nested Results remain unaffected.
     *
     * @param sb            StringBuilder the output should be printed to.
     * @param currentIndent initial indent for printing.
     * @throws NullPointerException If any given argument is null.
     */
    default void print(
            @NonNull StringBuilder sb,
            @NonNull String currentIndent) {
        print(sb, currentIndent, "  ");
    }

    /**
     * This method serves the same function as {@link #print(StringBuilder, String)}. However, this method allows to manipulate the way nested Results are printed.
     * <p>
     * For example, it is possible to change the display of 2*(2+[x])+[y]^2 from
     *
     * <pre>
     *     10 + 9 = 19
     *       2 * 5 = 10
     *         2 = 2
     *         x = 3
     *         3 ^ 2 = 9
     *       y = 3
     *         2 = 2
     * </pre>
     * to
     * <pre>
     *     10 + 9 = 19
     *     --- 2 * 5 = 10
     *     --- --- 2 = 2
     *     --- --- x = 3
     *     --- 3 ^ 2 = 9
     *     --- --- y = 3
     *     --- --- 2 = 2
     * </pre>
     * by providing "--- " as indent
     *
     * @param sb            StringBuilder the output should be printed to.
     * @param currentIndent initial indent for printing.
     * @param indent        indent to be used for nested equations.
     * @throws NullPointerException If any given argument is null.
     */
    void print(
            @NonNull StringBuilder sb,
            @NonNull String currentIndent,
            @NonNull String indent);
}
