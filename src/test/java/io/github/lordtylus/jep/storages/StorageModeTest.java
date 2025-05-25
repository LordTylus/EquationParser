package io.github.lordtylus.jep.storages;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageModeTest {

    @Test
    void throwsExceptionOnStrict() {

        /* Given */

        StorageMode sut = StorageMode.STRICT;

        /* When */

        Executable result = () -> sut.handleNull("test", 0.0);

        /* Then */

        assertThrows(IllegalArgumentException.class, result);
    }

    @Test
    void returnsDefaultWhenNotStrict() {

        /* Given */

        StorageMode sut = StorageMode.UNKNOWN_MEANS_DEFAULT;

        /* When */

        Number actual = sut.handleNull("test", 0.0);

        /* Then */

        assertEquals(0.0, actual.doubleValue(), 0.00001);
    }
}