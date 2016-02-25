
package paavohuh.sourcream.utils;

/**
 * Contains additional mathematical functions.
 */
public class MathUtils {
    /**
     * Clamps a value between a min and a max, both inclusive. Equivalent to
     * calling Math.min(max, Math.max(x, min)).
     * @param min The minimum value.
     * @param x The value.
     * @param max The maximum value.
     * @return The clamped value.
     */
    public static float clamp(float min, float x, float max) {
        return Math.min(max, Math.max(x, min));
    }
}
