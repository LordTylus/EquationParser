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
package io.github.lordtylus.jep;

import io.github.lordtylus.jep.equation.Variable;
import lombok.NonNull;

/**
 * The Storage is used when evaluating parsed {@link Equation equasions} to determine what value a given variable currently represents.
 * The storages implementation can use any logic to determine the value of the given variable.
 * <p>
 * The implementation may use other {@link Equation equations} to solve variables.
 * However, the programmer has to ensure that there aren't any circle dependencies between Storage and Equation which would lead to a stack overflow.
 */
public interface Storage {

    /**
     * Evaluates the given variable name and returns its associated Number value used for evaluating {@link Equation equations}.
     * <p>
     * The most prominent example for that would be the {@link Variable} class which is specifically made to use a variable value such as x in the equation.
     *
     * @param variable The variable a value is needed for.
     * @return The Number value associated with the given variable.
     * @throws NullPointerException     If any given argument is null.
     * @throws IllegalArgumentException The implementation may decide to throw this exception if the provided variable is unknown or doesn't conform to standards.
     * @see Variable#evaluate(Storage)
     */
    Number evaluate(@NonNull String variable);
}
