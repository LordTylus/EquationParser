package io.lordtylus.equation;

import io.lordtylus.Result;
import io.lordtylus.equation.storages.SimpleStorage;
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
}