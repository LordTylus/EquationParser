package io.github.lordtylus.jep;

/**
 * This demo shows how to work with negative numbers.
 * <p>
 * Similar to many other programs out there, negative numbers have to be put in parentheses to be parsed correctly.
 * <p>
 * In that form they behave like any other number.
 */
public class NegativeNumbersDemo {

    public static void main(String[] args) {

        String input = "2*(-3)^3";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //-54.0
    }
}
