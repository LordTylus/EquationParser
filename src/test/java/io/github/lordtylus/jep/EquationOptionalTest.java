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
package io.github.lordtylus.jep;

import io.github.lordtylus.jep.equation.Constant;
import io.github.lordtylus.jep.parsers.ParseException;
import io.github.lordtylus.jep.parsers.ParseResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquationOptionalTest {

    @Test
    void returnsOptionalWithEquation() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult parseResult = ParseResult.ok(equation);

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        Equation actual = sut.asOptional().orElseThrow();

        /* Then */

        assertEquals(equation, actual);
    }

    @Test
    void canGetTheEquationIfItExists() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult parseResult = ParseResult.ok(equation);

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        Equation actual = sut.get();

        /* Then */

        assertEquals(equation, actual);
    }

    @Test
    void isPresentReturnsTrueIfEquationExists() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult parseResult = ParseResult.ok(equation);

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        boolean actual = sut.isPresent();

        /* Then */

        assertTrue(actual);
    }

    @Test
    void hasErrorReturnsFalseIfEquationExists() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult parseResult = ParseResult.ok(equation);

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        boolean actual = sut.hasError();

        /* Then */

        assertFalse(actual);
    }

    @Test
    void returnsEmptyOptionalIfNoEquationIsSet() {

        /* Given */

        ParseResult parseResult = ParseResult.error("Test");

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        Optional<Equation> equation = sut.asOptional();

        /* Then */

        assertTrue(equation.isEmpty());
    }

    @Test
    void exceptionIfTryingToGetNonExistentEquation() {

        /* Given */

        ParseResult parseResult = ParseResult.error("Test");

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        Executable result = sut::get;

        /* Then */

        assertThrows(NoSuchElementException.class, result);
    }

    @Test
    void isPresentReturnsFalseIfEquationDoesntExist() {

        /* Given */

        ParseResult parseResult = ParseResult.error("Test");

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        boolean actual = sut.isPresent();

        /* Then */

        assertFalse(actual);
    }

    @Test
    void hasErrorReturnsFalseIfEquationDoesntExist() {

        /* Given */

        ParseResult parseResult = ParseResult.error("Test");

        EquationOptional sut = EquationOptional.of(parseResult);

        /* When */

        boolean actual = sut.hasError();

        /* Then */

        assertTrue(actual);
    }

    @Test
    void returnsEmptyOptionalIfExceptionOccurred() {

        /* Given */

        ParseException parseException = new ParseException("Test");
        EquationOptional sut = EquationOptional.of(parseException);

        /* When */

        Optional<Equation> equation = sut.asOptional();

        /* Then */

        assertTrue(equation.isEmpty());
    }

    @Test
    void exceptionIfTryingToGetEquationAfterException() {

        /* Given */

        ParseException parseException = new ParseException("Test");
        EquationOptional sut = EquationOptional.of(parseException);

        /* When */

        Executable result = sut::get;

        /* Then */

        assertThrows(NoSuchElementException.class, result);
    }

    @Test
    void isPresentReturnsFalseIfExceptionOccurred() {

        /* Given */

        ParseException parseException = new ParseException("Test");
        EquationOptional sut = EquationOptional.of(parseException);

        /* When */

        boolean actual = sut.isPresent();

        /* Then */

        assertFalse(actual);
    }

    @Test
    void hasErrorReturnsFalseIfExceptionOccurred() {

        /* Given */

        ParseException parseException = new ParseException("Test");
        EquationOptional sut = EquationOptional.of(parseException);

        /* When */

        boolean actual = sut.hasError();

        /* Then */

        assertTrue(actual);
    }
}