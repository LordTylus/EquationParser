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
import io.github.lordtylus.jep.equation.Variable;
import io.github.lordtylus.jep.options.CustomParserOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
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

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        Equation actual = VariableParser.INSTANCE
                .parse(tokenized, 0, tokenized.size() - 1, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals(expected, actual.toPattern(Locale.ENGLISH));
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "abc",
            "[bc",
            "bc]",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    })
    void doesNotParse(String equation) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = EquationStringTokenizer.tokenize(equation, options);

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, tokenized.size() - 1, options);

        /* Then */

        assertNotEquals(ParseType.OK, actual.getParseType());
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "-1",
            "abc",
            "[bc",
            "bc]",
            "([Hallo])",
            "1+1",
            "sqrt([hallo])",
    })
    void doesNotParseTokens(String token) {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(new ValueToken(token));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
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

        ParseResult actual = VariableParser.INSTANCE
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

        ParseResult actual = VariableParser.INSTANCE
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
                new ValueToken("[hallo]"),
                new OperatorToken('+'),
                new ValueToken("3"));

        /* When */

        Equation actual = VariableParser.INSTANCE
                .parse(tokenized, 2, 2, options)
                .getEquation()
                .orElseThrow();

        /* Then */

        assertEquals("[hallo]", actual.toPattern(Locale.ENGLISH));
    }

    @Test
    void ignoresStringsThatAreTooLong() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test"),
                new ValueToken("Test"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 1, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoresStringsNotStartingWithOpenBracket() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("Test]"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void ignoresStringsNotEndingWithClosingBracket() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("[Test"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void returnsResultWithVariableNameIfOk() {

        /* Given */

        ParsingOptions options = ParsingOptions.defaultOptions();

        List<Token> tokenized = List.of(
                new ValueToken("[Test]"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Variable("Test"));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void canWorkWithBraces() {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.setVariablePattern(StandardVariablePatterns.BRACES);

        List<Token> tokenized = List.of(
                new ValueToken("{Test}"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Variable("Test"));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void doesNotParseBracketsIfBracesIsConfigured() {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.setVariablePattern(StandardVariablePatterns.BRACES);

        List<Token> tokenized = List.of(
                new ValueToken("[Test]"));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.notMine();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesStringAsIsIfNothingIsEscaped() {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.setVariablePattern(StandardVariablePatterns.NONE);

        List<Token> tokenized = List.of(
                new ValueToken(" [ T e s t ] "));

        /* When */

        ParseResult actual = VariableParser.INSTANCE
                .parse(tokenized, 0, 0, options);

        /* Then */

        ParseResult expected = ParseResult.ok(new Variable("[ T e s t ]"));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}