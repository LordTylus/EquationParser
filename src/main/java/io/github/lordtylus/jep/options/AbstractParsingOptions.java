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
import io.github.lordtylus.jep.parsers.variables.StandardVariablePatterns;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.tokenizer.EquationTokenizer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract implementation of {@link ParsingOptions} provides basic functionality
 * for adding and removing {@link EquationParser} objects for its implementations.
 */
public abstract class AbstractParsingOptions implements ParsingOptions {

    private final List<EquationParser> registeredParsers = new ArrayList<>();
    private final List<EquationParser> registeredParsersUnmodifiable = Collections.unmodifiableList(registeredParsers);

    private final List<EquationTokenizer> registeredTokenizers = new ArrayList<>();
    private final List<EquationTokenizer> registeredTokenizersUnmodifiable = Collections.unmodifiableList(registeredTokenizers);

    private final Map<Character, EquationTokenizer> tokenizerMapping = new HashMap<>();
    private final Map<Character, EquationTokenizer> tokenizerMappingUnmodifiable = Collections.unmodifiableMap(tokenizerMapping);

    /**
     * Decides if the parsing of {@link Equation equations} should throw exceptions
     * in case of an occurring error.
     */
    @NonNull
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ErrorBehavior errorBehavior = ErrorBehavior.ERROR_RESULT;

    @NonNull
    @Getter
    private VariablePattern variablePattern = StandardVariablePatterns.BRACKETS;

    @Override
    public Map<Character, EquationTokenizer> getTokenizerForDelimiterMap() {
        return tokenizerMappingUnmodifiable;
    }

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
     * @throws IllegalArgumentException if a tokenizer using the same delimiters is already registered.
     */
    protected void register(@NonNull EquationTokenizer tokenizer) {

        for (Character delimiter : tokenizer.getDelimitersFor(this))
            if (this.tokenizerMapping.containsKey(delimiter))
                throw new IllegalArgumentException("Tokenizer already exists!");

        this.registeredTokenizers.add(tokenizer);

        rebuildCharacterMapping();
    }

    /**
     * Removes a registered {@link EquationTokenizer} so it will no longer be used for parsing.
     *
     * @param tokenizer {@link EquationTokenizer} to be removed
     */
    protected void unregister(@NonNull EquationTokenizer tokenizer) {
        this.registeredTokenizers.remove(tokenizer);

        rebuildCharacterMapping();
    }

    /**
     * Decides how variables should be read during parsing.
     *
     * @param variablePattern to be used.
     */
    protected void setVariablePattern(
            @NonNull VariablePattern variablePattern) {

        this.variablePattern = variablePattern;

        rebuildCharacterMapping();
    }

    private void rebuildCharacterMapping() {

        this.tokenizerMapping.clear();

        for (EquationTokenizer tokenizer : this.registeredTokenizers)
            for (char c : tokenizer.getDelimitersFor(this))
                this.tokenizerMapping.put(c, tokenizer);
    }
}
