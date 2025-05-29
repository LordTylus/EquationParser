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

import java.util.Deque;
import java.util.LinkedList;

/**
 * This is a helper class that can pass information between
 * {@link EquationTokenizer} instances to maintain thread safety.
 */
public class TokenizerContext {

    private int bracketCount;

    private Deque<ParenthesisToken> openingTokens = new LinkedList<>();

    public void addOpeningToken(ParenthesisToken parenthesisToken) {
        this.openingTokens.push(parenthesisToken);
    }

    public ParenthesisToken retrieveOpeningToken() {

        if (this.openingTokens.peek() == null)
            return null;

        return this.openingTokens.pop();
    }

    public void increaseBracketCount() {
        bracketCount++;
    }

    public void decreaseBracketCount() {
        bracketCount--;
    }

    public boolean isSplitProhibited() {
        return bracketCount != 0;
    }
}
