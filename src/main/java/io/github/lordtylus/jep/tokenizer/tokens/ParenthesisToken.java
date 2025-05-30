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

import io.github.lordtylus.jep.Equation;
import lombok.Getter;
import lombok.Setter;

/**
 * This token visualizes parenthesis in an equation and can either be opening or closing.
 * <p>
 * Additionally, an opening parenthesis can have a function string, which
 * identifies a math function to be applied when the {@link Equation} is evaluated.
 */
@Getter
public class ParenthesisToken implements Token {

    private final char character;
    private String function;

    private ParenthesisToken opening;
    private ParenthesisToken closing;

    /**
     * This index is the index in the Token List. It is set during tokenizing
     * and can be used by the parsers to skip over entries when a Parenthesis
     * is encountered.
     */
    @Setter
    private int index;

    /**
     * Creates a new {@link ParenthesisToken} with the given character.
     *
     * @param character the opening or closing parenthesis ( or )
     * @throws IllegalArgumentException if the character is neither ( nor )
     */
    public ParenthesisToken(char character) {

        if (character != '(' && character != ')')
            throw new IllegalArgumentException("Character must be either ( or ) but was " + character);

        this.character = character;
        this.function = "";
    }

    /**
     * Sets the function to an opening parenthesis.
     *
     * @param function name of the potential function.
     * @throws IllegalArgumentException if the parenthesis this method is called on is a closing one.
     */
    public void setFunction(String function) {

        if (isClosing())
            throw new IllegalArgumentException("Closing parenthesis cannot have any functions!");

        this.function = function;
    }

    /**
     * @return true if the parenthesis is opening -> '(' false otherwise.
     */
    public boolean isOpening() {
        return character == '(';
    }

    /**
     * @return true if the parenthesis is closing -> ')' false otherwise.
     */
    public boolean isClosing() {
        return character == ')';
    }

    @Override
    public String getString() {

        if (isOpening())
            return function + character;

        return String.valueOf(character);
    }

    @Override
    public int adjustDepth(int currentDepth) {

        if (isOpening())
            return currentDepth + 1;

        return currentDepth - 1;
    }

    /**
     * Sets the instance of the closing parenthesis to this opening one.
     * That way they are forming a parenthesis pair.
     * <p>
     * This method also takes care setting the opening token of the one passed in.
     *
     * @param token The token of the closing parenthesis.
     * @throws UnsupportedOperationException if the parenthesis this method is called is not an opening one.
     */
    public void setClosing(ParenthesisToken token) {

        if (isClosing())
            throw new UnsupportedOperationException("Can only be set on an opening parenthesis!");

        this.closing = token;
        token.setOpening(this);
    }

    private void setOpening(ParenthesisToken token) {
        this.opening = token;
    }
}