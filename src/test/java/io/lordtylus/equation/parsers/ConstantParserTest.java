package io.lordtylus.equation.parsers;

import io.lordtylus.Equation;
import io.lordtylus.equation.registers.EquationParserRegister;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantParserTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1;1",
            "-1;-1",
            "1.2;1.2",
            "1,2;1.2",
            "-1.2;-1.2",
            "124.345;124.345",
            "-1234,2345;-1234.2345",
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Equation actual = ConstantParser.INSTANCE.parse(equation, register).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "abc",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    })
    void doesNotParse(String equation) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Optional<? extends Equation> actual = ConstantParser.INSTANCE.parse(equation, register);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}