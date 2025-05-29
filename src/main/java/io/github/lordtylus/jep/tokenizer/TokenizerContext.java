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

import io.github.lordtylus.jep.tokenizer.tokens.ParenthesisToken;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This is a helper class that can pass information between
 * {@link EquationTokenizer} instances to maintain thread safety.
 */
public class TokenizerContext {

    private int bracketCount;

    private Deque<ParenthesisToken> openingTokens = new ArrayDeque<>(25);

    /**
     * This method saves an opening parenthesis Token to the context.
     * This token can later be retrieved from an internal deque when
     * a closing token is found.
     *
     * @param parenthesisToken The token to push to the stack.
     */
    public void addOpeningToken(ParenthesisToken parenthesisToken) {
        this.openingTokens.push(parenthesisToken);
    }

    /**
     * When a closing {@link ParenthesisToken} was discovered this method allows to retrieve
     * the correct opening token, to link them up to make parsing later easier.
     *
     * @return Last {@link ParenthesisToken} that was added to the context.
     */
    public ParenthesisToken retrieveOpeningToken() {

        if (this.openingTokens.peek() == null)
            return null;

        return this.openingTokens.pop();
    }

    /**
     * This method increases the count of '[' tokens found in the string to tokenize.
     * That way tokenizers can be informed that they are not supposed to split before
     * the ']' is reached.
     * <p>
     * If is possible to have nested [] which therefore will increase the count further.
     */
    public void increaseBracketCount() {
        bracketCount++;
    }

    /**
     * This method decreases the count of [] tokens found in the string to tokenize.
     * If the count reaches back to zero other tokenizers can start splitting again.
     */
    public void decreaseBracketCount() {
        bracketCount--;
    }

    /**
     * This method is there to tell tokenizers if they are to be idle or if there
     * is anything for them to do. Most commonly they check for [] to ignore
     * operators inside the brackets.
     *
     * @return true if at least one opening bracket was encountered.
     */
    public boolean isSplitProhibited() {
        return bracketCount != 0;
    }
}
