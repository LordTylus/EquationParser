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
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * This tokenizer implementation reacts to parentheses ( and ) and add these
 * to the passed in token list.
 * <p>
 * Everything that wasn't tokenized before an opening parenthesis will be added as a function token.
 */
@RequiredArgsConstructor
public final class ParenthesisTokenizer implements EquationTokenizer {

    /**
     * Default instance of this class which tokenizes values before an opening parenthesis as a function.
     */
    public static final ParenthesisTokenizer DEFAULT = new ParenthesisTokenizer(true);

    @Getter
    private final Set<Character> delimiters = Set.of('(', ')');

    private final boolean tokenizeFunctions;

    @Override
    public boolean handle(
            int beginIndex,
            int currentIndex,
            char currentCharacter,
            @NonNull String equation,
            @NonNull List<Token> tokenList,
            @NonNull TokenizerContext context) {

        if (context.isSplitProhibited())
            return false;

        String substring = equation.substring(beginIndex, currentIndex);

        ParenthesisToken token = new ParenthesisToken(currentCharacter);

        if (token.isOpening()) {
            context.addOpeningToken(token);
        } else {

            ParenthesisToken openingToken = context.retrieveOpeningToken();

            if (openingToken != null)
                openingToken.setClosing(token);
        }

        if (tokenizeFunctions && token.isOpening())
            token.setFunction(substring);
        else if (!substring.isBlank())
            tokenList.add(new ValueToken(substring));

        tokenList.add(token);

        return true;
    }
}
