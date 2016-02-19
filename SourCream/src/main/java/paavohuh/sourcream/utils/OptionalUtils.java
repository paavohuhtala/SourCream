
package paavohuh.sourcream.utils;

import java.util.Optional;

/**
 * Contains utility functions for working with Optional objects.
 */
public class OptionalUtils {
    /**
     * Takes a builder function, runs it and wrap the result into an Optional.
     * If the builder throws an exception, returns an empty Optional.
     * @param <T> The type stored in Optional
     * @param builder A function that might throw and returns an instance of T.
     * @return The result of the function call, or an empty Optional.
     */
    public static <T> Optional<T> ofThrowing(ThrowingSupplier<T> builder) {
        try {
            return Optional.of(builder.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
