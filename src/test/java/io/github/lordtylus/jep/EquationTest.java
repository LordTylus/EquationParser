package io.github.lordtylus.jep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquationTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1;1",
            "110;110",
            "123.456;123.456",
            "123,456;123.456",
            "[hallo];[hallo]",
            "[hallo]+1;[hallo]+1",
            "123.44*3;123.44*3",
            "sqrt(4+3);sqrt(4+3)",
            "sqrt(9)+cbrt(8);sqrt(9)+cbrt(8)",
            "(sqrt(9)+cbrt(8))+4;(sqrt(9)+cbrt(8))+4",
            "1+2*3-4/2^2;1+2*3-4/2^2",
            "2+(s q r t(  9 )+ c  b r t (  8 ) ) + 4  ,  4;2+(sqrt(9)+cbrt(8))+4.4",
            "(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5;(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5",
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given / When */

        Equation actual = Equation.parse(equation).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "(1+1",
            "abc",
            "a#c",
            "lol([hallo])",
            "(2+((1+2)^2+(4+[hallo])^2))+(2*2))+5"
    })
    void doesNotParse(String equation) {

        /* Given / When */

        Optional<? extends Equation> actual = Equation.parse(equation);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}