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
package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.CustomParserOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.EquationStringTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationParserTest {

    @Test
    void returnsDefaultOperatorPatterns() {

        /* Given */

        OperationParser sut = OperationParser.DEFAULT;

        /* When */

        Set<Character> actual = sut.getOperatorCharacters();

        /* Then */

        Set<Character> expected = Set.of('+', '-', '*', '/', '^');

        assertEquals(actual, expected);
    }

    @Test
    void returnsCustomOperatorPatterns() {

        /* Given */

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));

        /* When */

        Set<Character> actual = sut.getOperatorCharacters();

        /* Then */

        Set<Character> expected = Set.of('+', '-');

        assertEquals(actual, expected);
    }

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

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Equation actual = OperationParser.DEFAULT.parse(tokenized, options).orElseThrow();

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

        CustomParserOptions customParserOptions = CustomParserOptions.withDefaults();
        customParserOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customParserOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParserOptions);

        /* When */

        Equation actual = sut.parse(tokenized, customParserOptions).orElseThrow();

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

        CustomParserOptions customParserOptions = CustomParserOptions.withDefaults();
        customParserOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParserOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParserOptions);

        /* When */

        Equation actual = sut.parse(tokenized, customParserOptions).orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1-2;1-2",
            "1.1-2,2;1.1-2.2",
            "123.1-223,2;123.1-223.2",
            "1*2;1*2",
            "1.1*2,2;1.1*2.2",
            "123.1*223,2;123.1*223.2",
            "1/2;1/2",
            "1.1/2,2;1.1/2.2",
            "123.1/223,2;123.1/223.2",
            "(10+20)*(10-20)/(20+30);(10+20)*(10-20)/(20+30)",
            "(10*20)-(10/20)+(20*30);(10*20)-(10/20)+(20*30)",
            "(10*20)-(10/20)^(20*30);(10*20)-(10/20)^(20*30)",
            "(10-20)-(10-20)-(20-30);(10-20)-(10-20)-(20-30)",
            "(10/20)/(10/20)/(20/30);(10/20)/(10/20)/(20/30)",
            "(10*20)*(10*20)*(20*30);(10*20)*(10*20)*(20*30)",
            "  1 2  3  . 1 / 2 2  3 , 2   ;123.1/223.2",
            "5-(6-(7-3));5-(6-(7-3))",
            "5*(6*(7*3));5*(6*(7*3))",
            "5/(6/(7/3));5/(6/(7/3))",
            "5+(6+(7-(7-3)));5+(6+(7-(7-3)))",
            "2*2+2;2*2+2",
            "2*(2+2);2*(2+2)",
    }, delimiter = ';')
    void whenOnlyPlusAndExponentTheRestIsNotParsed(String equation) {

        /* Given */

        CustomParserOptions customParserOptions = CustomParserOptions.withDefaults();
        customParserOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParserOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParserOptions);

        /* When */

        Optional<? extends Equation> actual = sut.parse(tokenized, customParserOptions);

        /* Then */

        assertTrue(actual.isEmpty());
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

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Optional<? extends Equation> actual = OperationParser.DEFAULT.parse(tokenized, options);

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

        CustomParserOptions customParserOptions = CustomParserOptions.withDefaults();
        customParserOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customParserOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParserOptions);

        /* When */

        Optional<? extends Equation> actual = sut.parse(tokenized, customParserOptions);

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

        CustomParserOptions customParserOptions = CustomParserOptions.withDefaults();
        customParserOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParserOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParserOptions);

        /* When */

        Optional<? extends Equation> actual = sut.parse(tokenized, customParserOptions);

        /* Then */

        assertTrue(actual.isEmpty());
    }
}