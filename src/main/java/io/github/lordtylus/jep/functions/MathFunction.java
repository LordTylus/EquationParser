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
package io.github.lordtylus.jep.functions;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * This class represents a mathematics function, such as sqrt() that performs a calculation
 * with only one number.
 * <p>
 * What calculation exactly is performed is dependent on the passed {@link #evalFunction}
 * <p>
 * A function always has a pattern, that <b>must</b> be lowercase, and optionally can have
 * a collection of aliases under which, when being parsed, the Function can also be identified.
 */
@Value
@AllArgsConstructor
public class MathFunction {

    @NonNull
    String pattern;
    @NonNull
    List<String> aliases;
    @NonNull
    Function<Number, Number> evalFunction;

    /**
     * Creates a new function with the given pattern and evaluation function.
     *
     * @param pattern      pattern the parser should recognize this function by
     * @param evalFunction function to be executed when the equation is evaluated.
     */
    public MathFunction(
            @NonNull String pattern,
            @NonNull Function<Number, Number> evalFunction) {

        this(pattern, Collections.emptyList(), evalFunction);
    }

    /**
     * Returns the pattern name of the function, ignoring any aliases. The purpose of the method
     * is to help create an equation string that can be parsed later on.
     * It is not meant to recreate the exact string this function was originally parsed from.
     *
     * @return parsable string pattern of this function.
     */
    public String toPattern() {
        return pattern;
    }

    /**
     * Evaluates the given number and performs any calculations (if needed) according to the {@link #evalFunction}.
     * <p>
     * At this point it is important to note that the abstract concept of number will boil down to the concrete implementation of the function itself.
     * The {@link StandardFunctions} for example use double arithmetic, meaning that loss in precision can occur.
     * <p>
     * If precision is needed, using a custom implementation of an {@link MathFunction} is advised.
     *
     * @param number the function should perform its calculation on.
     * @return Resulting number of {@link #evalFunction}
     * @throws NullPointerException If any given argument is null.
     */
    public Number evaluate(
            @NonNull Number number) {

        return evalFunction.apply(number);
    }
}