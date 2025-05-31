package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.tokenizer.tokens.ParenthesisToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenizerContextTest {

    @Test
    void initiallySplitIsNotProhibited() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();

        /* When / Then */

        assertFalse(sut.isSplitProhibited());
    }

    @Test
    void increaseBracketCountMeansSplitProhibited() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();

        /* When */

        sut.increaseBracketCount();

        /* Then */

        assertTrue(sut.isSplitProhibited());
    }

    @Test
    void decreaseCountBackToZeroAllowsSplittingAgain() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        sut.increaseBracketCount();

        /* When */

        sut.decreaseBracketCount();

        /* Then */

        assertFalse(sut.isSplitProhibited());
    }

    @Test
    void increaseCountTwiceFollowedByDecreaseMeansStillProhibited() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        sut.increaseBracketCount();
        sut.increaseBracketCount();

        /* When */

        sut.decreaseBracketCount();

        /* Then */

        assertTrue(sut.isSplitProhibited());
    }

    @Test
    void increaseFollowedByEqualNumberOfDecreaseUnlocksSplitting() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        sut.increaseBracketCount();
        sut.increaseBracketCount();
        sut.decreaseBracketCount();

        /* When */

        sut.decreaseBracketCount();

        /* Then */

        assertFalse(sut.isSplitProhibited());
    }

    @Test
    void hasNoParhenthisesTokensByDefault() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();

        /* When / Then */

        assertNull(sut.retrieveOpeningToken());
    }

    @Test
    void canAddParenthesisToken() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        ParenthesisToken token = new ParenthesisToken('(');

        /* When */

        sut.addOpeningToken(token);

        /* Then */

        ParenthesisToken actual = sut.retrieveOpeningToken();

        assertSame(token, actual);
    }

    @Test
    void afterAllParenthesisHaveBeenRetrievedResultIsNull() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        ParenthesisToken token = new ParenthesisToken('(');

        sut.addOpeningToken(token);
        sut.retrieveOpeningToken();

        /* When */

        ParenthesisToken actual = sut.retrieveOpeningToken();

        /* Then */

        assertNull(actual);
    }

    @Test
    void tokensAreReturnedInRightOrder() {

        /* Given */

        TokenizerContext sut = new TokenizerContext();
        ParenthesisToken token1 = new ParenthesisToken('(');
        ParenthesisToken token2 = new ParenthesisToken('(');
        ParenthesisToken token3 = new ParenthesisToken('(');

        sut.addOpeningToken(token1);
        sut.addOpeningToken(token2);
        sut.addOpeningToken(token3);

        /* When */

        ParenthesisToken actual3 = sut.retrieveOpeningToken();
        ParenthesisToken actual2 = sut.retrieveOpeningToken();
        ParenthesisToken actual1 = sut.retrieveOpeningToken();

        /* Then */

        assertSame(token1, actual1);
        assertSame(token2, actual2);
        assertSame(token3, actual3);
    }
}