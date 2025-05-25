package io.github.lordtylus.jep.operators;

import io.github.lordtylus.jep.Equation;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class contains a set of default operators to be used for solving {@link Equation eqations}.
 * All the operators provided convert the number object to a double first before performing the calculations.
 */
public class StandardOperators {

    /**
     * Adds number B to A (A+B)
     */
    public static final Operator ADD = new Operator(0, '+', fromDouble(Double::sum));
    /**
     * Subtracts number A from B (A-B)
     */
    public static final Operator SUB = new Operator(0, '-', fromDouble((a, b) -> a - b));
    /**
     * Multiplies number A with B (A*B)
     */
    public static final Operator MULT = new Operator(1, '*', fromDouble((a, b) -> a * b));
    /**
     * Divides number A with B (A/B)
     */
    public static final Operator DIV = new Operator(1, '/', fromDouble((a, b) -> a / b));
    /**
     * Raises number A to the power of B (A^B)
     */
    public static final Operator POW = new Operator(2, '^', fromDouble(Math::pow));

    private static final List<Operator> ALL = List.of(ADD, SUB, MULT, DIV, POW);

    /**
     * Returns all standard {@link Operator operators} defined in this class.
     */
    public static List<Operator> all() {
        return ALL;
    }

    private static BiFunction<Number, Number, Number> fromDouble(BiFunction<Double, Double, Double> function) {
        return (a, b) -> function.apply(a.doubleValue(), b.doubleValue());
    }
}
