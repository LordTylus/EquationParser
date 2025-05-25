package io.lordtylus.equation.operators;

import io.lordtylus.equation.parsers.OperationParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

/**
 * This parser can map a given char to an {@link Operator} based on the {@link Operator operators} pattern.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OperatorParser {

    /**
     * Performs the parsing according to class definition.
     * <p>
     * This class does not handle conflicts of patters in the provided {@link Operator operators}.
     * If there is a conflict, the first {@link Operator} encountered will be returned. What is first depends on the order of the passed Collection.
     * In case of an unordered set, it cannot be predicted which {@link Operator} will be first.
     *
     * @param relevantOperators Collection of {@link Operator operators} used for matching.
     * @param pattern           character pattern to match.
     * @return Optional with parsed {@link Operator} or empty() if there is no match.
     * @see OperationParser
     */
    public static Optional<Operator> parse(
            @NonNull Collection<Operator> relevantOperators,
            char pattern) {

        return relevantOperators.stream()
                .filter(operator -> pattern == operator.getPattern())
                .findFirst();
    }
}
