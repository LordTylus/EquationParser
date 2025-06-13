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
import io.github.lordtylus.jep.equation.Constant;
import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.CustomParsingOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import io.github.lordtylus.jep.tokenizer.EquationStringTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.OperatorToken;
import io.github.lordtylus.jep.tokenizer.tokens.ParenthesisToken;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        Equation actual = OperationParser.DEFAULT
                .parse(tokenized, 0, tokenized.size() - 1, options)
                .getEquation()
                .orElseThrow();

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

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customParsingOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParsingOptions);

        /* When */

        Equation actual = sut
                .parse(tokenized, 0, tokenized.size() - 1, customParsingOptions)
                .getEquation()
                .orElseThrow();

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

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParsingOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParsingOptions);

        /* When */

        Equation actual = sut
                .parse(tokenized, 0, tokenized.size() - 1, customParsingOptions)
                .getEquation()
                .orElseThrow();

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

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParsingOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParsingOptions);

        /* When */

        ParseResult actual = sut
                .parse(tokenized, 0, 0, customParsingOptions);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
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

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
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

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.SUB));
        customParsingOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParsingOptions);

        /* When */

        ParseResult actual = sut
                .parse(tokenized, 0, 0, customParsingOptions);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
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

        CustomParsingOptions customParsingOptions = CustomParsingOptions.withDefaults();
        customParsingOptions.unregister(OperationParser.DEFAULT);

        OperationParser sut = new OperationParser(List.of(StandardOperators.ADD, StandardOperators.POW));
        customParsingOptions.register(sut);

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, customParsingOptions);

        /* When */

        ParseResult actual = sut
                .parse(tokenized, 0, 0, customParsingOptions);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void doesNotParseWhenLeftIsEmpty() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new OperatorToken('+'),
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("1"));

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void doesNotParseWhenRightIsEmpty() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new OperatorToken('+'),
                new ValueToken("1"),
                new OperatorToken('+'));

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void parsesSublist() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('+'),
                new ValueToken("2"),
                new OperatorToken('+'),
                new ValueToken("3"),
                new OperatorToken('+'),
                new ValueToken("4"));

        /* When */

        Equation actual = OperationParser.DEFAULT
                .parse(tokenized, 2, 4, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals("2+3", actual.toPattern(Locale.ENGLISH));
    }

    @Test
    void ignoresListsThatAreTooSmall() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test"));

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenParenthesisDontMatch() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('+'),
                new ValueToken("2"),
                new ParenthesisToken(')'));

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        ParseResult expected = ParseResult.error("Token pair mismatch! Could not find opening!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void notMineWhenNoOperatorOutsideParenthesesWasFound() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("1"),
                new OperatorToken('+'),
                new ValueToken("2"),
                token2);

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 4, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenLeftOperandIsEmpty() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new OperatorToken('+'),
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("2")
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        ParseResult expected = ParseResult.error("Left operand is empty!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorOnRightOperandBeingEmpty() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("2"),
                new OperatorToken('+')
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        ParseResult expected = ParseResult.error("Right operand is empty!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void okWhenOperationIsValid() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("2")
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Operation(new Constant(1), new Constant(2), StandardOperators.MULT));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void returnsErrorIfLeftOperandIsInvalid() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1.1.1"),
                new OperatorToken('*'),
                new ValueToken("2")
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Multiple decimal points!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void returnsErrorIfRightOperandIsInvalid() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("test")
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("This expression doesn't resemble an equation!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void returnsErrorIfOperatorIsNotRecognized() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('#'),
                new ValueToken("2")
        );

        /* When */

        ParseResult actual = OperationParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Operator '#' not recognized!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void throwsParseExceptionWhenRuntimeErrorOccurred() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('*'),
                new ValueToken("2")
        );

        /* When */

        Executable result = () -> OperationParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        assertThrows(ParseException.class, result);
    }
}