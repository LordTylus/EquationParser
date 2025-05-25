package io.lordtylus.equation.operators;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorTest {

    @Test
    void evaluates() {

        /* Given */

        Operator sut = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());

        /* When */

        double actual = sut.evaluate(20, 25.5).doubleValue();

        /* Then */

        assertEquals(45.5, actual, 0.0001);
    }

    @Test
    void findAllOrdersSorted() {

        /* Given */

        Operator operator1 = new Operator(5, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator2 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator3 = new Operator(3, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator4 = new Operator(3, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator5 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator6 = new Operator(6, '+', (a, b) -> a.doubleValue() + b.doubleValue());

        List<Operator> operators = List.of(operator1, operator2, operator3, operator4, operator5, operator6);

        /* When */

        List<Integer> actual = Operator.getRelevantOrders(operators);

        /* Then */

        List<Integer> expected = List.of(1, 3, 5, 6);

        assertEquals(actual, expected);
    }

    @Test
    void findsOperatorsForOrder() {

        /* Given */

        Operator operator1 = new Operator(5, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator2 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator3 = new Operator(3, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator4 = new Operator(3, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator5 = new Operator(1, '+', (a, b) -> a.doubleValue() + b.doubleValue());
        Operator operator6 = new Operator(6, '+', (a, b) -> a.doubleValue() + b.doubleValue());

        List<Operator> operators = List.of(operator1, operator2, operator3, operator4, operator5, operator6);

        /* When */

        List<Operator> actual = Operator.getRelevantOperatorsForOrder(1, operators);

        /* Then */

        List<Operator> expected = List.of(operator2, operator5);

        assertEquals(actual, expected);
    }
}