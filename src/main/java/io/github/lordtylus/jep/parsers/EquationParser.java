package io.github.lordtylus.jep.parsers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.registers.EquationParserRegister;
import lombok.NonNull;

import java.util.Optional;

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
     * This Method parses a given equation string to an {@link Equation} object. The returned Optional is empty, if the string couldn't be parsed. Otherwise, it contains the root {@link Equation} of the equation Tree.
     * <p>
     * It is ensured that the Root {@link Equation} will always match the implementation of this Parser. E.g. A {@link OperationParser} will always return an optional with an {@link Operation Operation} object if parsing was successful.
     *
     * @param equation The equation string to be parsed. E.g. 12.4^3*sqrt(2+[x])^3
     * @param register {@link EquationParserRegister} to be used when recursively calling other parsers.
     * @return Optional with parsed Equation if parsing was successful. If parsing failed the optional will be empty.
     * @throws ParseException If a parser cannot parse an equation string an empty optional is expected. This exception can be thrown if a parser encounters an unexpected situation it cannot recover from.
     */
    Optional<? extends Equation> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register);

}
