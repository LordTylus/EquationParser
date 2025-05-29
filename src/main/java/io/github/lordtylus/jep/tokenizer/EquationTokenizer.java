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
import java.util.Set;

/**
 * This interface defines the API to be used by {@link EquationStringTokenizer} to evaluate
 * a character and decide what to do with the equation string.
 */
public interface EquationTokenizer {

    /**
     * Returns a set of delimiters this tokenizer recognizes and works with.
     *
     * @return character set of delimiters
     */
    Set<Character> getDelimiters();

    /**
     * This method is invoked by the {@link EquationStringTokenizer} while tokenizing the equation string.
     * <p>
     * It will already have determined that this {@link EquationTokenizer} is the right one to call and
     * pass it the current delimiter as well as other meta information of the tokenizing process.
     * <p>
     * The delimiter is already extracted for performance reasons, so the implementation doesn't
     * need to read it from the string itself.
     *
     * @param beginIndex       index of the string after the last token was added.
     * @param currentIndex     current index of the string to be tokenized.
     * @param currentCharacter the character the currentIndex points to.
     * @param equation         equation string to be tokenized.
     * @param tokenList        mutable List of Tokens this tokenizer can add a token to.
     * @param context          the {@link TokenizerContext} for tokenizers to store and exchange data.
     * @return true if this tokenizer added a token to the list.
     */
    boolean handle(
            int beginIndex,
            int currentIndex,
            char currentCharacter,
            @NonNull String equation,
            @NonNull List<Token> tokenList,
            @NonNull TokenizerContext context);
}
