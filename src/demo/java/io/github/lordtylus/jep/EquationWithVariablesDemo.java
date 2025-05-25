package io.github.lordtylus.jep;

import io.github.lordtylus.jep.storages.SimpleStorage;

/**
 * This demo shows how to work with variables in the default configuration.
 * Variables are passed using brackets such as [x] and can take any value.
 * <p>
 * The current value of x is stored in the SimpleStorage implementation and
 * passed into the evaluate method.
 * <p>
 * The equation only needs to be parsed once, and can be used as many times as needed.
 * The default implementations of this framework are stateless and thread safe.
 * <p>
 * However, if the content of the storage changes during evaluation the result will
 * be undefined, and can be potentially wrong.
 */
public class EquationWithVariablesDemo {

    public static void main(String[] args) {

        String input = "2*[x]^2+5";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        SimpleStorage storage = new SimpleStorage();

        for (int i = 0; i < 5; i++) {

            storage.putValue("x", i);

            Result result = equation.evaluate(storage);

            System.out.println(result.asDouble()); //5, 7, 13, 23, 37
        }
    }
}
