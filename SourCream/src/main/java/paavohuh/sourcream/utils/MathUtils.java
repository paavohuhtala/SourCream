
package paavohuh.sourcream.utils;

public class MathUtils {
    public static float clamp(float min, float x, float max) {
        return Math.min(max, Math.max(x, min));
    }
}
