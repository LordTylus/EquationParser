package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.options.CustomParsingOptions;
import io.github.lordtylus.jep.options.DefaultParsingOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
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

        ParsingOptions parsingOptions = DefaultParsingOptions.INSTANCE;

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

        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(StandardVariablePatterns.NONE);

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        /* When */

        Set<Character> actual = sut.getDelimitersFor(parsingOptions);

        /* Then */

        Set<Character> expected = Set.of();

        assertEquals(expected, actual);
    }

    @Test
    void onlyReturnsOneDelimiterIfThePatternUsesTheSameForOpeningAndClosing() {

        /* Given */

        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(new VariablePattern(true, ':', ':'));

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        /* When */

        Set<Character> actual = sut.getDelimitersFor(parsingOptions);

        /* Then */

        Set<Character> expected = Set.of(':');

        assertEquals(expected, actual);
    }

    @Test
    void openingParenthesisProhibitsSplitting() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        ParsingOptions parsingOptions = DefaultParsingOptions.INSTANCE;

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
        ParsingOptions parsingOptions = DefaultParsingOptions.INSTANCE;

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();
        sut.handle(0, 0, '[', "[1+2]", tokenList, tokenizerContext, parsingOptions);

        /* When */

        boolean actual = sut.handle(1, 4, ']', "[1+2]", tokenList, tokenizerContext, parsingOptions);

        /* Then */

        assertFalse(actual);
        assertFalse(tokenizerContext.isSplitProhibited());
    }

    @Test
    void firstOccurrenceOfOpeningDelimiterWillProhibitSplittingIfOpeningAndClosingAreIdentical() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(new VariablePattern(true, ':', ':'));

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();

        /* When */

        boolean actual = sut.handle(0, 0, ':', ":hello:", tokenList, tokenizerContext, parsingOptions);

        /* Then */

        assertFalse(actual);
        assertTrue(tokenizerContext.isSplitProhibited());
    }

    @Test
    void secondOccurrenceOfOpeningDelimiterWillAllowSplittingCountIfOpeningAndClosingAreIdentical() {

        /* Given */

        TokenizerContext tokenizerContext = new TokenizerContext();
        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(new VariablePattern(true, ':', ':'));

        VariableTokenizer sut = VariableTokenizer.INSTANCE;

        List<Token> tokenList = new ArrayList<>();

        sut.handle(0, 0, ':', ":hello:", tokenList, tokenizerContext, parsingOptions);

        /* When */

        boolean actual = sut.handle(0, 6, ':', ":hello:", tokenList, tokenizerContext, parsingOptions);

        /* Then */

        assertFalse(actual);
        assertFalse(tokenizerContext.isSplitProhibited());
    }
}