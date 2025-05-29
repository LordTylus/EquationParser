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

import io.github.lordtylus.jep.parsers.EquationParser;
import io.github.lordtylus.jep.tokenizer.EquationTokenizer;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This abstract implementation of {@link ParsingOptions} provides basic functionality
 * for adding and removing {@link EquationParser} objects for its implementations.
 */
public abstract class AbstractParserOptions implements ParsingOptions {

    private final List<EquationParser> registeredParsers = new ArrayList<>();
    private final List<EquationParser> registeredParsersUnmodifiable = Collections.unmodifiableList(registeredParsers);

    private final List<EquationTokenizer> registeredTokenizers = new ArrayList<>();
    private final List<EquationTokenizer> registeredTokenizersUnmodifiable = Collections.unmodifiableList(registeredTokenizers);

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

    @Override
    public List<EquationTokenizer> getRegisteredTokenizers() {
        return registeredTokenizersUnmodifiable;
    }

    /**
     * Registers a new {@link EquationTokenizer} to be used for parsing equation strings.
     *
     * @param tokenizer {@link EquationTokenizer} to be registered
     */
    protected void register(@NonNull EquationTokenizer tokenizer) {
        this.registeredTokenizers.add(tokenizer);
    }

    /**
     * Removes a registered {@link EquationTokenizer} so it will no longer be used for parsing.
     *
     * @param tokenizer {@link EquationTokenizer} to be removed
     */
    protected void unregister(@NonNull EquationTokenizer tokenizer) {
        this.registeredTokenizers.remove(tokenizer);
    }
}
