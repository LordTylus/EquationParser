/*
  Copyright 2025 Martin Rökker

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package io.github.lordtylus.jep;

import io.github.lordtylus.jep.options.CustomParsingOptions;
import io.github.lordtylus.jep.options.ParsingOptions.ErrorBehavior;
import io.github.lordtylus.jep.parsers.ParseException;
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.storages.SimpleStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

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
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3;(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3"
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given / When */

        Equation actual = Equation.parse(equation).get();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-  1 ;-1",
            " [a b c]  ; [a b c]",
            " 1 + 1  ; 1+1",
            " 1 + 1 ^ 2 ; 1+1^2",
            " - 1 + 1 ^ 2  ; -1+1^2",
            " 2 *   ( 2 + 3 ) ^ 2 ; 2*(2+3)^2",
            "   2 * ( 2  +  3 ) ^ ( 1 / 2  ) ; 2*(2+3)^(1/2)",
            "   2 * (   2   +   3   ) ^ (  - 1 / 2 )  ; 2*(2+3)^(-1/2)",
            " 2   * s q r   t ( 2 + 3 ) ^ 2 ; 2*sqrt(2+3)^2",
            "  2 * s q r t  ( 2 + 3 ) ^ l o g ( 1/ 2 )  ; 2*sqrt(2+3)^log(1/2)",
            " (  - 3 )  + a b s (7   . 3 + 3  ) *  s i  n ( 6 +  ( [ h a l   l  o ] - 2 ) )   + 2 1 6 /    3^ 3 ; (-3)+abs(7.3+3)*sin(6+([ h a l   l  o ]-2))+216/3^3",
            "  (  ( ( 1 + 2   ) * ( 3 + 4 ) )+  ((  ( 1 - 2  ) ^ 2) + 5) )  ; (((1+2)*(3+4))+(((1-2)^2)+5))",
    }, delimiter = ';')
    void parsesSpaces(String equation, String expected) {

        /* Given / When */

        Equation actual = Equation.parse(equation).get();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "{hallo};[hallo]",
            "{ha+llo}+1;[ha+llo]+1",
            "{ h a l l o }+1;[ h a l l o ]+1",
    }, delimiter = ';')
    void parsesDifferentVariableStyle(String equation, String expected) {

        /* Given / When */

        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(StandardVariablePatterns.BRACES);

        Equation actual = Equation.parse(equation, parsingOptions).get();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "hallo;[hallo]",
            "hallo+1;[hallo]+1",
            " h a l l o +1;[h a l l o]+1",
    }, delimiter = ';')
    void parsesVariablesWithNoStyle(String equation, String expected) {

        /* Given / When */

        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(StandardVariablePatterns.NONE);

        Equation actual = Equation.parse(equation, parsingOptions).get();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "(1+1",
            "abc",
            "a#c",
            "-1+-1^2",
            "lol([hallo])",
            "(2+((1+2)^2+(4+[hallo])^2))+(2*2))+5",
            "(((1+2)*(3+4))+(((1-2)^2)+))",
            "(  ( ( 1 + 2   ) * ( 3 + 4 ) )+  ((  ( 1 - 2  ) ^ 2) + ) )",
            "2*((2-4)hallo)",
    })
    void doesNotParse(String equation) {

        /* Given / When */

        Optional<? extends Equation> actual = Equation.parse(equation).asOptional();

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;1",
            "110;110",
            "123.456;123.456",
            "[hallo];0",
            "[hallo]+1;1",
            "123.44*3;370.32",
            "sqrt(4+5);3",
            "sqrt(9)+cbrt(8);5",
            "(sqrt(9)+cbrt(8))+4;9",
            "1+2*3-4/2^2;6",
            "(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5;36",
            "(7+3)*(6-3)+216/3^3;38",
            "(1+8)*(4-1)+16/2^3;29",
    }, delimiter = ';')
    void canBeEvaluated(String equation, double expected) {

        /* Given */

        Equation sut = Equation.parse(equation).get();

        /* When */

        double actual = sut.evaluate().asDouble();

        /* Then */

        assertEquals(expected, actual, 0.00001);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "[hallo];4",
            "[hallo]+1;5",
            "(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5;84",
    }, delimiter = ';')
    void canBeEvaluatedWithStorage(String equation, double expected) {

        /* Given */

        Equation sut = Equation.parse(equation).get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        /* When */

        double actual = sut.evaluate(storage).asDouble();

        /* Then */

        assertEquals(expected, actual, 0.00001);
    }

    @Test
    void printsComplexResult() {

        /* Given */

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);

        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        String expected = """
                79.0 + 5.0 = 84.0
                   ( 79.0 ) = 79.0
                    75.0 + 4.0 = 79.0
                      2.0 + 73.0 = 75.0
                        2.0 = 2.0
                         ( 73.0 ) = 73.0
                          9.0 + 64.0 = 73.0
                            3.0 ^ 2.0 = 9.0
                               ( 3.0 ) = 3.0
                                1.0 + 2.0 = 3.0
                                  1.0 = 1.0
                                  2.0 = 2.0
                              2.0 = 2.0
                            8.0 ^ 2.0 = 64.0
                               ( 8.0 ) = 8.0
                                4.0 + 4 = 8.0
                                  4.0 = 4.0
                                  hallo = 4
                              2.0 = 2.0
                       ( 4.0 ) = 4.0
                        2.0 * 2.0 = 4.0
                          2.0 = 2.0
                          2.0 = 2.0
                  5.0 = 5.0
                """;

        assertEquals(expected, sb.toString());
    }

    @Test
    void printsComplexResultDifferentStartIndentation() {

        /* Given */

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);

        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb, "=");

        /* Then */

        String expected = """
                =79.0 + 5.0 = 84.0
                =   ( 79.0 ) = 79.0
                =    75.0 + 4.0 = 79.0
                =      2.0 + 73.0 = 75.0
                =        2.0 = 2.0
                =         ( 73.0 ) = 73.0
                =          9.0 + 64.0 = 73.0
                =            3.0 ^ 2.0 = 9.0
                =               ( 3.0 ) = 3.0
                =                1.0 + 2.0 = 3.0
                =                  1.0 = 1.0
                =                  2.0 = 2.0
                =              2.0 = 2.0
                =            8.0 ^ 2.0 = 64.0
                =               ( 8.0 ) = 8.0
                =                4.0 + 4 = 8.0
                =                  4.0 = 4.0
                =                  hallo = 4
                =              2.0 = 2.0
                =       ( 4.0 ) = 4.0
                =        2.0 * 2.0 = 4.0
                =          2.0 = 2.0
                =          2.0 = 2.0
                =  5.0 = 5.0
                """;

        assertEquals(expected, sb.toString());
    }

    @Test
    void printsComplexResultDifferentIndentationSymbols() {

        /* Given */

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);

        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb, "", "--- ");

        /* Then */

        String expected = """
                79.0 + 5.0 = 84.0
                ---  ( 79.0 ) = 79.0
                --- --- 75.0 + 4.0 = 79.0
                --- --- --- 2.0 + 73.0 = 75.0
                --- --- --- --- 2.0 = 2.0
                --- --- --- ---  ( 73.0 ) = 73.0
                --- --- --- --- --- 9.0 + 64.0 = 73.0
                --- --- --- --- --- --- 3.0 ^ 2.0 = 9.0
                --- --- --- --- --- --- ---  ( 3.0 ) = 3.0
                --- --- --- --- --- --- --- --- 1.0 + 2.0 = 3.0
                --- --- --- --- --- --- --- --- --- 1.0 = 1.0
                --- --- --- --- --- --- --- --- --- 2.0 = 2.0
                --- --- --- --- --- --- --- 2.0 = 2.0
                --- --- --- --- --- --- 8.0 ^ 2.0 = 64.0
                --- --- --- --- --- --- ---  ( 8.0 ) = 8.0
                --- --- --- --- --- --- --- --- 4.0 + 4 = 8.0
                --- --- --- --- --- --- --- --- --- 4.0 = 4.0
                --- --- --- --- --- --- --- --- --- hallo = 4
                --- --- --- --- --- --- --- 2.0 = 2.0
                --- --- ---  ( 4.0 ) = 4.0
                --- --- --- --- 2.0 * 2.0 = 4.0
                --- --- --- --- --- 2.0 = 2.0
                --- --- --- --- --- 2.0 = 2.0
                --- 5.0 = 5.0
                """;

        assertEquals(expected, sb.toString());
    }

    @Test
    void toStaticEquationDisplaysCorrectString() {

        /* Given */

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);
        StringBuilder sb = new StringBuilder();

        /* When */

        result.toStaticEquation(sb);

        /* Then */

        assertEquals("(2.0+((1.0+2.0)^2.0+(4.0+4)^2.0)+(2.0*2.0))+5.0", sb.toString());
    }

    @Test
    void toStringIsCorrect() {

        /* Given */

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);

        /* When */

        String actual = result.toDisplayString();

        /* Then */

        assertEquals("(2.0+((1.0+2.0)^2.0+(4.0+4)^2.0)+(2.0*2.0))+5.0=84.0", actual);
    }

    @Test
    void throwsExceptionIfOptionsDemandItOnParseError() {

        /* Given */

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.setErrorBehavior(ErrorBehavior.EXCEPTION);

        /* When */

        Executable result = () -> Equation.parse("(1+1", customParsingOptions);

        /* Then */

        assertThrows(ParseException.class, result);
    }

    @Test
    void doesntThrowExceptionIfParsingIsSuccessful() {

        /* Given */

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.setErrorBehavior(ErrorBehavior.EXCEPTION);

        /* When */

        Executable result = () -> Equation.parse("(1+1)", customParsingOptions);

        /* Then */

        assertDoesNotThrow(result);
    }

    @Test
    void returnsEquationOptionalWithException() {

        /* Given */

        CustomParsingOptions customParsingOptionsSpy = spy(CustomParsingOptions.withDefaults());

        Throwable throwable = new NullPointerException("Test");
        doThrow(throwable).when(customParsingOptionsSpy).getRegisteredParsers();

        /* When */

        EquationOptional actual = Equation.parse("(1+1)", customParsingOptionsSpy);

        /* Then */

        assertEquals(throwable, actual.getThrowable());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "[hello] ; true ; [ ; ] ; true",
            "[h e l l o] ; true ; [ ; ] ; true",
            "[ hello ] ; true ; [ ; ] ; true",
            "[h+o] ; true ; [ ; ] ; true",
            "[123] ; true ; [ ; ] ; true",
            "[h[1-1]o] ; true ; [ ; ] ; true",
            "[h]1-1[o] ; true ; [ ; ] ; false",
            ":hello: ; true ; : ; : ; true",
            ":h e l l o: ; true ; : ; : ; true",
            ": hello : ; true ; : ; : ; true",
            ":h+o: ; true ; : ; : ; true",
            ":123: ; true ; : ; : ; true",
            ":h:1-1:o: ; true ; [ ; ] ; false",
            "hello ; false ; 0 ; 0 ; true",
            "h e l l o ; false ; 0 ; 0 ; true",
            "h+o ; false ; 0 ; 0 ; true",
            "123 ; false ; 0 ; 0 ; true",
    }, delimiter = ';')
    void parsesVariableDemos(String equation, boolean isEscaped, char opening, char closing, boolean expected) {

        /* Given */

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.setVariablePattern(new VariablePattern(isEscaped, opening, closing));

        /* When */

        boolean actual = Equation.parse(equation, customParsingOptions).isPresent();

        /* Then */

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "[hello] ; true ; [ ; ] ; [hello]",
            "[h e l l o] ; true ; [ ; ] ; [h e l l o]",
            "[ hello ] ; true ; [ ; ] ; [ hello ]",
            "[h+o] ; true ; [ ; ] ; [h+o]",
            "[123] ; true ; [ ; ] ; [123]",
            "[h[1-1]o] ; true ; [ ; ] ; [h[1-1]o]",
            ":hello: ; true ; : ; : ; [hello]",
            ":h e l l o: ; true ; : ; : ; [h e l l o]",
            ": hello : ; true ; : ; : ; [ hello ]",
            ":h+o: ; true ; : ; : ; [h+o]",
            ":123: ; true ; : ; : ; [123]",
            "hello ; false ; 0 ; 0 ; [hello]",
            "h e l l o ; false ; 0 ; 0 ; [h e l l o]",
            "h+o ; false ; 0 ; 0 ; [h]+[o]",
            "123 ; false ; 0 ; 0 ; 123",
    }, delimiter = ';')
    void parsesVariableDemos(String equation, boolean isEscaped, char opening, char closing, String expected) {

        /* Given */

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.setVariablePattern(new VariablePattern(isEscaped, opening, closing));

        /* When */

        String actual = Equation.parse(equation, customParsingOptions).get().toPattern(Locale.ENGLISH);

        /* Then */

        assertEquals(expected, actual);
    }
}