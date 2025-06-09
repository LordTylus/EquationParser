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
package io.github.lordtylus.jep.parsers.variables;

import io.github.lordtylus.jep.equation.Constant;
import io.github.lordtylus.jep.equation.Operation;
import io.github.lordtylus.jep.options.ParsingOptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class provides some default patterns for the parsing of variables.
 * <p>
 * The {@link ParsingOptions} can be configured to use one of these standards or
 * even a custom pattern.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StandardVariablePatterns {

    /**
     * This is a pattern that does not escape the variables.
     * <p>
     * This pattern is easiest to input before parsing, more
     * error-prone as numbers or operators in the variable name
     * could be incorrectly interpreted as {@link Constant} or
     * {@link Operation} and therefore not lead to the desired result.
     */
    public static final VariablePattern NONE = new VariablePattern(false, '\u0000', '\u0000');
    /**
     * This is the default pattern, which wraps variable names between in [ and ]
     */
    public static final VariablePattern BRACKETS = new VariablePattern(true, '[', ']');
    /**
     * This is a pattern, which wraps variable names between in { and }
     */
    public static final VariablePattern BRACES = new VariablePattern(true, '{', '}');
    /**
     * This is a pattern, which wraps variable names between in &lt; and >
     */
    public static final VariablePattern TAGS = new VariablePattern(true, '<', '>');

}
