
package paavohuh.sourcream.utils;

import java.awt.Color;

public class ColorUtils {
    
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
}
