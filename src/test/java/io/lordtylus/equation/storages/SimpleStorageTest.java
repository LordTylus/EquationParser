package io.lordtylus.equation.storages;

import io.lordtylus.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleStorageTest {

    @Test
    void defaultStorageIsStrict() {

        /* Given */

        Storage sut = new SimpleStorage();

        /* When */

        Executable result = () -> sut.evaluate("test");

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void defaultValueIsZeroWhenNotStrict() {

        /* Given */

        Storage sut = new SimpleStorage(StorageMode.UNKNOWN_MEANS_DEFAULT);

        /* When */

        Number actual = sut.evaluate("test");

        /* Then */

        assertEquals(0, actual);
    }

    @Test
    void customDefaultValueIsUsed() {

        /* Given */

        Storage sut = new SimpleStorage(StorageMode.UNKNOWN_MEANS_DEFAULT, 2.0);

        /* When */

        Number actual = sut.evaluate("test");

        /* Then */

        assertEquals(2.0, actual.doubleValue(), 0.00001);
    }

    @Test
    void valueCanBeAdded() {

        /* Given */

        SimpleStorage sut = new SimpleStorage();

        sut.putValue("test", 123);

        /* When */

        Number actual = sut.evaluate("test");

        /* Then */

        assertEquals(123, actual);
    }

    @Test
    void valueCanBeRemoved() {

        /* Given */

        SimpleStorage sut = new SimpleStorage();

        sut.putValue("test", 123);

        /* When */

        sut.removeValue("test");

        /* Then */

        Executable result = () -> sut.evaluate("test");

        assertThrows(IllegalArgumentException.class, result);
    }

}