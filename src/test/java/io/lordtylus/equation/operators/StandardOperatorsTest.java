package io.lordtylus.equation.operators;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardOperatorsTest {

    @Test
    void evaluateAddition() {

        /* Given */

        Operator sut = StandardOperators.ADD;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(30.5, actual, 0.0001);
    }

    @Test
    void evaluateSubtraction() {

        /* Given */

        Operator sut = StandardOperators.SUB;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(-9.5, actual, 0.0001);
    }

    @Test
    void evaluateMultiplication() {

        /* Given */

        Operator sut = StandardOperators.MULT;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(210, actual, 0.0001);
    }

    @Test
    void evaluateDivision() {

        /* Given */

        Operator sut = StandardOperators.DIV;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(0.525, actual, 0.0001);
    }

    @Test
    void evaluatePower() {

        /* Given */

        Operator sut = StandardOperators.POW;

        /* When */

        double actual = sut.evaluate(10.5, 20).doubleValue();

        /* Then */

        assertEquals(265329770514442013394.54307, actual, 0.0001);
    }

    @Test
    void allContainsCorrectOperators() {

        /* Given / When */

        List<Operator> actual = StandardOperators.all();

        /* Then */

        List<Operator> expected = List.of(
                StandardOperators.ADD,
                StandardOperators.SUB,
                StandardOperators.MULT,
                StandardOperators.DIV,
                StandardOperators.POW
        );

        assertEquals(expected, actual);
    }
}