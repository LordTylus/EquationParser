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
package io.github.lordtylus.jep.operators;

import io.github.lordtylus.jep.parsers.OperationParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

/**
 * This parser can map a given char to an {@link Operator} based on the {@link Operator operators} pattern.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperatorParser {

    /**
     * Performs the parsing according to class definition.
     * <p>
     * This class does not handle conflicts of patters in the provided {@link Operator operators}.
     * If there is a conflict, the first {@link Operator} encountered will be returned. What is first depends on the order of the passed Collection.
     * In case of an unordered set, it cannot be predicted which {@link Operator} will be first.
     *
     * @param relevantOperators Collection of {@link Operator operators} used for matching.
     * @param pattern           character pattern to match.
     * @return Optional with parsed {@link Operator} or empty() if there is no match.
     * @see OperationParser
     */
    public static Optional<Operator> parse(
            @NonNull Map<Character, Operator> relevantOperators,
            char pattern) {

        return Optional.ofNullable(relevantOperators.get(pattern));
    }
}
