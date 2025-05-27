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
     *
     * @return new mutable empty {@link CustomRegister} class
     */
    public static CustomRegister empty() {
        return new CustomRegister();
    }

    /**
     * Creates a new and mutable {@link EquationParserRegister} which already contains all
     * {@link EquationParser} objects from {@link DefaultRegister#INSTANCE}
     *
     * @return new mutable {@link CustomRegister} class with default {@link EquationParser parsers}
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
