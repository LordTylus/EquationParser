package io.github.lordtylus.jep;

/**
 * This demo shows the basic function how to input and
 * parse a basic equation without any variables or special cases.
 * <p>
 * You will notice that parsing and evaluating the equation is quite simple.
 */
public class BasicEquationDemo {

    public static void main(String[] args) {

        String input = "(7+3)*(6-3)+216/3^3";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        Result result = equation.evaluate();

        System.out.println(result.asDouble()); //38.0
    }
}
