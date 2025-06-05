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
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import io.github.lordtylus.jep.tokenizer.EquationStringTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.OperatorToken;
import io.github.lordtylus.jep.tokenizer.tokens.ParenthesisToken;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Equation actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, tokenized.size() - 1, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "+",
            "abc",
            "1.2.3",
            "1,2.3",
            "+1",
            "1-1",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    }, delimiter = ';')
    void doesNotParse(String equation) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, tokenized.size() - 1, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "+",
            "abc",
            "1.2.3",
            "1,2.3",
            "+1",
            "1-1",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    }, delimiter = ';')
    void doesNotParseTokens(String token) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(new ValueToken(token));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void doesNotAcceptOperators() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(new OperatorToken('+'));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void doesNotAcceptParenthesisTokens() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(new ParenthesisToken('('));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @Test
    void doesNotAcceptBlankStrings() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(new ValueToken("   "));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
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
                new ValueToken("3"));

        /* When */

        Equation actual = ConstantParser.INSTANCE
                .parse(tokenized, 2, 2, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals("2", actual.toPattern(Locale.US));
    }

    @Test
    void blankStringWillBeAnError() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("  "));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.error("Constant is empty!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void multipleDecimalsInStringWillBeAnError() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1.2.3"));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.error("Multiple decimal points!");

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoresStringsThatAreNotNumbers() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test"));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoresStringsThatAreTooLong() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test"),
                new ValueToken("Test"));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 1, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void numbers0To9Supported() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("1234567890"));

        /* When */

        ParseResult actual = ConstantParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Constant(1234567890));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}