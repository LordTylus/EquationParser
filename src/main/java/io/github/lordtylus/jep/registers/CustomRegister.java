package io.github.lordtylus.jep.registers;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.parsers.EquationParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is a custom {@link EquationParserRegister} that allows to freely configure
 * which {@link EquationParser parsers} should be used when Parsing Equations using
 * {@link Equation#parse(String, EquationParserRegister)}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomRegister extends AbstractEquationParserRegister {

    /**
     * Creates a new Empty {@link EquationParserRegister} that is mutable can be manipulated
     * using {@link #register(EquationParser)} and {@link #unregister(EquationParser)}
     */
    public static CustomRegister empty() {
        return new CustomRegister();
    }

    /**
     * Creates a new and mutable {@link EquationParserRegister} which already contains all
     * {@link EquationParser} objects from {@link DefaultRegister#INSTANCE}
     */
    public static CustomRegister withDefaults() {

        CustomRegister empty = empty();

        DefaultRegister.INSTANCE.getRegisteredParsers()
                .forEach(empty::register);

        return empty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(@NonNull EquationParser parser) {
        super.register(parser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(@NonNull EquationParser parser) {
        super.unregister(parser);
    }
}
