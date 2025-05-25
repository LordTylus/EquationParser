package io.github.lordtylus.jep;

/**
 * Unless a custom implementation is provided this framework can only work with square and cube roots.
 * <p>
 * If possible and feasible for the required use case you can use a fractional power for higher level roots.
 * <p>
 * This example shows how the 5th root will be calculated.
 */
public class HigherRootsDemo {

    public static void main(String[] args) {

        String input = "32^(1/5)";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //2.0
    }
}
