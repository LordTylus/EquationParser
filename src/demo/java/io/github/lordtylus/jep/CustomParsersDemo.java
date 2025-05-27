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

import io.github.lordtylus.jep.operators.StandardOperators;
import io.github.lordtylus.jep.parsers.ConstantParser;
import io.github.lordtylus.jep.parsers.OperationParser;
import io.github.lordtylus.jep.registers.CustomRegister;

import java.util.List;

/**
 * This demo shows how to manipulate the parsing by adding custom functionality.
 * In this case only constant values such as 2.0 or 14 are valid, and the only
 * operators allowed are addition and subtraction.
 * <p>
 * When working with custom settings the parserRegister has to be passed
 * into the Equation to bypass the default behavior.
 */
public class CustomParsersDemo {

    public static void main(String[] args) {

        CustomRegister parserRegister = CustomRegister.empty();

        parserRegister.register(ConstantParser.INSTANCE);
        parserRegister.register(new OperationParser(List.of(
                StandardOperators.ADD, StandardOperators.SUB
        )));

        var equation1 = Equation.parse("2+4-3", parserRegister);
        System.out.println(equation1.isPresent()); //true

        var equation2 = Equation.parse("2*4-3", parserRegister);
        System.out.println(equation2.isPresent()); //false
    }
}
