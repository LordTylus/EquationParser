package io.github.lordtylus.jep;

import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.registers.CustomRegister;

import java.util.List;

/**
 * This demo shows how to manipulate the parsing by adding custom functionality.
 * In this case only constant values such as 2.0 or 14 are valid, and the only
 * operators allowed are addition and subtraction.
 * <p>
 * When working with custom settings the parserRegister has to be passed
 * into the Equation to bypass the default behavior.
 */
public class CustomParsersDemo {

    public static void main(String[] args) {

        CustomRegister parserRegister = CustomRegister.empty();

        parserRegister.register(ConstantParser.INSTANCE);
        parserRegister.register(new OperationParser(List.of(
                StandardOperators.ADD, StandardOperators.SUB
        )));

        var equation1 = Equation.parse("2+4-3", parserRegister);
        System.out.println(equation1.isPresent()); //true

        var equation2 = Equation.parse("2*4-3", parserRegister);
        System.out.println(equation2.isPresent()); //false
    }
}
