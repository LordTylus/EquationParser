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

import lombok.NonNull;

/**
 * This Mode determines the {@link SimpleStorage#evaluate(String)} method should behave if no value for a given variable name could be found.
 */
public enum StorageMode {

    /**
     * Strict Mode means that unknown variables are strictly forbidden. Is an unknown variable name encountered, a IllegalArgumentException will be thrown.
     */
    STRICT,
    /**
     * Unknown means default treats unknown arguments as Storages defaultValue and will not throw any exception.
     */
    UNKNOWN_MEANS_DEFAULT;

    /**
     * This method executes the logic on what to do if the {@link SimpleStorage} was not able to find the value to a given variable name.
     *
     * @param variableName name of the variable whose value could not be determined.
     * @param defaultValue default value to use in case of {@link #UNKNOWN_MEANS_DEFAULT}
     * @return defaultValue if {@link #UNKNOWN_MEANS_DEFAULT} is set.
     * @throws IllegalArgumentException if {@link #STRICT} mode is set.
     */
    public Number handleNull(
            @NonNull String variableName,
            @NonNull Number defaultValue) {

        return switch (this) {
            case STRICT -> throw new IllegalArgumentException("Variable '" + variableName + "' not set!");
            case UNKNOWN_MEANS_DEFAULT -> defaultValue;
            default -> throw new UnsupportedOperationException("Mode " + this + " is not handled!");
        };
    }
}
