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
package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.tokenizer.tokens.Token;
import lombok.NonNull;

import java.util.List;

/**
 * This interface defines the API to be used by {@link EquationStringTokenizer} to evaluate
 * a character and decide what to do with the equation string.
 */
public interface EquationTokenizer {

    /**
     * @param beginIndex   index of the string after the last token was added.
     * @param currentIndex current index of the string to be tokenized.
     * @param equation     equation string to be tokenized.
     * @param tokenList    mutable List of Tokens this tokenizer can add a token to.
     * @return true if this tokenizer added a token to the list.
     */
    boolean handle(
            int beginIndex,
            int currentIndex,
            @NonNull String equation,
            @NonNull List<Token> tokenList);
}
