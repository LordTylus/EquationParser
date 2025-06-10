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
import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import lombok.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This part of an {@link Equation} represents a constant double value.
 * <p>
 * The constant value is parsed using {@link ConstantParser}
 */
public record Constant(
        double value
) implements Equation {

    private static final String DECIMAL_FORMAT = "0.#####";

    @Override
    public ConstantResult evaluate(@NonNull Storage storage) {
        return new ConstantResult(value);
    }

    @Override
    public String toPattern(
            @NonNull Locale locale,
            @NonNull VariablePattern variablePattern) {

        NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT,
                DecimalFormatSymbols.getInstance(locale));

        return formatter.format(value);
    }

    /**
     * This record represents the result of the evaluation of the {@link Constant} class.
     */
    public record ConstantResult(Number result) implements Result {

        @Override
        public void print(
                @NonNull StringBuilder sb,
                @NonNull String currentIndent,
                @NonNull String indent) {

            sb.append(currentIndent)
                    .append(result)
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
