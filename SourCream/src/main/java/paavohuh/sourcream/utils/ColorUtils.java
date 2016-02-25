
package paavohuh.sourcream.utils;

import java.awt.Color;

/**
 * Contains utilities for manipulating colors.
 */
public class ColorUtils {
    
    /**
     * Linearly interpolates between two colors.
     * @param one The "one" color. At its maximum when level = 1.0.
     * @param zero The "zero" color. At its maximum when level = 0.0.
     * @param level The fade level between the colors.
     * @return The mixed color.
     */
    public static Color lerp(Color one, Color zero, float level) {
        float inverseLevel = 1.0f - level;
                
        float red =
            (one.getRed() / 255f) * level +
            (zero.getRed() / 255f) * inverseLevel;

        float green =
            (one.getGreen() / 255f) * level +
            (zero.getGreen() / 255f) * inverseLevel;

        float blue =
            (one.getBlue() / 255f) * level +
            (zero.getBlue() / 255f) * inverseLevel;

        return new Color(
            MathUtils.clamp(0.0f, red, 1.0f),
            MathUtils.clamp(0.0f, green, 1.0f),
            MathUtils.clamp(0.0f, blue, 1.0f));
    }
    
    /**
     * Inverts a color channel by channel.
     * @param color The color to invert.
     * @return The inverted color.
     */
    public static Color invert(Color color) {
        return new Color(
            255 - color.getRed(),
            255 - color.getGreen(),
            255 - color.getBlue());
    }
}
