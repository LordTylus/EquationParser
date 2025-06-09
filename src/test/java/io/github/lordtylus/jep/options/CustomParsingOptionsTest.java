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
package io.github.lordtylus.jep.options;

import io.github.lordtylus.jep.functions.StandardFunctions;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.options.ParsingOptions.ErrorBehavior;
import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.tokenizer.EquationTokenizer;
import io.github.lordtylus.jep.tokenizer.OperatorTokenizer;
import io.github.lordtylus.jep.tokenizer.ParenthesisTokenizer;
import io.github.lordtylus.jep.tokenizer.TokenizerContext;
import io.github.lordtylus.jep.tokenizer.VariableTokenizer;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomParsingOptionsTest {

    @Test
    void createsEmpty() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        List<EquationParser> actual1 = sut.getRegisteredParsers();
        List<EquationTokenizer> actual2 = sut.getRegisteredTokenizers();

        /* Then */

        assertTrue(actual1.isEmpty());
        assertTrue(actual2.isEmpty());
    }

    @Test
    void createsNewWithDefault() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        List<EquationParser> actual1 = sut.getRegisteredParsers();
        List<EquationTokenizer> actual2 = sut.getRegisteredTokenizers();

        /* Then */

        List<EquationParser> expected1 = List.of(
                ParenthesisParser.DEFAULT,
                OperationParser.DEFAULT,
                ConstantParser.INSTANCE,
                VariableParser.INSTANCE
        );

        List<EquationTokenizer> expected2 = List.of(
                VariableTokenizer.INSTANCE,
                ParenthesisTokenizer.DEFAULT,
                OperatorTokenizer.DEFAULT
        );

        assertEquals(actual1, expected1);
        assertEquals(actual2, expected2);
        assertEquals(ErrorBehavior.ERROR_RESULT, sut.getErrorBehavior());
        assertEquals(StandardVariablePatterns.BRACKETS, sut.getVariablePattern());
    }

    @Test
    void createWithOperatorsVararg() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.defaultWith(
                StandardOperators.ADD, StandardOperators.SUB
        );

        /* When / Then */

        CustomParsingOptions expected = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(
                List.of(StandardOperators.ADD, StandardOperators.SUB)
        );

        expected.register(new ParenthesisParser(StandardFunctions.all()));
        expected.register(operationParser);
        expected.register(ConstantParser.INSTANCE);
        expected.register(VariableParser.INSTANCE);
        expected.register(VariableTokenizer.INSTANCE);
        expected.register(ParenthesisTokenizer.DEFAULT);
        expected.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        assertThat(sut).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createWithOperatorsCollection() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.defaultWithOperators(
                List.of(StandardOperators.ADD, StandardOperators.SUB)
        );

        /* When / Then */

        CustomParsingOptions expected = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(
                List.of(StandardOperators.ADD, StandardOperators.SUB)
        );

        expected.register(new ParenthesisParser(StandardFunctions.all()));
        expected.register(operationParser);
        expected.register(ConstantParser.INSTANCE);
        expected.register(VariableParser.INSTANCE);
        expected.register(VariableTokenizer.INSTANCE);
        expected.register(ParenthesisTokenizer.DEFAULT);
        expected.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        assertThat(sut).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createWithFunctionsVararg() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.defaultWith(
                StandardFunctions.SIN, StandardFunctions.ASIN
        );

        /* When / Then */

        CustomParsingOptions expected = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(StandardOperators.all());

        expected.register(new ParenthesisParser(List.of(StandardFunctions.SIN, StandardFunctions.ASIN)));
        expected.register(operationParser);
        expected.register(ConstantParser.INSTANCE);
        expected.register(VariableParser.INSTANCE);
        expected.register(VariableTokenizer.INSTANCE);
        expected.register(ParenthesisTokenizer.DEFAULT);
        expected.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        assertThat(sut).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createWithFunctionCollection() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.defaultWithFunctions(
                List.of(StandardFunctions.SIN, StandardFunctions.ASIN)
        );

        /* When / Then */

        CustomParsingOptions expected = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(StandardOperators.all());

        expected.register(new ParenthesisParser(List.of(StandardFunctions.SIN, StandardFunctions.ASIN)));
        expected.register(operationParser);
        expected.register(ConstantParser.INSTANCE);
        expected.register(VariableParser.INSTANCE);
        expected.register(VariableTokenizer.INSTANCE);
        expected.register(ParenthesisTokenizer.DEFAULT);
        expected.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        assertThat(sut).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createWithFunctionAndOperatorCollection() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.defaultWith(
                List.of(StandardFunctions.SIN, StandardFunctions.ASIN),
                List.of(StandardOperators.ADD, StandardOperators.SUB)
        );

        /* When / Then */

        CustomParsingOptions expected = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(
                List.of(StandardOperators.ADD, StandardOperators.SUB)
        );

        expected.register(new ParenthesisParser(List.of(StandardFunctions.SIN, StandardFunctions.ASIN)));
        expected.register(operationParser);
        expected.register(ConstantParser.INSTANCE);
        expected.register(VariableParser.INSTANCE);
        expected.register(VariableTokenizer.INSTANCE);
        expected.register(ParenthesisTokenizer.DEFAULT);
        expected.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        assertThat(sut).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void registersOneParser() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        sut.register(ConstantParser.INSTANCE);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                ConstantParser.INSTANCE
        );

        assertEquals(actual, expected);
    }

    @Test
    void registersASecondParser() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();
        sut.register(ConstantParser.INSTANCE);

        /* When */

        sut.register(ParenthesisParser.DEFAULT);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                ConstantParser.INSTANCE,
                ParenthesisParser.DEFAULT
        );

        assertEquals(actual, expected);
    }

    @Test
    void registersOneTokenizer() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        sut.register(ParenthesisTokenizer.DEFAULT);

        /* Then */

        List<EquationTokenizer> actual = sut.getRegisteredTokenizers();

        List<EquationTokenizer> expected = List.of(
                ParenthesisTokenizer.DEFAULT
        );

        assertEquals(actual, expected);
    }

    @Test
    void cannotRegisterTheSameTokenizerTwice() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();
        sut.register(ParenthesisTokenizer.DEFAULT);

        /* When */

        Executable result = () -> sut.register(ParenthesisTokenizer.DEFAULT);

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void registersASecondTokenizer() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();
        sut.register(ParenthesisTokenizer.DEFAULT);

        /* When */

        sut.register(OperatorTokenizer.DEFAULT);

        /* Then */

        List<EquationTokenizer> actual = sut.getRegisteredTokenizers();

        List<EquationTokenizer> expected = List.of(
                ParenthesisTokenizer.DEFAULT,
                OperatorTokenizer.DEFAULT
        );

        assertEquals(actual, expected);
    }

    @Test
    void canUnregisterAParser() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.unregister(ParenthesisParser.DEFAULT);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                OperationParser.DEFAULT,
                ConstantParser.INSTANCE,
                VariableParser.INSTANCE
        );

        assertEquals(actual, expected);
    }

    @Test
    void canUnregisterATokenizer() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.unregister(ParenthesisTokenizer.DEFAULT);

        /* Then */

        List<EquationTokenizer> actual = sut.getRegisteredTokenizers();

        List<EquationTokenizer> expected = List.of(
                VariableTokenizer.INSTANCE,
                OperatorTokenizer.DEFAULT
        );

        assertEquals(actual, expected);
    }

    @Test
    void returnsDefaultDelimiterMapping() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        /* Then */

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('(', ParenthesisTokenizer.DEFAULT);
        expected.put(')', ParenthesisTokenizer.DEFAULT);
        expected.put('[', VariableTokenizer.INSTANCE);
        expected.put(']', VariableTokenizer.INSTANCE);
        expected.put('*', OperatorTokenizer.DEFAULT);
        expected.put('+', OperatorTokenizer.DEFAULT);
        expected.put('-', OperatorTokenizer.DEFAULT);
        expected.put('/', OperatorTokenizer.DEFAULT);
        expected.put('^', OperatorTokenizer.DEFAULT);

        assertEquals(actual, expected);
    }

    @Test
    void returnsDefaultDelimiterMappingAfterUnregister() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.unregister(VariableTokenizer.INSTANCE);

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('(', ParenthesisTokenizer.DEFAULT);
        expected.put(')', ParenthesisTokenizer.DEFAULT);
        expected.put('*', OperatorTokenizer.DEFAULT);
        expected.put('+', OperatorTokenizer.DEFAULT);
        expected.put('-', OperatorTokenizer.DEFAULT);
        expected.put('/', OperatorTokenizer.DEFAULT);
        expected.put('^', OperatorTokenizer.DEFAULT);

        assertEquals(actual, expected);
    }

    @Test
    void mappingUpdatesAfterAddingTokenizer() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        sut.register(VariableTokenizer.INSTANCE);

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('[', VariableTokenizer.INSTANCE);
        expected.put(']', VariableTokenizer.INSTANCE);

        assertEquals(actual, expected);
    }

    @Test
    void mappingUpdatesAfterChangeOfVariablePattern() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.setVariablePattern(StandardVariablePatterns.TAGS);

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('<', VariableTokenizer.INSTANCE);
        expected.put('>', VariableTokenizer.INSTANCE);
        expected.put('(', ParenthesisTokenizer.DEFAULT);
        expected.put(')', ParenthesisTokenizer.DEFAULT);
        expected.put('*', OperatorTokenizer.DEFAULT);
        expected.put('+', OperatorTokenizer.DEFAULT);
        expected.put('-', OperatorTokenizer.DEFAULT);
        expected.put('/', OperatorTokenizer.DEFAULT);
        expected.put('^', OperatorTokenizer.DEFAULT);

        assertEquals(actual, expected);
    }

    @Test
    void whenVariablesUseSameOpeningAndClosingCharacterItOnlyAppearsOnceInMap() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.setVariablePattern(new VariablePattern(true, ':', ':'));

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put(':', VariableTokenizer.INSTANCE);
        expected.put('(', ParenthesisTokenizer.DEFAULT);
        expected.put(')', ParenthesisTokenizer.DEFAULT);
        expected.put('*', OperatorTokenizer.DEFAULT);
        expected.put('+', OperatorTokenizer.DEFAULT);
        expected.put('-', OperatorTokenizer.DEFAULT);
        expected.put('/', OperatorTokenizer.DEFAULT);
        expected.put('^', OperatorTokenizer.DEFAULT);

        assertEquals(actual, expected);
    }

    @Test
    void whenVariablesDontHaveSpecialPatternTheyWontShowUp() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.withDefaults();

        /* When */

        sut.setVariablePattern(StandardVariablePatterns.NONE);

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('(', ParenthesisTokenizer.DEFAULT);
        expected.put(')', ParenthesisTokenizer.DEFAULT);
        expected.put('*', OperatorTokenizer.DEFAULT);
        expected.put('+', OperatorTokenizer.DEFAULT);
        expected.put('-', OperatorTokenizer.DEFAULT);
        expected.put('/', OperatorTokenizer.DEFAULT);
        expected.put('^', OperatorTokenizer.DEFAULT);

        assertEquals(actual, expected);
    }

    @Test
    void removingATokenizerThatIsNotAddedWontAlterAnything() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();
        sut.register(VariableTokenizer.INSTANCE);

        EquationTokenizer toRemove = new EquationTokenizer() {
            @Override
            public Set<Character> getDelimitersFor(ParsingOptions parsingOptions) {
                return Set.of('[');
            }

            @Override
            public boolean handle(int beginIndex, int currentIndex, char currentCharacter,
                    String equation, List<Token> tokenList, TokenizerContext context, ParsingOptions options) {
                return false;
            }
        };

        /* When */

        sut.unregister(toRemove);

        /* Then */

        Map<Character, EquationTokenizer> actual = sut.getTokenizerForDelimiterMap();

        Map<Character, EquationTokenizer> expected = new HashMap<>();
        expected.put('[', VariableTokenizer.INSTANCE);
        expected.put(']', VariableTokenizer.INSTANCE);

        assertEquals(actual, expected);
    }

    @Test
    void canSetThrowsExceptionsIsFalseByDefault() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        ErrorBehavior errorBehavior = sut.getErrorBehavior();

        /* Then */

        assertEquals(ErrorBehavior.ERROR_RESULT, errorBehavior);
    }

    @Test
    void canSetThrowsExceptions() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        sut.setErrorBehavior(ErrorBehavior.EXCEPTION);

        /* Then */

        ErrorBehavior actual = sut.getErrorBehavior();

        assertEquals(ErrorBehavior.EXCEPTION, actual);
    }

    @Test
    void usesBracketsForVariablesByDefault() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        VariablePattern actual = sut.getVariablePattern();

        /* Then */

        assertEquals(StandardVariablePatterns.BRACKETS, actual);
    }

    @Test
    void canSetVariablePattern() {

        /* Given */

        CustomParsingOptions sut = CustomParsingOptions.empty();

        /* When */

        sut.setVariablePattern(StandardVariablePatterns.BRACES);

        /* Then */

        VariablePattern actual = sut.getVariablePattern();

        assertEquals(StandardVariablePatterns.BRACES, actual);
    }
}