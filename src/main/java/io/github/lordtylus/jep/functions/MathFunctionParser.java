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
package io.github.lordtylus.jep.functions;

import lombok.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * This parser can map a given string to a {@link MathFunction} based on the {@link MathFunction functions} pattern and known aliases.
 * <p>
 * To be able to match the {@link MathFunction} its function pattern and/or aliases must be lowercase. Case-sensitive patterns are not supported by this class.
 * <p>
 * If an uppercase pattern is passed as an argument, it will be converted to lowercase first.
 */
public class MathFunctionParser {

    private final Map<String, MathFunction> relevantFunctions = new HashMap<>();

    /**
     * Constructs a new {@link MathFunctionParser} with the given collection of functions to check for.
     * <p>
     * The order may or may not be important depending on the use case of the caller. If an order of
     * functions is needed, a list should be passed. That way conflicts of operator names, which this
     * class does not handle, will be predictable.
     * <p>
     * If an unordered collection is passed, name conflicts may lead to non-deterministic results.
     *
     * @param relevantFunctions Collection of relevant functions to parse.
     */
    public MathFunctionParser(Collection<MathFunction> relevantFunctions) {

        for (MathFunction relevantFunction : relevantFunctions) {

            this.relevantFunctions.put(relevantFunction.getPattern(), relevantFunction);

            for (String alias : relevantFunction.getAliases())
                this.relevantFunctions.put(alias, relevantFunction);
        }
    }

    /**
     * Performs the parsing according to class definition.
     * <p>
     * This class does not handle conflicts of aliases and patters in the provided {@link MathFunction functions}.
     * If there is a conflict, the last {@link MathFunction} encountered will be returned. What is first depends on the order of the passed Collection.
     * In case of an unordered set, it cannot be predicted which {@link MathFunction} will be first.
     *
     * @param pattern String pattern to match.
     * @return Optional with parsed {@link MathFunction} or empty() if there is no match.
     * @see MathFunctionParser
     */
    public Optional<MathFunction> parse(
            @NonNull String pattern) {

        String lowerCasePattern = pattern.toLowerCase(Locale.ENGLISH);

        return Optional.ofNullable(relevantFunctions.get(lowerCasePattern));
    }
}
