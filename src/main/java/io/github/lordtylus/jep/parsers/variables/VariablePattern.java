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
package io.github.lordtylus.jep.parsers.variables;

import io.github.lordtylus.jep.parsers.VariableParser;
import io.github.lordtylus.jep.tokenizer.VariableTokenizer;

/**
 * This class helps the {@link VariableParser} and {@link VariableTokenizer}
 * by defining how variables are marked to match the respective use case.
 * <p>
 * Only single characters are supported. While the {@link VariableParser}
 * could theoretically work with a character sequence, the {@link VariableTokenizer} cannot.
 * <p>
 * Hence only single characters are allowed.
 *
 * @param isEscaped        This method signals both the {@link VariableTokenizer} and the {@link VariableParser} if there are any characters wrapping the variable name or not.
 *                         <p>
 *                         If this variable returns false the results of {@link #openingCharacter} and {@link #closingCharacter} are set to some default and are therefore to be ignored.
 * @param openingCharacter The opening character of a Variable.
 *                         <p>
 *                         The default implementation uses '[' but the implementation can use any other character.
 *                         However, the implementation has to be careful to not use any characters used by different parsers.
 *                         For example using any of the operators would lead to confusion and parsing errors.
 * @param closingCharacter The closing character of a Variable.
 *                         <p>
 *                         The default implementation uses ']' but the implementation can use any other character.
 *                         However, the implementation has to be careful to not use any characters used by different parsers.
 *                         For example using any of the operators would lead to confusion and parsing errors.
 */
public record VariablePattern(
        boolean isEscaped,
        char openingCharacter,
        char closingCharacter) {

    /**
     * This constructor creates a new VariablePattern with the given settings.
     *
     * @throws IllegalArgumentException if {@link #openingCharacter} and {@link #closingCharacter} are identical.
     */
    public VariablePattern {
        if (isEscaped && openingCharacter == closingCharacter)
            throw new IllegalArgumentException("Characters must not be identical!");
    }
}
