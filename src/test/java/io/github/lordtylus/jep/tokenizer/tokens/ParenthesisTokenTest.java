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
package io.github.lordtylus.jep.tokenizer.tokens;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParenthesisTokenTest {

    @Test
    void exceptionIfSomethingOtherThanParenthesisArePassedIn() {

        /* Given / When */

        Executable result = () -> new ParenthesisToken('a');

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void returnsOpeningParenthesisString() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken('(');

        /* When */

        String actual = sut.getString();

        /* Then */

        assertEquals("(", actual);
    }

    @Test
    void openingParenthesisIsOpeningNotClosing() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken('(');

        /* When */

        boolean actual1 = sut.isOpening();
        boolean actual2 = sut.isClosing();

        /* Then */

        assertTrue(actual1);
        assertFalse(actual2);
    }

    @Test
    void returnsOpeningParenthesisStringWithFunction() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken('(');
        sut.setFunction("abc");

        /* When */

        String actual = sut.getString();

        /* Then */

        assertEquals("abc(", actual);
    }

    @Test
    void returnsClosingParenthesisString() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken(')');

        /* When */

        String actual = sut.getString();

        /* Then */

        assertEquals(")", actual);
    }

    @Test
    void functionCannotBeSetToClosingParenthesis() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken(')');

        /* When */

        Executable result = () -> sut.setFunction("abc");

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void closingParenthesisIsClosingNotOpening() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken(')');

        /* When */

        boolean actual1 = sut.isOpening();
        boolean actual2 = sut.isClosing();

        /* Then */

        assertFalse(actual1);
        assertTrue(actual2);
    }

    @Test
    void closingAdjustsDepthPlus1() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken('(');

        /* When */

        int actual = sut.adjustDepth(10);

        /* Then */

        assertEquals(11, actual);
    }

    @Test
    void closingAdjustsDepthMinus1() {

        /* Given */

        ParenthesisToken sut = new ParenthesisToken(')');

        /* When */

        int actual = sut.adjustDepth(10);

        /* Then */

        assertEquals(9, actual);
    }

    @Test
    void anOpeningParenthesisCanBeSetAClosingOne() {

        /* Given */

        ParenthesisToken sut1 = new ParenthesisToken('(');
        ParenthesisToken sut2 = new ParenthesisToken(')');

        /* When */

        sut1.setClosing(sut2);

        /* Then */

        assertSame(sut2, sut1.getClosing());
    }

    @Test
    void anClosingParenthesisCannotBeSetAnything() {

        /* Given */

        ParenthesisToken sut1 = new ParenthesisToken('(');
        ParenthesisToken sut2 = new ParenthesisToken(')');

        /* When */

        Executable result = () -> sut2.setClosing(sut1);

        /* Then */

        assertThrows(UnsupportedOperationException.class, result);
    }

    @Test
    void settingAClosingParenthesisLinksThemBidirectionally() {

        /* Given */

        ParenthesisToken sut1 = new ParenthesisToken('(');
        ParenthesisToken sut2 = new ParenthesisToken(')');

        /* When */

        sut1.setClosing(sut2);

        /* Then */

        ParenthesisToken opening = sut2.getOpening();

        assertSame(sut1, opening);
    }
}