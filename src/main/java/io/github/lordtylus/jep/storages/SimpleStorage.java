package io.github.lordtylus.jep.storages;

import io.github.lordtylus.jep.Equation;
import io.github.lordtylus.jep.Storage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a simple key-value storage used to solve variables when evaluating {@link Equation equations}.
 * <p>
 * It is possible to add and remove key and value pairs in a thread safe manner making this storage mutable.
 * <p>
 * However, when changing the storage during evaluation of an {@link Equation Equation} will results in an undeterministic and potentially wrong result.
 *
 * @see Equation#evaluate(Storage)
 */
@RequiredArgsConstructor
public final class SimpleStorage implements Storage {

    private final Map<String, Number> storage = new ConcurrentHashMap<>();

    @NonNull
    private final StorageMode storageMode;
    @NonNull
    private final Number defaultValue;

    /**
     * Initializes a new Storage with {@link StorageMode#STRICT}
     */
    public SimpleStorage() {
        this(StorageMode.STRICT);
    }

    /**
     * Initializes a new Storage with the provided mode and 0.0 as defaultValue.
     *
     * @param storageMode {@link StorageMode} to be used.
     * @throws NullPointerException If any given argument is null.
     */
    public SimpleStorage(
            @NonNull StorageMode storageMode) {

        this(storageMode, 0);
    }

    /**
     * Adds or replaces the value to a given variable name.
     *
     * @param variable name of the variable to be added or changed.
     * @param value    number value of the variable.
     * @throws NullPointerException If any given argument is null.
     */
    public void putValue(
            @NonNull String variable,
            @NonNull Number value) {

        storage.put(variable, value);
    }

    /**
     * Adds or replaces the value to a given variable name.
     *
     * @param variable name of the variable to be removed.
     * @throws NullPointerException If any given argument is null.
     */
    public void removeValue(
            @NonNull String variable) {

        storage.remove(variable);
    }

    /**
     * Evaluates the given variable name and returns its associated Number value used for evaluating {@link Equation equations}.
     * <p>
     * Depending on the {@link StorageMode}, the storage was initialized with, the behavior of this method can change.
     * <p>
     * If {@link StorageMode#STRICT} is set, this method will throw an {@link IllegalArgumentException} if an unknown variable is encountered.
     * However, if {@link StorageMode#UNKNOWN_MEANS_DEFAULT} is set, this method will treat unknown variables as the set defaultValue (standard 0.0) and won't throw any exception.
     * <p>
     * Which mode to choose depends on the use case.
     *
     * @param variable name of the variable to be retrieved.
     * @return Number associated with requested variable name.
     * @throws IllegalArgumentException if an unknown variable is encountered in {@link StorageMode#STRICT}
     */
    @Override
    public Number evaluate(
            @NonNull String variable) {

        Number value = storage.get(variable);

        if (value != null)
            return value;

        return storageMode.handleNull(variable, defaultValue);
    }
}
