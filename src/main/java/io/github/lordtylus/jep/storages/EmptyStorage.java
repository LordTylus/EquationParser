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

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Storage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This special Storage implementation can be for evaluation {@link Equation equations} where no Variables are needed.
 * <p>
 * The implementation ensures that for every provided variable the int value 0 is returned. This implementation does not throw any exceptions for any non-null variable name.
 *
 * @see Equation#evaluate(Storage)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyStorage implements Storage {

    /**
     * immutable singleton instance of this class.
     */
    public static final Storage INSTANCE = new EmptyStorage();

    @Override
    public Number evaluate(@NonNull String variable) {
        return 0;
    }
}
