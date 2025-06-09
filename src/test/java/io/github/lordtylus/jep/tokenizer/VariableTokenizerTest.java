package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.options.CustomParserOptions;
import io.github.lordtylus.jep.options.DefaultParserOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VariableTokenizerTest {

    @Test
    void returnsDelimitersOfVariablePattern() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        ParsingOptions parsingOptions = DefaultParserOptions.INSTANCE;

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        /* When */

        Set<Character> actual = sut.getDelimitersFor(parsingOptions);

        /* Then */

        Set<Character> expected = Set.of('[', ']');

        assertEquals(expected, actual);
    }

    @Test
    void hasNoDelimitersWhenPatternDoesHaveAnyEither() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        CustomParserOptions parsingOptions = CustomParserOptions.withDefaults();
        parsingOptions.setVariablePattern(StandardVariablePatterns.NONE);

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        /* When */

        Set<Character> actual = sut.getDelimitersFor(parsingOptions);

        /* Then */

        Set<Character> expected = Set.of();

        assertEquals(expected, actual);
    }

    @Test
    void openingParenthesisProhibitsSplitting() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        ParsingOptions parsingOptions = DefaultParserOptions.INSTANCE;

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();

        /* When */

        boolean actual = sut.handle(0, 0, '[', "(1+2)", tokenList, tokenizerContext, parsingOptions);

        /* Then */

        assertFalse(actual);
        assertTrue(tokenizerContext.isSplitProhibited());
    }

    @Test
    void closingParenthesisMarksContextReadableAgain() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        ParsingOptions parsingOptions = DefaultParserOptions.INSTANCE;

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();
        sut.handle(0, 0, '[', "(1+2)", tokenList, tokenizerContext, parsingOptions);

        /* When */

        boolean actual = sut.handle(1, 4, ']', "(1+2)", tokenList, tokenizerContext, parsingOptions);

        /* Then */

        assertFalse(actual);
        assertFalse(tokenizerContext.isSplitProhibited());
    }
}