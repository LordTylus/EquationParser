package io.lordtylus.equation.registers;

import io.lordtylus.equation.parsers.ConstantParser;
import io.lordtylus.equation.parsers.OperationParser;
import io.lordtylus.equation.parsers.ParenthesisParser;
import io.lordtylus.equation.parsers.VariableParser;

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

    public static final DefaultRegister INSTANCE = new DefaultRegister();

    private DefaultRegister() {

        register(ParenthesisParser.DEFAULT);
        register(OperationParser.DEFAULT);
        register(ConstantParser.INSTANCE);
        register(VariableParser.INSTANCE);
    }
}
