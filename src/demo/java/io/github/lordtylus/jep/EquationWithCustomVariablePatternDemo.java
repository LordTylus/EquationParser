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
package io.github.lordtylus.jep;

import io.github.lordtylus.jep.options.CustomParsingOptions;
import io.github.lordtylus.jep.parsers.variables.VariablePattern;
import io.github.lordtylus.jep.storages.SimpleStorage;

/**
 * This demo shows how custom wrapping for variables could be introduced.
 */
public class EquationWithCustomVariablePatternDemo {

    public static void main(String[] args) {

        String input = "2*:x:^:y:+5";

        CustomParsingOptions parsingOptions = CustomParsingOptions.withDefaults();
        parsingOptions.setVariablePattern(new VariablePattern(true, ':', ':'));

        Equation equation = Equation.parse(input, parsingOptions).get();

        SimpleStorage storage = new SimpleStorage();
        storage.putValue("y", 2);

        for (int i = 0; i < 5; i++) {

            storage.putValue("x", i);

            Result result = equation.evaluate(storage);

            System.out.println(result.asDouble()); //5, 7, 13, 23, 37
        }
    }
}
