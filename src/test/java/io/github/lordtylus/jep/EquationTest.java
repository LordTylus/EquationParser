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

import io.github.lordtylus.jep.storages.SimpleStorage;
import org.junit.jupiter.api.Test;
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
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3;(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3"
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given / When */

        Equation actual = Equation.parse(equation).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
//            "1 ; 1",
//            "-  1 ;-1",
//            " [a b c]  ; [a b c]",
//            " 1 + 1  ; 1+1",
//            " 1 + 1 ^ 2 ; 1+1^2",
//            " - 1 + 1 ^ 2  ; -1+1^2",
//            " 2 *   ( 2 + 3 ) ^ 2 ; 2*(2+3)^2",
//            "   2 * ( 2  +  3 ) ^ ( 1 / 2  ) ; 2*(2+3)^(1/2)",
            "   2 * (   2   +   3   ) ^ (  - 1 / 2 )  ; 2*(2+3)^(-1/2)",
//            " 2   * s q r   t ( 2 + 3 ) ^ 2 ; 2*sqrt(2+3)^2",
//            "  2 * s q r t  ( 2 + 3 ) ^ l o g ( 1/ 2 )  ; 2*sqrt(2+3)^log(1/2)",
//            " (  - 3 )  + a b s (7   . 3 + 3  ) *  s i  n ( 6 +  ( [ h a l   l  o ] - 2 ) )   + 2 1 6 /    3^ 3 ; (-3)+abs(7.3+3)*sin(6+([ h a l   l  o ]-2))+216/3^3",
//            "  (  ( ( 1 + 2   ) * ( 3 + 4 ) )+  ((  ( 1 - 2  ) ^ 2) + 5) )  ; (((1+2)*(3+4))+(((1-2)^2)+5))",
    }, delimiter = ';')
    void parsesSpaces(String equation, String expected) {

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
            "-1+-1^2",
            "lol([hallo])",
            "(2+((1+2)^2+(4+[hallo])^2))+(2*2))+5",
            "(((1+2)*(3+4))+(((1-2)^2)+))",
            "(  ( ( 1 + 2   ) * ( 3 + 4 ) )+  ((  ( 1 - 2  ) ^ 2) + ) )",
            "2*((2-4)hallo)",
    })
    void doesNotParse(String equation) {

        /* Given / When */

        Optional<? extends Equation> actual = Equation.parse(equation);

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

        Equation sut = Equation.parse(equation).orElseThrow();

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

        Equation sut = Equation.parse(equation).orElseThrow();

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

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").orElseThrow();

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

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").orElseThrow();

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

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").orElseThrow();

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

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").orElseThrow();

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

        Equation sut = Equation.parse("(2+((1+2)^2+(4+[hallo])^2)+(2*2))+5").orElseThrow();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("hallo", 4);

        Result result = sut.evaluate(storage);

        /* When */

        String actual = result.toDisplayString();

        /* Then */

        assertEquals("(2.0+((1.0+2.0)^2.0+(4.0+4)^2.0)+(2.0*2.0))+5.0=84.0", actual);
    }
}