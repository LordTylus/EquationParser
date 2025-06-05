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
import io.github.lordtylus.jep.equation.Parenthesis;
import io.github.lordtylus.jep.functions.StandardFunctions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParenthesisParserTest {

    @ParameterizedTest
    @CsvSource(value = {
            "(1+2);(1+2)",
            "abs(1+2);abs(1+2)",
            "sin(1+2);sin(1+2)",
            "asin(1+2);asin(1+2)",
            "sinh(1+2);sinh(1+2)",
            "SiNh(1+2);sinh(1+2)",
            "cos(1+2);cos(1+2)",
            "acos(1+2);acos(1+2)",
            "cosh(1+2);cosh(1+2)",
            "tan(1+2);tan(1+2)",
            "atan(1+2);atan(1+2)",
            "tanh(1+2);tanh(1+2)",
            "exp(1+2);exp(1+2)",
            "log(1+2);log(1+2)",
            "log10(1+2);log10(1+2)",
            "floor(1+2);floor(1+2)",
            "round(1+2);round(1+2)",
            " r o u n d ( 1 + 2 );round(1+2)",
            "ceil(1+2);ceil(1+2)",
            "sqrt(1+2);sqrt(1+2)",
            "cbrt(1+2);cbrt(1+2)",
            "rad(1+2);rad(1+2)",
            "deg(1+2);deg(1+2)",
            "((1+2)*(2+3));((1+2)*(2+3))",
            "(([hallo]+2)*(2+[Hal lo]));(([hallo]+2)*(2+[Hal lo]))"
    }, delimiter = ';')
    void parses(String equation, String expected) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Equation actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, tokenized.size() - 1, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "1+1",
            "1+1)",
            "(1+1",
            "(1+1(",
            ")1+1",
            "lol(1+1",
            "lol1+1)",
            "lol(123+333)",
            "1*((1+2)lol)",
            "a#c",
            "[hallo]",
    })
    void doesNotParse(String equation) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void parsesSublist() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken opening = new ParenthesisToken('(');
        ParenthesisToken closing = new ParenthesisToken(')');
        opening.setClosing(closing);

        List<Token> tokenized = List.of(
                new ValueToken("1"),
                new OperatorToken('+'),
                opening,
                new ValueToken("2"),
                new OperatorToken('+'),
                new ValueToken("3"),
                closing,
                new OperatorToken('+'),
                new ValueToken("4"));

        /* When */

        Equation actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 2, 6, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals("(2+3)", actual.toPattern(Locale.ENGLISH));
    }

    @Test
    void ignoresListsThatAreTooSmall() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test"));

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoresThingsNotStartingWithParenthesis() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("2"),
                new OperatorToken('+'),
                new ValueToken("3")
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenFirstParenthesisIsNotOpening() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ParenthesisToken(')'),
                new ValueToken("test"),
                new ParenthesisToken(')')
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Parenthesis mismatch, '(' expected!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenFirstParenthesisDoesntKnowItsClosingOne() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ParenthesisToken('('),
                new ValueToken("test"),
                new ParenthesisToken(')')
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Parenthesis mismatch, ')' missing!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoreIfDoesntEndWithClosingParenthesis() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');

        token1.setClosing(token2);

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("test"),
                token2,
                new OperatorToken('+')
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenLastParenthesisIsOpeningOne() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');

        token1.setClosing(token2);

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("test"),
                token2,
                new OperatorToken('+'),
                new ParenthesisToken('(')
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 4, options);

        /* Then */

        ParseResult expected = ParseResult.error("Parenthesis mismatch, ')' missing!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoreIfStartAndEndDontMatch() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        ParenthesisToken token3 = new ParenthesisToken('(');
        ParenthesisToken token4 = new ParenthesisToken(')');
        token3.setClosing(token4);

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("test"),
                token2,
                new OperatorToken('+'),
                token3,
                new ValueToken("test"),
                token4
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 6, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void errorWhenFunctionUnknown() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        token1.setFunction("mod");

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("test"),
                token2
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Unknown function 'mod'!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void throwsErrorOfInnerWhenEncountered() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        token1.setFunction("log");

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("1.2.3"),
                token2
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("Multiple decimal points!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void throwsErrorCouldNotParseIfStringIsNotEquation() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        token1.setFunction("log");

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("test"),
                token2
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.error("This expression doesn't resemble an equation!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void returnsOkWhenEverythingIsInOrder() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        token1.setFunction("log");

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("123"),
                token2
        );

        /* When */

        ParseResult actual = ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 2, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Parenthesis(StandardFunctions.LOG, new Constant(123)));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void throwsParseErrorWhenRuntimeErrorOccurred() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken(')');
        token1.setClosing(token2);

        List<Token> tokenized = List.of(
                token1,
                new ValueToken("123"),
                token2
        );

        /* When */

        Executable result = () -> ParenthesisParser.DEFAULT
                .parse(tokenized, 0, 3, options);

        /* Then */

        assertThrows(ParseException.class, result);
    }
}