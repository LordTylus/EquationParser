package io.github.lordtylus.jep.registers;

import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;

/**
 * The default Register that contains the following parsers in order:
 * <ul>
 *     <li>{@link ParenthesisParser#DEFAULT}</li>
 *     <li>{@link OperationParser#DEFAULT}</li>
 *     <li>{@link ConstantParser#INSTANCE}</li>
 *     <li>{@link VariableParser#INSTANCE}</li>
 * </ul>
 */
public final class DefaultRegister extends AbstractEquationParserRegister {

    /**
     * Immutable singleton instance of this class
     */
    public static final DefaultRegister INSTANCE = new DefaultRegister();

    private DefaultRegister() {

        register(ParenthesisParser.DEFAULT);
        register(OperationParser.DEFAULT);
        register(ConstantParser.INSTANCE);
        register(VariableParser.INSTANCE);
    }
}
