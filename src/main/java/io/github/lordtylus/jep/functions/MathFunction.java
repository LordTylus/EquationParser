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

    public MathFunction(
            @NonNull String pattern,
            @NonNull Function<Number, Number> evalFunction) {

        this(pattern, Collections.emptyList(), evalFunction);
    }

    /**
     * Returns the pattern name of the function.
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
     * @return Resulting number of {@link #evalFunction}
     * @throws NullPointerException If any given argument is null.
     */
    public Number evaluate(
            @NonNull Number a) {

        return evalFunction.apply(a);
    }
}