
package paavohuh.sourcream.utils;

import java.util.function.Supplier;

/**
 * A lazy wrapper for an object. The value is only initialized once and only
 * when required.
 * @param <T> The type of the stored object
 */
public class Cached<T> {
    private T value;
    private final Supplier<T> initializer;

    /**
     * Creates a new Cached instace. The value is initialized with the
     * initializer when get() is called.
     * @param initializer 
     */
    public Cached(Supplier<T> initializer) {
        this.initializer = initializer;
    }
    
    /**
     * Gets the value. If it doesn't exist yet, the initializer is run and the
     * value is returned.
     * @return The value
     */
    public T get() {
        if (value == null) {
            value = initializer.get();
        }
        
        return value;
    }
}
