package io.github.lordtylus.jep.parsers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class ParseExceptionTest {

    @Test
    void providesCause() {

        /* Given */

        NumberFormatException test = new NumberFormatException("test");
        ParseException sut = new ParseException(test);

        /* When */

        Throwable actual = sut.getCause();

        /* Then */

        assertSame(test, actual);
    }
}