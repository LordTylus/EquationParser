package io.lordtylus.equation;

import io.lordtylus.Result;
import io.lordtylus.equation.functions.MathFunction;
import io.lordtylus.equation.functions.StandardFunctions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParenthesisTest {

    @Test
    void canOutputPatternEnglish() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        String actual = sut.toPattern(Locale.ENGLISH);

        /* Then */

        String expected = "sqrt(72.25)";

        assertEquals(expected, actual);
    }

    @Test
    void canOutputPatternGerman() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        String actual = sut.toPattern(Locale.GERMAN);

        /* Then */

        String expected = "sqrt(72,25)";

        assertEquals(expected, actual);
    }

    @Test
    void canEvaluateDouble() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asDouble();

        assertEquals(8.5, value, 0.00001);
    }

    @Test
    void canEvaluateFloat() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        double value = actual.asFloat();

        assertEquals(8.5F, value, 0.00001);
    }

    @Test
    void canEvaluateInt() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        int value = actual.asInt();

        assertEquals(8, value);
    }

    @Test
    void canEvaluateLong() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        /* When */

        Result actual = sut.evaluate();

        /* Then */

        long value = actual.asLong();

        assertEquals(8, value);
    }

    @Test
    void printsPattern() {

        /* Given */

        Constant inner = new Constant(72.25);
        MathFunction function = StandardFunctions.SQRT;

        Parenthesis sut = new Parenthesis(function, inner);

        Result result = sut.evaluate();
        StringBuilder sb = new StringBuilder();

        /* When */

        result.print(sb);

        /* Then */

        String expected = """
                sqrt ( 72.25 ) = 8.5
                  72.25 = 72.25
                """;

        assertEquals(expected, sb.toString());
    }
}