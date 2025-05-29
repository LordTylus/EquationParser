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
package io.github.lordtylus.jep.tokenizer.tokens;

/**
 * This interface defines how a string token should look like after the equation string was tokenized.
 */
public interface Token {

    /**
     * @return The token string to be used for parsing.
     */
    String getString();

    /**
     * When traversing through all tokens we can determine on what layer we currently are.
     * Each opening parenthesis for example may increase the depth while a closing one decreases it.
     * <p>
     * that way we can check if everything is in order or if its unparsable.
     *
     * @param currentDepth current depth in the string to parse.
     * @return new depth
     */
    default int adjustDepth(int currentDepth) {
        return currentDepth;
    }
}