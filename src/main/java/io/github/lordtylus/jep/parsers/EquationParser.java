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
package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.options.ParsingOptions;
import io.github.lordtylus.jep.tokenizer.tokens.Token;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This interface is the base for parsing equation strings. It is expected that each {@link Equation} has one parser that is capable of creating the composite object hierarchy of parsed {@link Equation Equations}.
 * <p>
 * A parser must respect the following rules when parsing:
 * <p>
 * <b>Reject strings meant for a different parser</b><br>
 * If there's a string the parser cannot work with, reject it with an empty optional.
 * That way the parsing process can try again with the next parser in line till one was
 * found that matches it. Throwing an exception for an unparsable string may end up in
 * undesired behavior.
 * <p>
 * <b>Don't change the string passed to a different parser</b><br>
 * It may be tempting to just remove all whitespaces from the string to make parsing easier,
 * but it may be that another parser needs those whitespaces. Therefore, removing them may
 * lead to unwanted results. Only change the parts of the string that is not passed down to
 * a different parser. However, there is one exception. Trimming the string is before passing
 * it down to remove leading and trailing whitespaces is okay.
 * <p>
 * <b>Don't swallow exceptions</b><br>
 * While exceptions should be avoided, it may happen that due to unplanned behavior an exception
 * occurs. Most prominently are runtime exceptions such as NumberFormatException. Naturally
 * a parser should check the string first to avoid any exceptions, but in the event an edge case is
 * met, and exception may still slip through. If this happens, the caller should be able to handle
 * or log it, as well as be empowered to report it as a bug.
 */
public interface EquationParser {

    /**
     * This method parses the given tokenized equation to an equation object using the specified parsing options.
     * <p>
     * This method will be called recursively and requires a start and end index of the list to be passed in.
     * Those indices are altered by different parsers before starting the next recursion.
     *
     * @param tokenizedEquation List of string tokens to be used for parsing.
     * @param startIndex        start index of the list to parse.
     * @param endIndex          endIndex of the List to parse.
     * @param parsingOptions    Options which serve as source for the Parsers to be used.
     * @return an optional with the parsed equation or empty if parsing failed.
     */
    static Optional<Equation> parseEquation(
            @NonNull List<Token> tokenizedEquation,
            int startIndex,
            int endIndex,
            @NonNull ParsingOptions parsingOptions) {

        List<EquationParser> registeredParsers = parsingOptions.getRegisteredParsers();

        for (int i = 0; i < registeredParsers.size(); i++) {

            EquationParser parser = registeredParsers.get(i);

            Optional<Equation> parsed = parser.parse(tokenizedEquation, startIndex, endIndex, parsingOptions)
                    .map(Function.identity());

            if (parsed.isPresent())
                return parsed;
        }

        return Optional.empty();
    }

    /**
     * This method parses the given tokenized equation to an equation object using the specified parsing options.
     * <p>
     * The provided list may contain more information than needed, so the implementation has to respect the passed in start and end index of the list to parse correctly.
     *
     * @param tokenizedEquation The tokenized equation string to be parsed. E.g. 12.4|^|3|*|sqrt(|2|+|[x]|)|^|3
     * @param startIndex        start index of the list to parse.
     * @param endIndex          endIndex of the List to parse.
     * @param options           {@link ParsingOptions} to be used when recursively calling other parsers.
     * @return Optional with parsed Equation if parsing was successful. If parsing failed the optional will be empty.
     * @throws ParseException If a parser cannot parse an equation string an empty optional is expected. This exception can be thrown if a parser encounters an unexpected situation it cannot recover from.
     */
    Optional<? extends Equation> parse(
            @NonNull List<Token> tokenizedEquation,
            int startIndex,
            int endIndex,
            @NonNull ParsingOptions options);

}
