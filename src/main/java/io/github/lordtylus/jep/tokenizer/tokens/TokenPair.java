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
package io.github.lordtylus.jep.tokenizer.tokens;

import lombok.Getter;
import lombok.Setter;

/**
 * This class serves as a hint for parsing. Certain Tokens such as Parenthesis come in a pair and therefore can hold each other's reference.
 * <p>
 * That way during parsing its easier to just ignore everything in between the parenthesis for example, by directly jumping from the opening to the closing parenthesis.
 */
@Getter
public abstract class TokenPair implements Token {

    /**
     * This field holds the reference of the opening token of a token pair.
     * Is will only be filled once tokenizing is the equation string is finished
     * and might still be null during tokenizing. It will always be null if
     * {@link #isOpening()} returns true.
     */
    private TokenPair opening;
    /**
     * This field holds the reference of the closing token of a token pair.
     * Is will only be filled once tokenizing is the equation string is finished
     * and might still be null during tokenizing. It will always be null if
     * {@link #isClosing()} returns true.
     */
    private TokenPair closing;

    /**
     * This index is the index in the Token List. It is set during tokenizing
     * and can be used by the parsers to skip over entries when a TokenPair such as Parenthesis
     * is encountered.
     */
    @Setter
    private int index;

    /**
     * @return true if this is the opening token of a token pair.
     */
    public abstract boolean isOpening();

    /**
     * @return true if this is the closing token of a token pair.
     */
    public abstract boolean isClosing();

    /**
     * Sets the instance of the closing parenthesis to this opening one.
     * That way they are forming a parenthesis pair.
     * <p>
     * This method also takes care setting the opening token of the one passed in.
     *
     * @param token The token of the closing parenthesis.
     * @throws UnsupportedOperationException if the parenthesis this method is called is not an opening one.
     */
    public void setClosing(TokenPair token) {

        if (isClosing())
            throw new UnsupportedOperationException("Can only be set on an opening parenthesis!");

        this.closing = token;
        token.setOpening(this);
    }

    private void setOpening(TokenPair token) {
        this.opening = token;
    }

    @Override
    public int adjustDepth(int currentDepth) {

        if (isOpening())
            return currentDepth + 1;

        return currentDepth - 1;
    }
}
