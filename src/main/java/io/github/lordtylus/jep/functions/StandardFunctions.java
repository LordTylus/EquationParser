package io.github.lordtylus.jep.functions;

import io.github.lordtylus.jep.Equation;

import java.util.List;
import java.util.function.Function;

/**
 * This class contains a set of default functions to be used for solving {@link Equation eqations}.
 * All the functions provided convert the number object to a double first before performing the calculations.
 */
public class StandardFunctions {

    /**
     * No function, returns the same number
     */
    public static final MathFunction NOP = new MathFunction("", value -> value);
    /**
     * Returns absolute value of number.
     *
     * @see Math#abs(double)
     */
    public static final MathFunction ABS = new MathFunction("abs", fromDouble(Math::abs));
    /**
     * Returns sine of number.
     *
     * @see Math#sin(double)
     */
    public static final MathFunction SIN = new MathFunction("sin", fromDouble(Math::sin));
    /**
     * Returns arc sine of number.
     *
     * @see Math#asin(double)
     */
    public static final MathFunction ASIN = new MathFunction("asin", fromDouble(Math::asin));
    /**
     * Returns hyperbolic sine of number.
     *
     * @see Math#sinh(double)
     */
    public static final MathFunction SINH = new MathFunction("sinh", fromDouble(Math::sinh));
    /**
     * Returns cosine of number.
     *
     * @see Math#cos(double)
     */
    public static final MathFunction COS = new MathFunction("cos", fromDouble(Math::cos));
    /**
     * Returns arc cosine of number.
     *
     * @see Math#acos(double)
     */
    public static final MathFunction ACOS = new MathFunction("acos", fromDouble(Math::acos));
    /**
     * Returns hyperbolic cosine of number.
     *
     * @see Math#cosh (double)
     */
    public static final MathFunction COSH = new MathFunction("cosh", fromDouble(Math::cosh));
    /**
     * Returns tangent of number.
     *
     * @see Math#tan(double)
     */
    public static final MathFunction TAN = new MathFunction("tan", fromDouble(Math::tan));
    /**
     * Returns arc tangent of number.
     *
     * @see Math#atan(double)
     */
    public static final MathFunction ATAN = new MathFunction("atan", fromDouble(Math::atan));
    /**
     * Returns hyperbolic tangent of number.
     *
     * @see Math#tanh(double)
     */
    public static final MathFunction TANH = new MathFunction("tanh", fromDouble(Math::tanh));
    /**
     * Returns e to given power.
     *
     * @see Math#exp(double)
     */
    public static final MathFunction EXP = new MathFunction("exp", fromDouble(Math::exp));
    /**
     * Returns natural log of number.
     *
     * @see Math#log(double)
     */
    public static final MathFunction LOG = new MathFunction("log", List.of("ln"), fromDouble(Math::log));
    /**
     * Returns base 10 log of number.
     *
     * @see Math#log10(double)
     */
    public static final MathFunction LOG10 = new MathFunction("log10", fromDouble(Math::log10));
    /**
     * Rounds number down.
     *
     * @see Math#floor(double)
     */
    public static final MathFunction FLOOR = new MathFunction("floor", fromDouble(Math::floor));
    /**
     * Rounds to the closed whole number.
     *
     * @see Math#round(double)
     */
    public static final MathFunction ROUND = new MathFunction("round", a -> (double) Math.round(a.doubleValue()));
    /**
     * Rounds number up.
     *
     * @see Math#ceil(double)
     */
    public static final MathFunction CEIL = new MathFunction("ceil", fromDouble(Math::ceil));
    /**
     * Returns positive square root of number.
     *
     * @see Math#sqrt(double)
     */
    public static final MathFunction SQRT = new MathFunction("sqrt", fromDouble(Math::sqrt));
    /**
     * Returns qube root of number.
     *
     * @see Math#cbrt(double)
     */
    public static final MathFunction CBRT = new MathFunction("cbrt", fromDouble(Math::cbrt));

    private static final List<MathFunction> ALL = List.of(
            NOP, ABS, SIN, ASIN, SINH, COS, ACOS,
            COSH, TAN, ATAN, TANH, EXP, LOG,
            LOG10, FLOOR, ROUND, CEIL, SQRT, CBRT
    );

    /**
     * Returns all standard {@link MathFunction functions} defined in this class.
     */
    public static List<MathFunction> all() {
        return ALL;
    }

    private static Function<Number, Number> fromDouble(Function<Double, Double> function) {
        return number -> function.apply(number.doubleValue());
    }
}
