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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * This parser can map a given string to a {@link MathFunction} based on the {@link MathFunction functions} pattern and known aliases.
 * <p>
 * To be able to match the {@link MathFunction} its function pattern and/or aliases must be lowercase. Case-sensitive patterns are not supported by this class.
 * <p>
 * If an uppercase pattern is passed as an argument, it will be converted to lowercase first.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathFunctionParser {

    /**
     * Performs the parsing according to class definition.
     * <p>
     * This class does not handle conflicts of aliases and patters in the provided {@link MathFunction functions}.
     * If there is a conflict, the first {@link MathFunction} encountered will be returned. What is first depends on the order of the passed Collection.
     * In case of an unordered set, it cannot be predicted which {@link MathFunction} will be first.
     *
     * @param relevantFunctions Collection of {@link MathFunction} used for matching.
     * @param pattern           String pattern to match.
     * @return Optional with parsed {@link MathFunction} or empty() if there is no match.
     * @see MathFunctionParser
     */
    public static Optional<MathFunction> parse(
            @NonNull Collection<MathFunction> relevantFunctions,
            @NonNull String pattern) {

        String lowerCasePattern = pattern.toLowerCase(Locale.ENGLISH);

        return relevantFunctions.stream()
                .filter(function ->
                        lowerCasePattern.equals(function.getPattern())
                                || function.getAliases().contains(lowerCasePattern))
                .findFirst();
    }
}
