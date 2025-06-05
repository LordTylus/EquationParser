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

    @Test
    void providesMessage() {

        /* Given */

        ParseException sut = new ParseException("Something went wrong");

        /* When */

        String actual = sut.getMessage();

        /* Then */

        assertSame("Something went wrong", actual);
    }
}