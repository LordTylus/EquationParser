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

import io.github.lordtylus.jep.Storage;
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