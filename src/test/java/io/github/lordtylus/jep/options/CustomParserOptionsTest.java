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

import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomParserOptionsTest {

    @Test
    void createsEmpty() {

        /* Given */

        CustomParserOptions sut = CustomParserOptions.empty();

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

        CustomParserOptions sut = CustomParserOptions.withDefaults();

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
    }

    @Test
    void registersOneParser() {

        /* Given */

        CustomParserOptions sut = CustomParserOptions.empty();

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

        CustomParserOptions sut = CustomParserOptions.empty();
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

        CustomParserOptions sut = CustomParserOptions.empty();

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

        CustomParserOptions sut = CustomParserOptions.empty();
        sut.register(ParenthesisTokenizer.DEFAULT);

        /* When */

        Executable result = () -> sut.register(ParenthesisTokenizer.DEFAULT);

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void registersASecondTokenizer() {

        /* Given */

        CustomParserOptions sut = CustomParserOptions.empty();
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

        CustomParserOptions sut = CustomParserOptions.withDefaults();

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

        CustomParserOptions sut = CustomParserOptions.withDefaults();

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

        CustomParserOptions sut = CustomParserOptions.withDefaults();

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

        CustomParserOptions sut = CustomParserOptions.withDefaults();

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

        CustomParserOptions sut = CustomParserOptions.empty();

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
    void removingATokenizerThatIsNotAddedWontAlterAnything() {

        /* Given */

        CustomParserOptions sut = CustomParserOptions.empty();
        sut.register(VariableTokenizer.INSTANCE);

        EquationTokenizer toRemove = new EquationTokenizer() {
            @Override
            public Set<Character> getDelimiters() {
                return Set.of('[');
            }

            @Override
            public boolean handle(int beginIndex, int currentIndex, char currentCharacter, String equation, List<Token> tokenList, TokenizerContext context) {
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
}