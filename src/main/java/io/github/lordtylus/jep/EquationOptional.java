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

import io.github.lordtylus.jep.parsers.ParseResult;
import lombok.Getter;
import lombok.Value;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * This class serves a similar purpose to a Java {@link Optional} with the
 * exception that it only works for {@link Equation} objects and has less convenience.
 * <p>
 * However, this Optional Class does have additional information and wraps not only the
 * parsed {@link Equation} but in case of a problem while parsing an error message.
 * <p>
 * There is the option to retrieve a Java Optional for mapping or streaming if necessary,
 * but simple methods such as {@link #get()}, {@link #isPresent()} or {@link #hasError()}
 * provided directly.
 */
@Value
public class EquationOptional {

    /**
     * This is the parsed equation. It may be null if a parsing error occurred.
     */
    Equation equation;
    /**
     * Contains the error message that occurred either during tokenizing or parsing.
     * In case of an exception it will contain the exception message.
     * <p>
     * If {@link #hasError()} returns true a non-null error message can be expected,
     * however it might still be empty.
     */
    @Getter
    String errorMessage;
    /**
     * If parsing failed due to an exception, the exceptions reference can be retrieved
     * through this field / method.
     * <p>
     * Even in case of {@link #hasError()} returning true, the throwable might still be null
     * if none was cause for the parsing to fail.
     */
    @Getter
    Throwable throwable;

    /**
     * Returns the {@link Equation} on the fly as a new java {@link Optional} to
     * allow use of mapping and streaming functions if needed.
     *
     * @return Optional of the {@link Equation}, which will be empty if an error occurred.
     */
    public Optional<Equation> asOptional() {
        return Optional.ofNullable(equation);
    }

    /**
     * This method functions like {@link Optional#get()} and returns the parsed
     * {@link Equation}. However, just like the java counterpart it will throw an
     * exception if a parsing error had occurred and there's no Equation to get.
     * <p>
     * It is advised to check {@link #isPresent()} or {@link #hasError()} first.
     *
     * @return The parsed {@link Equation}
     * @throws NoSuchElementException if a parsing error had occurred.
     */
    public Equation get() {

        if (equation == null)
            throw new NoSuchElementException("Equation not present!");

        return equation;
    }

    /**
     * This method is equivalent to {@link Optional#isPresent()} and checks if an {@link Equation}
     * was set or if an error had occurred. If this Method returns false, you will be able to
     * retrieve the errorMessage and or exception through {@link #errorMessage} and {@link #throwable}
     * <p>
     * However, it is not guaranteed that these contain anything useful, or are even set.
     *
     * @return true if parsing was successful.
     */
    public boolean isPresent() {
        return equation != null;
    }

    /**
     * This method is equivalent to {@link Optional#isEmpty()} and checks if an error occurred during parsing.
     * If this Method returns true, you will be able to retrieve the error message through {@link #errorMessage}.
     * It is guaranteed to be non-null in this instance. Though it might be an empty string.
     * <p>
     * Additionally, if this method returns true, {@link #throwable} might return the exception, if any caused this error to happen.
     *
     * @return true if parsing resulted in an error.
     */
    public boolean hasError() {
        return equation == null;
    }

    /**
     * Creates a new {@link EquationOptional} out of the given {@link ParseResult}.
     * If parsing was successful, the result's {@link Equation} will be set.
     * If an error had occurred, the errorMessage will be stored.
     * This method is not meant to report exceptions.
     *
     * @param parseResult Result object created during parsing.
     * @return EquationOptional new instance of this class.
     */
    public static EquationOptional of(ParseResult parseResult) {

        return parseResult.getEquation()
                .map(equation -> new EquationOptional(equation, null, null))
                .orElseGet(() -> new EquationOptional(null, parseResult.getErrorMessage(), null));
    }

    /**
     * Creates a new {@link EquationOptional} out of the given {@link Throwable}.
     * <p>
     * All instances created using this method are errors, whose message is the message
     * of the exception itself.
     *
     * @param throwable Throwable that occurred during parsing.
     * @return EquationOptional new instance of this class.
     */
    public static EquationOptional of(Throwable throwable) {
        return new EquationOptional(null, throwable.getMessage(), throwable);
    }
}
