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
package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.equation.Constant;
import io.github.lordtylus.jep.parsers.ParseResult.ParseType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParseResultTest {

    @Test
    void createsSuccess() {

        /* Given / When */

        ParseResult sut = ParseResult.ok(new Constant(123));

        /* Then */

        assertEquals(ParseType.OK, sut.getParseType());
    }

    @Test
    void containsEquationIfSuccess() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult sut = ParseResult.ok(equation);

        /* When */

        Equation actual = sut.getEquation().orElseThrow();

        /* Then */

        assertThat(actual).usingRecursiveComparison().isEqualTo(equation);
    }

    @Test
    void containsNoErrorMessageIfSuccess() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult sut = ParseResult.ok(equation);

        /* When */

        String actual = sut.getErrorMessage();

        /* Then */

        assertNull(actual);
    }

    @Test
    void canRetrieveEquationWithoutOptional() {

        /* Given */

        Constant equation = new Constant(123);
        ParseResult sut = ParseResult.ok(equation);

        /* When */

        Equation actual = sut.getNullableEquation();

        /* Then */

        assertThat(actual).usingRecursiveComparison().isEqualTo(equation);
    }

    @Test
    void createsError() {

        /* Given / When */

        ParseResult sut = ParseResult.error("Test");

        /* Then */

        assertEquals(ParseType.ERROR, sut.getParseType());
    }

    @Test
    void hasNoEquationIfError() {

        /* Given */

        ParseResult sut = ParseResult.error("Test");

        /* When */

        Optional<Equation> actual = sut.getEquation();

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @Test
    void nullableEquationWillBeNullOnError() {

        /* Given */

        ParseResult sut = ParseResult.error("Test");

        /* When */

        Equation actual = sut.getNullableEquation();

        /* Then */

        assertNull(actual);
    }

    @Test
    void hasMessageIfError() {

        /* Given */

        ParseResult sut = ParseResult.error("Test 123");

        /* When */

        String actual = sut.getErrorMessage();

        /* Then */

        assertEquals("Test 123", actual);
    }

    @Test
    void createsNotMine() {

        /* Given / When */

        ParseResult sut = ParseResult.notMine();

        /* Then */

        assertEquals(ParseType.NOT_MINE, sut.getParseType());
    }

    @Test
    void hasNoEquationIfNotMine() {

        /* Given */

        ParseResult sut = ParseResult.notMine();

        /* When */

        Optional<Equation> actual = sut.getEquation();

        /* Then */

        assertTrue(actual.isEmpty());
    }

    @Test
    void hasNoNullableEquationIfNotMine() {

        /* Given */

        ParseResult sut = ParseResult.notMine();

        /* When */

        Equation actual = sut.getNullableEquation();

        /* Then */

        assertNull(actual);
    }

    @Test
    void hasNoErrorIfNotMine() {

        /* Given */

        ParseResult sut = ParseResult.notMine();

        /* When */

        String actual = sut.getErrorMessage();

        /* Then */

        assertNull(actual);
    }
}