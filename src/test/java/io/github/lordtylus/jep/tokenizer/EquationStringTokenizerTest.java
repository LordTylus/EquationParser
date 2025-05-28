package io.github.lordtylus.jep.tokenizer;

import io.github.lordtylus.jep.options.CustomParserOptions;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquationStringTokenizerTest {

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1|+|1",
            "1+1^2 ; 1|+|1|^|2",
            "-1+1^2 ; -1|+|1|^|2",
            "-1+-1^2 ; -1|+|-|1|^|2",
            "2*(2+3)^2 ; 2|*|(|2|+|3|)|^|2",
            "2*(2+3)^(1/2) ; 2|*|(|2|+|3|)|^|(|1|/|2|)",
            "2*(2+3)^(-1/2) ; 2|*|(|2|+|3|)|^|(|-1|/|2|)",
            "2*sqrt(2+3)^2 ; 2|*|sqrt(|2|+|3|)|^|2",
            "2*sqrt(2+3)^abc(1/2) ; 2|*|sqrt(|2|+|3|)|^|abc(|1|/|2|)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (|-3|)|+|abs(|7.3|+|3|)|*|sin(|6|+|(|[hallo]|-|2|)|)|+|216|/|3|^|3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (|(|(|1|+|2|)|*|(|3|+|4|)|)|+|(|(|(|1|-|2|)|^|2|)|+|)|)",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs(|sin(|sqrt(|1|+|2|)|*|cos(|3|+|4|)|)|+|lol(|sss(|ddd(|1|-|2|)|^|2|)|+|)|sdf|)",
            "-1--1 ; -1|-|-|1",
            "2(x+2) ; 2(|x|+|2|)",
            "2(x+2 ; 2(|x|+|2",
            "2x+2) ; 2x|+|2|)",
    }, delimiter = ';')
    void tokenizesDefault(String input, String expected) {

        /* Given */

        ParsingOptions options = CustomParserOptions.withDefaults();

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-  1 ; -  1",
            " a b c  ; a b c",
            " 1 + 1  ; 1 |+| 1",
            " 1 + 1 ^ 2 ; 1 |+| 1 |^| 2",
            " - 1 + 1 ^ 2  ; - 1 |+| 1 |^| 2",
            " - 1 + -   1 ^  2 ; - 1 |+| |-|   1 |^|  2",
            " 2 *   ( 2 + 3 ) ^ 2 ; 2 |*|   (| 2 |+| 3 |)| |^| 2",
            "   2 * ( 2  +  3 ) ^ ( 1 / 2  ) ; 2 |*| (| 2  |+|  3 |)| |^| (| 1 |/| 2  |)",
            "   2 * (   2   +   3   ) ^ (  - 1 / 2 )  ; 2 |*| (|   2   |+|   3   |)| |^| (|  |-| 1 |/| 2 |)",
            " 2   * s q r   t ( 2 + 3 ) ^ 2 ; 2   |*| s q r   t (| 2 |+| 3 |)| |^| 2",
            "  2 * s q r t  ( 2 + 3 ) ^ a b c ( 1/ 2 )  ; 2 |*| s q r t  (| 2 |+| 3 |)| |^| a b c (| 1|/| 2 |)",
            " (  - 3 )  + a b s (7   . 3 + 3  ) *  s i  n ( 6 +  ( [ h a l   l  o ] - 2 ) )   + 2 1 6 /    3^ 3 ; (|  |-| 3 |)|  |+| a b s (|7   . 3 |+| 3  |)| |*|  s i  n (| 6 |+|  (| [ h a l   l  o ] |-| 2 |)| |)|   |+| 2 1 6 |/|    3|^| 3",
            "  (  ( ( 1 + 2   ) * ( 3 + 4 ) )+  ((  ( 1 - 2  ) ^ 2) + ) )  ; (|  (| (| 1 |+| 2   |)| |*| (| 3 |+| 4 |)| |)|+|  (|(|  (| 1 |-| 2  |)| |^| 2|)| |+| |)| |)",
    }, delimiter = ';')
    void tokenizesSpaces(String input, String expected) {

        /* Given */

        ParsingOptions options = CustomParserOptions.withDefaults();

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1|+|1",
            "1+1^2 ; 1|+|1|^|2",
            "-1+1^2 ; -1|+|1|^|2",
            "-1+-1^2 ; -1|+|-|1|^|2",
            "2*(2+3)^2 ; 2|*|(|2|+|3|)|^|2",
            "2*(2+3)^(1/2) ; 2|*|(|2|+|3|)|^|(|1|/|2|)",
            "2*(2+3)^(-1/2) ; 2|*|(|2|+|3|)|^|(|-1|/|2|)",
            "2*sqrt(2+3)^2 ; 2|*|sqrt|(|2|+|3|)|^|2",
            "2*sqrt(2+3)^abc(1/2) ; 2|*|sqrt|(|2|+|3|)|^|abc|(|1|/|2|)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (|-3|)|+|abs|(|7.3|+|3|)|*|sin|(|6|+|(|[hallo]|-|2|)|)|+|216|/|3|^|3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (|(|(|1|+|2|)|*|(|3|+|4|)|)|+|(|(|(|1|-|2|)|^|2|)|+|)|)",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs|(|sin|(|sqrt|(|1|+|2|)|*|cos|(|3|+|4|)|)|+|lol|(|sss|(|ddd|(|1|-|2|)|^|2|)|+|)|sdf|)",
            "-1--1 ; -1|-|-|1",
            "2(x+2) ; 2|(|x|+|2|)",
    }, delimiter = ';')
    void tokenizesWithoutFunctions(String input, String expected) {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.unregister(ParenthesisTokenizer.DEFAULT);
        options.register(new ParenthesisTokenizer(false));

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1+1",
            "1+1^2 ; 1+1^2",
            "-1+1^2 ; -1+1^2",
            "-1+-1^2 ; -1+-1^2",
            "2*(2+3)^2 ; 2*|(|2+3|)|^2",
            "2*(2+3)^(1/2) ; 2*|(|2+3|)|^|(|1/2|)",
            "2*(2+3)^(-1/2) ; 2*|(|2+3|)|^|(|-1/2|)",
            "2*sqrt(2+3)^2 ; 2*sqrt|(|2+3|)|^2",
            "2*sqrt(2+3)^abc(1/2) ; 2*sqrt|(|2+3|)|^abc|(|1/2|)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (|-3|)|+abs|(|7.3+3|)|*sin|(|6+|(|[hallo]-2|)|)|+216/3^3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (|(|(|1+2|)|*|(|3+4|)|)|+|(|(|(|1-2|)|^2|)|+|)|)",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs|(|sin|(|sqrt|(|1+2|)|*cos|(|3+4|)|)|+lol|(|sss|(|ddd|(|1-2|)|^2|)|+|)|sdf|)",
            "-1--1 ; -1--1",
            "2(x+2) ; 2|(|x+2|)",
    }, delimiter = ';')
    void tokenizesWithoutFunctionsAndOperators(String input, String expected) {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.unregister(ParenthesisTokenizer.DEFAULT);
        options.unregister(OperatorTokenizer.DEFAULT);
        options.register(new ParenthesisTokenizer(false));

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1|+|1",
            "1+1^2 ; 1|+|1|^|2",
            "-1+1^2 ; -1|+|1|^|2",
            "-1+-1^2 ; -1|+|-|1|^|2",
            "2*(2+3)^2 ; 2|*|(2|+|3)|^|2",
            "2*(2+3)^(1/2) ; 2|*|(2|+|3)|^|(1|/|2)",
            "2*(2+3)^(-1/2) ; 2|*|(2|+|3)|^|(-1|/|2)",
            "2*sqrt(2+3)^2 ; 2|*|sqrt(2|+|3)|^|2",
            "2*sqrt(2+3)^abc(1/2) ; 2|*|sqrt(2|+|3)|^|abc(1|/|2)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (-3)|+|abs(7.3|+|3)|*|sin(6|+|([hallo]|-|2))|+|216|/|3|^|3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (((1|+|2)|*|(3|+|4))|+|(((1|-|2)|^|2)|+|))",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs(sin(sqrt(1|+|2)|*|cos(3|+|4))|+|lol(sss(ddd(1|-|2)|^|2)|+|)sdf)",
            "-1--1 ; -1|-|-|1",
            "2(x+2) ; 2(x|+|2)",
    }, delimiter = ';')
    void tokenizesOnlyOperators(String input, String expected) {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.unregister(ParenthesisTokenizer.DEFAULT);

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1|+|1",
            "1+1^2 ; 1|+|1^2",
            "-1+1^2 ; -1|+|1^2",
            "-1+-1^2 ; -1|+|-1^2",
            "2*(2+3)^2 ; 2*(2|+|3)^2",
            "2*(2+3)^(1/2) ; 2*(2|+|3)^(1/2)",
            "2*(2+3)^(-1/2) ; 2*(2|+|3)^(-1/2)",
            "2*sqrt(2+3)^2 ; 2*sqrt(2|+|3)^2",
            "2*sqrt(2+3)^abc(1/2) ; 2*sqrt(2|+|3)^abc(1/2)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (-3)|+|abs(7.3|+|3)*sin(6|+|([hallo]-2))|+|216/3^3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (((1|+|2)*(3|+|4))|+|(((1-2)^2)|+|))",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs(sin(sqrt(1|+|2)*cos(3|+|4))|+|lol(sss(ddd(1-2)^2)|+|)sdf)",
            "-1--1 ; -1--1",
            "2(x+2) ; 2(x|+|2)",
    }, delimiter = ';')
    void tokenizesOnlyPlusSign(String input, String expected) {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.unregister(ParenthesisTokenizer.DEFAULT);
        options.unregister(OperatorTokenizer.DEFAULT);

        options.register(new OperatorTokenizer(Set.of('+')));

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1 ; 1",
            "-1 ; -1",
            "abc ; abc",
            "1+1 ; 1+1",
            "1+1^2 ; 1+1^2",
            "-1+1^2 ; -1+1^2",
            "-1+-1^2 ; -1+-1^2",
            "2*(2+3)^2 ; 2*(2+3)^2",
            "2*(2+3)^(1/2) ; 2*(2+3)^(1/2)",
            "2*(2+3)^(-1/2) ; 2*(2+3)^(-1/2)",
            "2*sqrt(2+3)^2 ; 2*sqrt(2+3)^2",
            "2*sqrt(2+3)^abc(1/2) ; 2*sqrt(2+3)^abc(1/2)",
            "(-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3 ; (-3)+abs(7.3+3)*sin(6+([hallo]-2))+216/3^3",
            "(((1+2)*(3+4))+(((1-2)^2)+)) ; (((1+2)*(3+4))+(((1-2)^2)+))",
            "abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf) ; abs(sin(sqrt(1+2)*cos(3+4))+lol(sss(ddd(1-2)^2)+)sdf)",
            "-1--1 ; -1--1",
            "2(x+2) ; 2(x+2)",
    }, delimiter = ';')
    void tokenizesNothing(String input, String expected) {

        /* Given */

        CustomParserOptions options = CustomParserOptions.withDefaults();
        options.unregister(ParenthesisTokenizer.DEFAULT);
        options.unregister(OperatorTokenizer.DEFAULT);

        /* When */

        List<Token> tokenized = EquationStringTokenizer.tokenize(input, options);

        /* Then */

        List<String> tokenList = tokenized.stream()
                .map(Token::getString)
                .toList();

        String actual = String.join("|", tokenList);

        assertEquals(expected, actual);
    }
}