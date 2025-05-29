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

import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.tokenizer.tokens.OperatorToken;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import lombok.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This tokenizer implementation reacts to parentheses ( and ) and add these
 * to the passed in token list.
 * <p>
 * Everything that wasn't tokenized before an opening parenthesis will be added as a function token.
 */
public class OperatorTokenizer implements EquationTokenizer {

    /**
     * Default instance of this class which can tokenize all default operators.
     */
    public static final OperatorTokenizer DEFAULT = new OperatorTokenizer(OperationParser.DEFAULT.getOperatorCharacters());

    private final Set<Character> operatorCharacterToCheck;

    /**
     * Creates a new OperatorTokenizer with the given list of Tokenizers to check.
     *
     * @param operatorCharacterToCheck List of operators to recognize when tokenizing the equation string.
     */
    public OperatorTokenizer(
            @NonNull Set<Character> operatorCharacterToCheck) {

        this.operatorCharacterToCheck = new HashSet<>(operatorCharacterToCheck);
    }

    @Override
    public boolean handle(
            int beginIndex,
            int currentIndex,
            @NonNull String equation,
            @NonNull List<Token> tokenList,
            @NonNull TokenizerContext context) {

        if (context.isSplitProhibited())
            return false;

        char currentCharacter = equation.charAt(currentIndex);

        if (!operatorCharacterToCheck.contains(currentCharacter))
            return false;

        /*
         * Negative numbers have an operator in front.
         * This should only be possible when it's the start of the string,
         * or after an opening.
         */
        if (currentIndex == 0 || equation.charAt(currentIndex - 1) == '(')
            return false;

        String substring = equation.substring(beginIndex, currentIndex);

        if (!substring.isBlank())
            tokenList.add(new ValueToken(substring));

        tokenList.add(new OperatorToken(currentCharacter));

        return true;
    }
}
