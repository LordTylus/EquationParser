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

import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.parsers.ParenthesisParser;
import io.github.lordtylus.jep.parsers.VariableParser;

/**
 * The default options contain the following parsers in order:
 * <ul>
 *     <li>{@link ParenthesisParser#DEFAULT}</li>
 *     <li>{@link OperationParser#DEFAULT}</li>
 *     <li>{@link ConstantParser#INSTANCE}</li>
 *     <li>{@link VariableParser#INSTANCE}</li>
 * </ul>
 */
public final class DefaultParserOptions extends AbstractParserOptions {

    /**
     * Immutable singleton instance of this class
     */
    public static final DefaultParserOptions INSTANCE = new DefaultParserOptions();

    private DefaultParserOptions() {

        register(ParenthesisParser.DEFAULT);
        register(OperationParser.DEFAULT);
        register(ConstantParser.INSTANCE);
        register(VariableParser.INSTANCE);
    }
}
