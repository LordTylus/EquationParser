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
package io.github.lordtylus.jep.options;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.functions.MathFunction;
import io.github.lordtylus.jep.functions.StandardFunctions;
import io.github.lordtylus.jep.operators.Operator;
import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.tokenizer.EquationTokenizer;
import io.github.lordtylus.jep.tokenizer.OperatorTokenizer;
import io.github.lordtylus.jep.tokenizer.ParenthesisTokenizer;
import io.github.lordtylus.jep.tokenizer.VariableTokenizer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * These are custom {@link ParsingOptions} which allow to be freely configured.
 * The main function is to determine which {@link EquationParser parsers} should
 * be used when Parsing Equations using {@link Equation#parse(String, ParsingOptions)}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomParsingOptions extends AbstractParsingOptions {

    /**
     * Creates a new Empty {@link ParsingOptions} object which is mutable can be manipulated
     * using {@link #register(EquationParser)} and {@link #unregister(EquationParser)}
     *
     * @return new mutable empty {@link CustomParsingOptions} object
     */
    public static CustomParsingOptions empty() {
        return new CustomParsingOptions();
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects from {@link DefaultParsingOptions#INSTANCE}
     *
     * @return new mutable {@link CustomParsingOptions} object with default {@link EquationParser parsers}
     */
    public static CustomParsingOptions withDefaults() {

        CustomParsingOptions empty = empty();
        DefaultParsingOptions.setupDefault(empty);

        return empty;
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects found in {@link DefaultParsingOptions#INSTANCE}.
     * <p>
     * However both {@link EquationParser parsers} and {@link EquationTokenizer tokenizers} are already preconfigured to only accept the passed in {@link Operator operators}.
     * <p>
     * Limiting the number of operators is not expected to have an impact on performance.
     *
     * @param operators varargs with the operators to use for parsing.
     * @return new mutable {@link CustomParsingOptions} with edited default config for passed operators.
     */
    public static CustomParsingOptions defaultWith(Operator... operators) {
        return defaultWithOperators(Arrays.asList(operators));
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects found in {@link DefaultParsingOptions#INSTANCE}.
     * <p>
     * However both {@link EquationParser parsers} and {@link EquationTokenizer tokenizers} are already preconfigured to only accept the passed in {@link Operator operators}.
     * <p>
     * Limiting the number of operators is not expected to have an impact on performance.
     *
     * @param operators collections with the operators to use for parsing.
     * @return new mutable {@link CustomParsingOptions} with edited default config for passed operators.
     */
    public static CustomParsingOptions defaultWithOperators(Collection<Operator> operators) {
        return defaultWith(StandardFunctions.all(), operators);
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects found in {@link DefaultParsingOptions#INSTANCE}.
     * <p>
     * However the parsers are configured to only recognize the passed in {@link MathFunction functions}.
     * <p>
     * This Method can be used to add your own functions to be recognized or limit which of the pre-existing ones will be recognized.
     * You can find all already existing ones by looking at {@link StandardFunctions}
     * <p>
     * Limiting the number of functions is not expected to have an impact on performance.
     *
     * @param functions varargs with the functions to use for parsing.
     * @return new mutable {@link CustomParsingOptions} with edited default config for passed functions.
     */
    public static CustomParsingOptions defaultWith(MathFunction... functions) {
        return defaultWithFunctions(Arrays.asList(functions));
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects found in {@link DefaultParsingOptions#INSTANCE}.
     * <p>
     * However the parsers are configured to only recognize the passed in {@link MathFunction functions}.
     * <p>
     * This Method can be used to add your own functions to be recognized or limit which of the pre-existing ones will be recognized.
     * You can find all already existing ones by looking at {@link StandardFunctions}
     * <p>
     * Limiting the number of functions is not expected to have an impact on performance.
     *
     * @param functions collection with the functions to use for parsing.
     * @return new mutable {@link CustomParsingOptions} with edited default config for passed functions.
     */
    public static CustomParsingOptions defaultWithFunctions(Collection<MathFunction> functions) {
        return defaultWith(functions, StandardOperators.all());
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects found in {@link DefaultParsingOptions#INSTANCE}.
     * <p>
     * This method is a combination of {@link #defaultWith(Operator...)} and
     * {@link #defaultWith(MathFunction...)} as it preconfigures these options to only work with the
     * operators and functions which were passed in.
     *
     * @param mathFunctions collection with the functions to use for parsing.
     * @param operators     collection with the operators to use for parsing.
     * @return new mutable {@link CustomParsingOptions} with edited default config for passed operators and functions.
     */
    public static CustomParsingOptions defaultWith(
            @NonNull Collection<MathFunction> mathFunctions,
            @NonNull Collection<Operator> operators) {

        CustomParsingOptions parserOptions = CustomParsingOptions.empty();

        OperationParser operationParser = new OperationParser(operators);

        parserOptions.register(new ParenthesisParser(mathFunctions));
        parserOptions.register(operationParser);
        parserOptions.register(ConstantParser.INSTANCE);
        parserOptions.register(VariableParser.INSTANCE);

        parserOptions.register(VariableTokenizer.INSTANCE);
        parserOptions.register(ParenthesisTokenizer.DEFAULT);
        parserOptions.register(new OperatorTokenizer(operationParser.getOperatorCharacters()));

        return parserOptions;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(@NonNull EquationTokenizer tokenizer) {
        super.register(tokenizer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(@NonNull EquationTokenizer tokenizer) {
        super.unregister(tokenizer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrorBehavior(ErrorBehavior errorBehavior) {
        super.setErrorBehavior(errorBehavior);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVariablePattern(@NonNull VariablePattern variablePattern) {
        super.setVariablePattern(variablePattern);
    }
}
