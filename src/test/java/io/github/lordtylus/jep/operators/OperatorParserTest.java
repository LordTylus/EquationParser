package io.github.lordtylus.jep.operators;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperatorParserTest {

    @Test
    void parsesFunction() {

        /* Given */

        Operator operator1 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator2 = new Operator(2, '-', (a, b) -> a.doubleValue() - b.doubleValue());
        Operator operator3 = new Operator(3, '*', (a, b) -> a.doubleValue() * b.doubleValue());

        List<Operator> operators = List.of(operator1, operator2, operator3);

        /* When */

        Operator actual = OperatorParser.parse(operators, '-').orElseThrow();

        /* Then */

        assertSame(operator2, actual);
    }

    @Test
    void doesNotParse() {

        /* Given */

        Operator operator1 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator2 = new Operator(2, '-', (a, b) -> a.doubleValue() - b.doubleValue());
        Operator operator3 = new Operator(3, '*', (a, b) -> a.doubleValue() * b.doubleValue());

        List<Operator> operators = List.of(operator1, operator2, operator3);

        /* When */

        Optional<Operator> actual = OperatorParser.parse(operators, '/');

        /* Then */

        assertTrue(actual.isEmpty());
    }
}