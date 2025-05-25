package io.lordtylus.equation.storages;

import io.lordtylus.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmptyStorageTest {

    @Test
    void evaluatesEverythingAsZero() {

        /* Given */

        Storage sut = EmptyStorage.INSTANCE;

        /* When */

        Number actual = sut.evaluate("test");

        /* Then */

        assertEquals(0, actual);
    }
}