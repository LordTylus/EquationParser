package io.github.lordtylus.jep.registers;

import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomRegisterTest {

    @Test
    void createsEmpty() {

        /* Given */

        CustomRegister sut = CustomRegister.empty();

        /* When */

        List<EquationParser> actual = sut.getRegisteredParsers();

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @Test
    void createsNewWithDefault() {

        /* Given */

        CustomRegister sut = CustomRegister.withDefaults();

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
    void registersOneParser() {

        /* Given */

        CustomRegister sut = CustomRegister.empty();

        /* When */

        sut.register(ConstantParser.INSTANCE);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                ConstantParser.INSTANCE
        );

        assertEquals(actual, expected);
    }

    @Test
    void registersASecondParser() {

        /* Given */

        CustomRegister sut = CustomRegister.empty();
        sut.register(ConstantParser.INSTANCE);

        /* When */

        sut.register(ParenthesisParser.DEFAULT);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                ConstantParser.INSTANCE,
                ParenthesisParser.DEFAULT
        );

        assertEquals(actual, expected);
    }

    @Test
    void canUnregisterAParser() {

        /* Given */

        CustomRegister sut = CustomRegister.withDefaults();

        /* When */

        sut.unregister(ParenthesisParser.DEFAULT);

        /* Then */

        List<EquationParser> actual = sut.getRegisteredParsers();

        List<EquationParser> expected = List.of(
                OperationParser.DEFAULT,
                ConstantParser.INSTANCE,
                VariableParser.INSTANCE
        );

        assertEquals(actual, expected);
    }
}