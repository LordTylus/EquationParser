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

import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import io.github.lordtylus.jep.tokenizer.tokens.ValueToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to take a given Equation String and tokenize it for parsing.
 * <p>
 * In the default configuration a String 12*([x]+3)^2 would be turned into:
 * <pre>
 * "12", "*", "(", "[x]", "3", ")", "^", "2"
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EquationStringTokenizer {

    /**
     * Tokenizes the given equation string according to the class definition.
     *
     * @param equation equation string to be tokenized.
     * @param options  parsing options to provide {@link EquationTokenizer} instances to use.
     * @return List with parsed tokens in order.
     */
    public static List<Token> tokenize(
            @NonNull String equation,
            @NonNull ParsingOptions options) {

        List<Token> tokenList = new ArrayList<>();
        int beginIndex = 0;

        for (int i = 0; i < equation.length(); i++)
            for (EquationTokenizer tokenizer : options.getRegisteredTokenizers())
                if (tokenizer.handle(beginIndex, i, equation, tokenList))
                    beginIndex = i + 1;

        String substring = equation.substring(beginIndex);

        if (!substring.isEmpty())
            tokenList.add(new ValueToken(substring));

        return tokenList;
    }
}
