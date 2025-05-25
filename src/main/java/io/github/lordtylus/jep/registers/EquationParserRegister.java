package io.github.lordtylus.jep.registers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.parsers.EquationParser;

import java.util.List;

/**
 * This register holds references to all {@link EquationParser EquationParsers} used for parsing equations.
 * <p>
 * It can be passed direction into {@link Equation#parse(String, EquationParserRegister)} and therefore can influence if and how an equation is parsed.
 * <p>
 * Its implementations are free to decide which {@link EquationParser} objects to use, for as long as they return everything they need in {@link #getRegisteredParsers()}
 */
public interface EquationParserRegister {

    /**
     * Returns a list of {@link EquationParser} classes to be used for solving mathematical equations.
     * <p>
     * See {@link DefaultRegister}
     *
     * @return {@link DefaultRegister} instance
     */
    static EquationParserRegister defaultRegister() {
        return DefaultRegister.INSTANCE;
    }

    /**
     * Returns a list of {@link EquationParser} objects to be used for parsing.
     * The order is important for parsing to prevent unnecessary checks and prevent
     * incorrect matches in case the EquationParsers must be executed in order to succeed.
     * This depends on the implementation of the {@link EquationParser} classes themselves.
     *
     * @return List of {@link EquationParser} to be used for parsing
     */
    List<EquationParser> getRegisteredParsers();
}
