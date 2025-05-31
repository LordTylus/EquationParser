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
package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.storages.SimpleStorage;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableTest {

    @Test
    void canOutputPatternEnglish() {

        /* Given */

        Variable sut = new Variable("Test 123");

        /* When */

        String actual = sut.toPattern(Locale.ENGLISH);

        /* Then */

        String expected = "[Test 123]";

        assertEquals(expected, actual);
    }

    @Test
    void canOutputPatternGerman() {

        /* Given */

        Variable sut = new Variable("Test 123");

        /* When */

        String actual = sut.toPattern(Locale.GERMAN);

        /* Then */

        String expected = "[Test 123]";

        assertEquals(expected, actual);
    }

    @Test
    void canEvaluateDouble() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        /* When */

        Result actual = sut.evaluate(storage);

        /* Then */

        double value = actual.asDouble();

        assertEquals(123.456789123456, value, 0.00001);
    }

    @Test
    void canEvaluateFloat() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        /* When */

        Result actual = sut.evaluate(storage);

        /* Then */

        double value = actual.asFloat();

        assertEquals(123.456789123456F, value, 0.00001);
    }

    @Test
    void canEvaluateInt() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        /* When */

        Result actual = sut.evaluate(storage);

        /* Then */

        int value = actual.asInt();

        assertEquals(123, value);
    }

    @Test
    void canEvaluateLong() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        /* When */

        Result actual = sut.evaluate(storage);

        /* Then */

        long value = actual.asLong();

        assertEquals(123, value);
    }

    @Test
    void printsPattern() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        Result result = sut.evaluate(storage);
        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        assertEquals("Test 123 = 123.456789123456\n", sb.toString());
    }

    @Test
    void toStaticEquationDisplaysCorrectString() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        Result result = sut.evaluate(storage);
        StringBuilder sb = new StringBuilder();

        /* When */

        result.toStaticEquation(sb);

        /* Then */

        assertEquals("123.456789123456", sb.toString());
    }

    @Test
    void toStringIsCorrect() {

        /* Given */

        Variable sut = new Variable("Test 123");

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("Test 123", 123.456789123456);

        Result result = sut.evaluate(storage);

        /* When */

        String actual = result.toDisplayString();

        /* Then */

        assertEquals("123.456789123456=123.456789123456", actual);
    }
}