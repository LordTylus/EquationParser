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