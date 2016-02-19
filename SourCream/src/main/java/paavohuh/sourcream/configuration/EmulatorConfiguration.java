
package paavohuh.sourcream.configuration;

import java.awt.Color;

/**
 * Configuration for the emulator. These settings have no effect on emulation;
 * they're only used user preferences.
 */
public class EmulatorConfiguration {

    public final ScreenConfiguration screen;

    public EmulatorConfiguration(ScreenConfiguration screen) {
        this.screen = screen;
    }
    
    public static EmulatorConfiguration getDefault() {
        return new EmulatorConfiguration(ScreenConfiguration.getDefault());
    }
    
    public enum ScreenEmulationStrategy {
        REDRAW_ON_BLIT,
        EMULATE_GHOSTING
    }
    
    /**
     * Contains the settings for screen emulation.
     */
    public static class ScreenConfiguration {
        public final int scaleFactor;
    
        public final Color backgroundColor;
        public final Color foregroundColor;
        
        public final boolean emulateGhosting;
        public final GhostingEmulationConfiguration ghosting;

        public ScreenConfiguration(int scaleFactor, Color backgroundColor, Color foregroundColor, boolean emulateGhosting, GhostingEmulationConfiguration ghosting) {
            this.scaleFactor = scaleFactor;
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
            this.emulateGhosting = emulateGhosting;
            this.ghosting = ghosting;
        }
        
        public static ScreenConfiguration getDefault() {
            return new ScreenConfiguration(10, Color.green, Color.darkGray, true, GhostingEmulationConfiguration.getDefault());
        }
    }
    
    /**
     * Contains the settings for screen ghosting emulation.
     */
    public static class GhostingEmulationConfiguration {
        public final float addBy;
        public final float subtractBy;

        public GhostingEmulationConfiguration(float addBy, float subtractBy) {
            this.addBy = addBy;
            this.subtractBy = subtractBy;
        }
        
        public static GhostingEmulationConfiguration getDefault() {
            return new GhostingEmulationConfiguration(0.50f, 0.015f);
        }
    }
}
