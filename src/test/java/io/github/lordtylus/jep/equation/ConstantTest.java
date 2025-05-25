package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Result;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantTest {

    @Test
    void canOutputPatternEnglish() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        String actual = sut.toPattern(Locale.ENGLISH);

        /* Then */

        String expected = "123.45679";

        assertEquals(expected, actual);
    }

    @Test
    void canOutputPatternGerman() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        String actual = sut.toPattern(Locale.GERMAN);

        /* Then */

        String expected = "123,45679";

        assertEquals(expected, actual);
    }

    @Test
    void canEvaluateDouble() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asDouble();

        assertEquals(123.456789123456, value, 0.00001);
    }

    @Test
    void canEvaluateFloat() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asFloat();

        assertEquals(123.456789123456F, value, 0.00001);
    }

    @Test
    void canEvaluateInt() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        int value = actual.asInt();

        assertEquals(123, value);
    }

    @Test
    void canEvaluateLong() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        long value = actual.asLong();

        assertEquals(123, value);
    }

    @Test
    void printsPattern() {

        /* Given */

        Constant sut = new Constant(123.456789123456);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        assertEquals("123.456789123456 = 123.456789123456\n", sb.toString());
    }
}