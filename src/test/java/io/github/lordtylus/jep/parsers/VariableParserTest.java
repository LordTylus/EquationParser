package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableParserTest {

    @ParameterizedTest
    @CsvSource({
            "[1],[1]",
            "[abc],[abc]",
            "[a[b]c],[a[b]c]",
            "[ab][c],[ab][c]",
            "[Hallo] ,[Hallo]",
            "[ H a l l o ] ,[ H a l l o ]",
    })
    void parses(String equation, String expected) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Equation actual = VariableParser.INSTANCE.parse(equation, register).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "abc",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    })
    void doesNotParse(String equation) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Optional<? extends Equation> actual = VariableParser.INSTANCE.parse(equation, register);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}