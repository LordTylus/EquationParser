package io.github.lordtylus.jep.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathFunctionTest {

    @Test
    void evaluates() {

        /* Given */

        MathFunction sut = new MathFunction("sqrt", number -> Math.sqrt(number.doubleValue()));

        /* When */

        double actual = sut.evaluate(9).doubleValue();

        /* Then */

        assertEquals(3, actual, 0.0001);
    }
}