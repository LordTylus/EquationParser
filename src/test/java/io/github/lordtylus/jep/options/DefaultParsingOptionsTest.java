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
import io.github.lordtylus.jep.tokenizer.VariableTokenizer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultParsingOptionsTest {

    @Test
    void containsCorrectParsers() {

        /* Given */

        DefaultParsingOptions sut = DefaultParsingOptions.INSTANCE;

        /* When */

        List<EquationParser> actual = sut.getRegisteredParsers();

        /* Then */

        List<EquationParser> expected = List.of(
                ParenthesisParser.DEFAULT,
                OperationParser.DEFAULT,
                ConstantParser.INSTANCE,
                VariableParser.INSTANCE
        );

        assertEquals(actual, expected);
    }

    @Test
    void doesNotThrowExceptionsOnError() {

        /* Given */

        DefaultParsingOptions sut = DefaultParsingOptions.INSTANCE;

        /* When */

        ErrorBehavior actual = sut.getErrorBehavior();

        /* Then */

        assertEquals(ErrorBehavior.ERROR_RESULT, actual);
    }

    @Test
    void usesBracketsForVariables() {

        /* Given */

        DefaultParsingOptions sut = DefaultParsingOptions.INSTANCE;

        /* When */

        VariablePattern actual = sut.getVariablePattern();

        /* Then */

        assertEquals(StandardVariablePatterns.BRACKETS, actual);
    }

    @Test
    void containsCorrectTokenizers() {

        /* Given */

        DefaultParsingOptions sut = DefaultParsingOptions.INSTANCE;

        /* When */

        List<EquationTokenizer> actual = sut.getRegisteredTokenizers();

        /* Then */

        List<EquationTokenizer> expected = List.of(
                VariableTokenizer.INSTANCE,
                ParenthesisTokenizer.DEFAULT,
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
}