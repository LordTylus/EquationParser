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