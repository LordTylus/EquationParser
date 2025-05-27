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

import lombok.NonNull;

/**
 * This is the default token when tokenizing a string.
 * If there are no parenthesis or operators, or anything else splitting the string,
 * this object will be used instead.
 */
public record ValueToken(
        @NonNull String value) implements Token {

    @Override
    public String getString() {
        return value;
    }
}