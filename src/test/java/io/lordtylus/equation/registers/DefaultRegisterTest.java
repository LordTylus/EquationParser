package io.lordtylus.equation.registers;

import io.lordtylus.equation.parsers.ConstantParser;
import io.lordtylus.equation.parsers.EquationParser;
import io.lordtylus.equation.parsers.OperationParser;
import io.lordtylus.equation.parsers.ParenthesisParser;
import io.lordtylus.equation.parsers.VariableParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultRegisterTest {

    @Test
    void containsCorrectParsers() {

        /* Given */

        DefaultRegister sut = DefaultRegister.INSTANCE;

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
}