package io.lordtylus.equation.registers;

import io.lordtylus.equation.parsers.EquationParser;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractEquationParserRegister implements EquationParserRegister {

    private final List<EquationParser> registeredParsers = new ArrayList<>();
    private final List<EquationParser> registeredParsersUnmodifiable = Collections.unmodifiableList(registeredParsers);

    @Override
    public List<EquationParser> getRegisteredParsers() {
        return registeredParsersUnmodifiable;
    }

    /**
     * Registers a new {@link EquationParser} to be used for parsing equation strings.
     */
    void register(@NonNull EquationParser parser) {
        this.registeredParsers.add(parser);
    }

    /**
     * Removes a registered {@link EquationParser} so it will no longer be used for parsing.
     */
    void unregister(@NonNull EquationParser parser) {
        this.registeredParsers.remove(parser);
    }
}
