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

import io.github.lordtylus.jep.storages.SimpleStorage;

/**
 * This demo shows how to work with variables in the default configuration.
 * Variables are passed using brackets such as [x] and can take any value.
 * <p>
 * The current value of x is stored in the SimpleStorage implementation and
 * passed into the evaluate method.
 * <p>
 * The equation only needs to be parsed once, and can be used as many times as needed.
 * The default implementations of this framework are stateless and thread safe.
 * <p>
 * However, if the content of the storage changes during evaluation the result will
 * be undefined, and can be potentially wrong.
 */
public class EquationWithVariablesDemo {

    public static void main(String[] args) {

        String input = "2*[x]^2+5";

        Equation equation = Equation.parse(input).orElseThrow(
                () -> new IllegalArgumentException("Could not parse!"));

        SimpleStorage storage = new SimpleStorage();

        for (int i = 0; i < 5; i++) {

            storage.putValue("x", i);

            Result result = equation.evaluate(storage);

            System.out.println(result.asDouble()); //5, 7, 13, 23, 37
        }
    }
}
