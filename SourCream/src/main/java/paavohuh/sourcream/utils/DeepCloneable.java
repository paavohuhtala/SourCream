
package paavohuh.sourcream.utils;

/**
 * Represents an object that can be deeply cloned. Classes implementing this
 * interface MUST guarantee the clone is completely independent of the original
 * object.
 * @param <T> The type of the cloned object.
 */
public interface DeepCloneable<T> {
    /**
     * Returns a deep clone of the object.
     * @return A deep clone.
     */
    public T cloned();
}
