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
package io.github.lordtylus.jep.equation;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Result;
import io.github.lordtylus.jep.Storage;
import io.github.lordtylus.jep.parsers.VariableParser;
import lombok.NonNull;

import java.util.Locale;

/**
 * This part of an {@link Equation} represents a variable which pulls
 * its current value from the passed {@link Storage} during evaluation.
 * <p>
 * To not interfere with over parts of the equation variable names are
 * passed in [] and extracted during parsing using {@link VariableParser}
 */
public record Variable(
        @NonNull String name
) implements Equation {

    @Override
    public VariableResult evaluate(@NonNull Storage storage) {

        Number result = storage.evaluate(name);

        return new VariableResult(name, result);
    }

    @Override
    public String toPattern(@NonNull Locale locale) {
        return "[" + name + "]";
    }

    /**
     * This record represents the result of the evaluation of the {@link Variable} class.
     */
    public record VariableResult(
            @NonNull String name,
            Number result) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(name)
                    .append(" = ")
                    .append(result)
                    .append("\n");
        }

        @Override
        public void toStaticEquation(@NonNull StringBuilder sb) {
            sb.append(result);
        }

        @Override
        public String toString() {
            return toDisplayString();
        }
    }
}
