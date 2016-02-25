
package paavohuh.sourcream.configuration;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import paavohuh.sourcream.utils.DeepCloneable;

/**
 * Configuration for the emulator. These settings have no effect on emulation;
 * they're only used user preferences.
 */
public class EmulatorConfiguration implements DeepCloneable<EmulatorConfiguration> {

    private ScreenConfiguration screen;
    private InputConfiguration input;

    /**
     * Creates a new emulator config.
     * @param screen The screen configuration
     * @param mapping The input configuration
     */
    public EmulatorConfiguration(ScreenConfiguration screen, InputConfiguration mapping) {
        this.screen = screen;
        this.input = mapping;
    }
    
    /**
     * Returns the default emulator configuration.
     * @return The configuration.
     */
    public static EmulatorConfiguration getDefault() {
        return new EmulatorConfiguration(
            ScreenConfiguration.getDefault(),
            InputConfiguration.getDefault());
    }

    @Override
    public EmulatorConfiguration cloned() {
        return new EmulatorConfiguration(getScreen().cloned(), getInput().cloned());
    }

    public ScreenConfiguration getScreen() {
        return screen;
    }

    public void setScreen(ScreenConfiguration screen) {
        this.screen = screen;
    }

    public InputConfiguration getInput() {
        return input;
    }

    public void setInput(InputConfiguration input) {
        this.input = input;
    }
        
    /**
     * Contains the settings for screen emulation.
     */
    public static class ScreenConfiguration implements DeepCloneable<ScreenConfiguration> {
        private int scaleFactor;
        private ColorScheme colors;
        private boolean emulateGhosting;
        private GhostingConfiguration ghosting;

        /**
         * Creates a new screen config.
         * @param scaleFactor The size of pixels in the emulated screen.
         * @param colors The color scheme.
         * @param emulateGhosting Should the emulator emulate LCD ghosting?
         * @param ghosting Configuration for ghosting emulation.
         */
        public ScreenConfiguration(int scaleFactor, ColorScheme colors, boolean emulateGhosting, GhostingConfiguration ghosting) {
            this.scaleFactor = scaleFactor;
            this.colors = colors;
            this.emulateGhosting = emulateGhosting;
            this.ghosting = ghosting;
        }
        
        /**
         * Returns the default screen configuration.
         * @return The default screen configuration.
         */
        public static ScreenConfiguration getDefault() {
            return new ScreenConfiguration(10, ColorScheme.getDefault(), true, GhostingConfiguration.getDefault());
        }

        @Override
        public ScreenConfiguration cloned() {
            return new ScreenConfiguration(
                getDisplayScale(),
                getColors().cloned(),
                emulateGhosting(),
                getGhosting().cloned());
        }

        public int getDisplayScale() {
            return scaleFactor;
        }

        public void setScaleFactor(int scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        public ColorScheme getColors() {
            return colors;
        }

        public void setColors(ColorScheme colors) {
            this.colors = colors;
        }

        public boolean emulateGhosting() {
            return emulateGhosting;
        }

        public void setEmulateGhosting(boolean emulateGhosting) {
            this.emulateGhosting = emulateGhosting;
        }

        public GhostingConfiguration getGhosting() {
            return ghosting;
        }

        public void setGhosting(GhostingConfiguration ghosting) {
            this.ghosting = ghosting;
        }
        
        /**
         * Contains the colors for the emulated screen.
         */
        public static class ColorScheme implements DeepCloneable<ColorScheme> {
            private Color background;
            private Color foreground;

            public ColorScheme(Color background, Color foreground) {
                this.background = background;
                this.foreground = foreground;
            }
            
            /**
             * Returns the default color scheme, consisting of a green
             * background color and a dark gray foreground color.
             * @return 
             */
            public static ColorScheme getDefault() {
                return new ColorScheme(Color.green, Color.darkGray);
            }

            @Override
            public ColorScheme cloned() {
                return new ColorScheme(getBackground(), getForeground());
            }

            public Color getBackground() {
                return background;
            }

            public void setBackground(Color background) {
                this.background = background;
            }

            public Color getForeground() {
                return foreground;
            }

            public void setForeground(Color foreground) {
                this.foreground = foreground;
            }
        }
    }
    
    /**
     * Contains the settings for screen ghosting emulation.
     */
    public static class GhostingConfiguration implements DeepCloneable<GhostingConfiguration> {
        private float addBy;
        private float subtractBy;

        /**
         * Creates a new ghosting emulation config.
         * @param addBy
         * @param subtractBy 
         */
        public GhostingConfiguration(float addBy, float subtractBy) {
            this.addBy = addBy;
            this.subtractBy = subtractBy;
        }
        
        /**
         * Returns the default ghosting emulation config.
         * @return Ghosting configuration.
         */
        public static GhostingConfiguration getDefault() {
            return new GhostingConfiguration(0.50f, 0.015f);
        }

        @Override
        public GhostingConfiguration cloned() {
            return new GhostingConfiguration(getAddBy(), getSubtractBy());
        }

        public float getAddBy() {
            return addBy;
        }

        public void setAddBy(float addBy) {
            this.addBy = addBy;
        }

        public float getSubtractBy() {
            return subtractBy;
        }

        public void setSubtractBy(float subtractBy) {
            this.subtractBy = subtractBy;
        }
    }
    
    /**
     * Contains the mappings between Swing virtual key codes and the Chip-8 hex
     * keys.
     */
    public static class InputConfiguration implements DeepCloneable<InputConfiguration> {
        private final HashMap<Integer, Integer> bindings;

        /**
         * Creates new 
         * @param bindings 
         */
        public InputConfiguration(HashMap<Integer, Integer> bindings) {
            this.bindings = bindings;
        }
        
        public static InputConfiguration getDefault() {
            return KnownBindings.pong();
        }
        
        public Map<Integer, Integer> getBindings() {
            return Collections.unmodifiableMap(bindings);
        }

        public void bind(int keyCode, int deviceKey) {
            bindings.put(keyCode, deviceKey);
        }
        
        @Override
        public InputConfiguration cloned() {
            return new InputConfiguration((HashMap<Integer, Integer>) bindings.clone());
        }
    }
}
