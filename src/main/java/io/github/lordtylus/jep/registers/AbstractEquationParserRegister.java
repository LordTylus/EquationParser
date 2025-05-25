package io.github.lordtylus.jep.registers;

import io.github.lordtylus.jep.parsers.EquationParser;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This abstract implementation of EquationParserRegister provides basic functionality
 * for adding and removing equation parsers for für its implementations.
 */
public abstract class AbstractEquationParserRegister implements EquationParserRegister {

    private final List<EquationParser> registeredParsers = new ArrayList<>();
    private final List<EquationParser> registeredParsersUnmodifiable = Collections.unmodifiableList(registeredParsers);

    @Override
    public List<EquationParser> getRegisteredParsers() {
        return registeredParsersUnmodifiable;
    }

    /**
     * Registers a new {@link EquationParser} to be used for parsing equation strings.
     *
     * @param parser {@link EquationParser} to be registered
     */
    protected void register(@NonNull EquationParser parser) {
        this.registeredParsers.add(parser);
    }

    /**
     * Removes a registered {@link EquationParser} so it will no longer be used for parsing.
     *
     * @param parser {@link EquationParser} to be removed
     */
    protected void unregister(@NonNull EquationParser parser) {
        this.registeredParsers.remove(parser);
    }
}
