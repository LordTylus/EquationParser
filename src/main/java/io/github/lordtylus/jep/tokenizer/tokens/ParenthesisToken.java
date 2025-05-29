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

/**
 * This token visualizes parenthesis in an equation and can either be opening or closing.
 * <p>
 * Additionally, an opening parenthesis can have a function string, which
 * identifies a math function to be applied when the {@link Equation} is evaluated.
 */
public class ParenthesisToken implements Token {

    private final char character;
    private String function;

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
}