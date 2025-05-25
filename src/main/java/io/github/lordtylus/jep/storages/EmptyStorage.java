package io.github.lordtylus.jep.storages;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Storage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This special Storage implementation can be for evaluation {@link Equation equations} where no Variables are needed.
 * <p>
 * The implementation ensures that for every provided variable the int value 0 is returned. This implementation does not throw any exceptions for any non-null variable name.
 *
 * @see Equation#evaluate(Storage)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyStorage implements Storage {

    public static final Storage INSTANCE = new EmptyStorage();

    @Override
    public Number evaluate(@NonNull String variable) {
        return 0;
    }
}
