package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.registers.CustomRegister;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationParserTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1+2;1+2",
            "1.1+2,2;1.1+2.2",
            "123.1+223,2;123.1+223.2",
            "1-2;1-2",
            "1.1-2,2;1.1-2.2",
            "123.1-223,2;123.1-223.2",
            "1*2;1*2",
            "1.1*2,2;1.1*2.2",
            "123.1*223,2;123.1*223.2",
            "1/2;1/2",
            "1.1/2,2;1.1/2.2",
            "123.1/223,2;123.1/223.2",
            "1^2;1^2",
            "1.1^2,2;1.1^2.2",
            "123.1^223,2;123.1^223.2",
            "[Hallo]+[Moin];[Hallo]+[Moin]",
            "(10+20)*(10-20)/(20+30);(10+20)*(10-20)/(20+30)",
            "(10*20)-(10/20)+(20*30);(10*20)-(10/20)+(20*30)",
            "(10*20)-(10/20)^(20*30);(10*20)-(10/20)^(20*30)",
            "(10+20)+(10+20)+(20+30);(10+20)+(10+20)+(20+30)",
            "(10-20)-(10-20)-(20-30);(10-20)-(10-20)-(20-30)",
            "(10/20)/(10/20)/(20/30);(10/20)/(10/20)/(20/30)",
            "(10*20)*(10*20)*(20*30);(10*20)*(10*20)*(20*30)",
            "(10^20)^(10^20)^(20^30);(10^20)^(10^20)^(20^30)",
            "[H a l l o ]    +    [  M o i n ];[H a l l o ]+[  M o i n ]",
            "  1 2  3  . 1 / 2 2  3 , 2   ;123.1/223.2",
            "5+(6+(7+3));5+(6+(7+3))",
            "5-(6-(7-3));5-(6-(7-3))",
            "5*(6*(7*3));5*(6*(7*3))",
            "5/(6/(7/3));5/(6/(7/3))",
            "5^(6^(7^3));5^(6^(7^3))",
            "5+(6+(7-(7-3)));5+(6+(7-(7-3)))",
            "5+([1+2]+3);5+([1+2]+3)",
            "2*2+2;2*2+2",
            "2*(2+2);2*(2+2)",
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Equation actual = OperationParser.DEFAULT.parse(equation, register).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1+2;1+2",
            "1.1+2,2;1.1+2.2",
            "123.1+223,2;123.1+223.2",
            "1-2;1-2",
            "1.1-2,2;1.1-2.2",
            "123.1-223,2;123.1-223.2",
            "[Hallo]+[Moin];[Hallo]+[Moin]",
            "(10+20)+(10+20)+(20+30);(10+20)+(10+20)+(20+30)",
            "(10-20)-(10-20)-(20-30);(10-20)-(10-20)-(20-30)",
            "[H a l l o ]    +    [  M o i n ];[H a l l o ]+[  M o i n ]",
            "5+(6+(7+3));5+(6+(7+3))",
            "5-(6-(7-3));5-(6-(7-3))",
            "5+(6+(7-(7-3)));5+(6+(7-(7-3)))",
            "5+([1+2]+3);5+([1+2]+3)",
    }, delimiter = ';')
    void parsesOnlyPlusAndMinus(String equation, String expected) {

        /* Given */

        CustomRegister customRegister = CustomRegister.withDefaults();
        customRegister.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customRegister.register(sut);

        /* When */

        Equation actual = sut.parse(equation, customRegister).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1+2;1+2",
            "1.1+2,2;1.1+2.2",
            "123.1+223,2;123.1+223.2",
            "1^2;1^2",
            "1.1^2,2;1.1^2.2",
            "123.1^223,2;123.1^223.2",
            "[Hallo]+[Moin];[Hallo]+[Moin]",
            "(10+20)+(10+20)+(20+30);(10+20)+(10+20)+(20+30)",
            "(10^20)^(10^20)^(20^30);(10^20)^(10^20)^(20^30)",
            "[H a l l o ]    +    [  M o i n ];[H a l l o ]+[  M o i n ]",
            "5+(6+(7+3));5+(6+(7+3))",
            "5^(6^(7^3));5^(6^(7^3))",
            "5+([1+2]+3);5+([1+2]+3)",
            "2^2+2;2^2+2",
            "2^(2+2);2^(2+2)",
            "2+(2^2);2+(2^2)",
            "2+(2^(2^2));2+(2^(2^2))",
    }, delimiter = ';')
    void parsesOnlyPlusAndExponent(String equation, String expected) {

        /* Given */

        CustomRegister customRegister = CustomRegister.withDefaults();
        customRegister.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customRegister.register(sut);

        /* When */

        Equation actual = OperationParser.DEFAULT.parse(equation, customRegister).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "(1+1)",
            "(1+1",
            "((1+1)+(1+1))",
            "abc",
            "a#c",
            "([Hallo])",
            "sqrt([hallo])",
    })
    void doesNotParse(String equation) {

        /* Given */

        EquationParserRegister register = EquationParserRegister.defaultRegister();

        /* When */

        Optional<? extends Equation> actual = OperationParser.DEFAULT.parse(equation, register);

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "1+((1*1)+(1+1))",
            "(1+1",
            "1*2",
            "1/2",
            "2+(2*2)",
            "2*(2+2)",
            "((1+1)+(1+1))",
            "abc",
            "a#c",
            "([Hallo])",
            "sqrt([hallo])",
    })
    void doesNotParseOnlyPlusAndMinus(String equation) {

        /* Given */

        CustomRegister customRegister = CustomRegister.withDefaults();
        customRegister.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customRegister.register(sut);

        /* When */

        Optional<? extends Equation> actual = sut.parse(equation, customRegister);

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "1+((1*1)+(1+1))",
            "(1+1",
            "1-2",
            "1*2",
            "1/2",
            "2+(2*2)",
            "2*(2+2)",
            "((1+1)+(1+1))",
            "abc",
            "a#c",
            "([Hallo])",
            "sqrt([hallo])",
    })
    void doesNotParseOnlyPlusAndExponent(String equation) {

        /* Given */

        CustomRegister customRegister = CustomRegister.withDefaults();
        customRegister.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customRegister.register(sut);

        /* When */

        Optional<? extends Equation> actual = sut.parse(equation, customRegister);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}