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
import io.github.lordtylus.jep.parsers.EquationParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * These are custom {@link ParsingOptions} which allow to be freely configured.
 * The main function is to determine which {@link EquationParser parsers} should
 * be used when Parsing Equations using {@link Equation#parse(String, ParsingOptions)}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomParserOptions extends AbstractParserOptions {

    /**
     * Creates a new Empty {@link ParsingOptions} object which is mutable can be manipulated
     * using {@link #register(EquationParser)} and {@link #unregister(EquationParser)}
     *
     * @return new mutable empty {@link CustomParserOptions} object
     */
    public static CustomParserOptions empty() {
        return new CustomParserOptions();
    }

    /**
     * Creates a new and mutable {@link ParsingOptions} object which already contains all
     * {@link EquationParser} objects from {@link DefaultParserOptions#INSTANCE}
     *
     * @return new mutable {@link CustomParserOptions} object with default {@link EquationParser parsers}
     */
    public static CustomParserOptions withDefaults() {

        CustomParserOptions empty = empty();

        DefaultParserOptions.INSTANCE.getRegisteredParsers()
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
