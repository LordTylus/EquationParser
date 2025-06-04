package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableTokenizerTest {

    @Test
    void openingParenthesisProhibitsSplitting() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();

        /* When */

        boolean actual = sut.handle(0, 0, '[', "(1+2)", tokenList, tokenizerContext);

        /* Then */

        assertFalse(actual);
        assertTrue(tokenizerContext.isSplitProhibited());
    }

    @Test
    void closingParenthesisMarksContextReadableAgain() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();
        sut.handle(0, 0, '[', "(1+2)", tokenList, tokenizerContext);

        /* When */

        boolean actual = sut.handle(1, 4, ']', "(1+2)", tokenList, tokenizerContext);

        /* Then */

        assertFalse(actual);
        assertFalse(tokenizerContext.isSplitProhibited());
    }
}