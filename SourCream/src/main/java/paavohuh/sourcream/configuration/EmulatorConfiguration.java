
package paavohuh.sourcream.configuration;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import paavohuh.sourcream.utils.DeepCloneable;
import paavohuh.sourcream.utils.MapUtils;

/**
 * Configuration for the emulator. These settings have no effect on emulation;
 * they're only used user preferences.
 */
public class EmulatorConfiguration implements DeepCloneable<EmulatorConfiguration> {

    private ScreenConfiguration screen;
    private InputConfiguration input;
    private SoundConfiguration sound;

    /**
     * Creates a new emulator config.
     * @param screen The screen configuration.
     * @param input The input configuration.
     * @param sound The sound configuration.
     */
    public EmulatorConfiguration(ScreenConfiguration screen, InputConfiguration input, SoundConfiguration sound) {
        this.screen = screen;
        this.input = input;
        this.sound = sound;
    }
    
    /**
     * Returns the default emulator configuration.
     * @return The configuration.
     */
    public static EmulatorConfiguration getDefault() {
        return new EmulatorConfiguration(
            ScreenConfiguration.getDefault(),
            InputConfiguration.getDefault(),
            SoundConfiguration.getDefault());
    }

    @Override
    public EmulatorConfiguration cloned() {
        return new EmulatorConfiguration(
            getScreen().cloned(),
            getInput().cloned(),
            getSound().cloned());
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

    public SoundConfiguration getSound() {
        return sound;
    }

    public void setSound(SoundConfiguration sound) {
        this.sound = sound;
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
                getEmulateGhosting(),
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

        public boolean getEmulateGhosting() {
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

            /**
             * Creates a new colo scheme.
             * @param background The background color.
             * @param foreground The foreground color.
             */
            public ColorScheme(Color background, Color foreground) {
                this.background = background;
                this.foreground = foreground;
            }
            
            /**
             * Returns the default color scheme, consisting of a green
             * background color and a dark gray foreground color.
             * @return The default color scheme.
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
         * @param addBy The value added to LCD buffer when blending between 0
         * and 1.
         * @param subtractBy The value subtracted from LCD buffer when blending
         * between 1 and 0.
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
         * Creates new input configuration.
         * @param bindings The key bindings.
         */
        public InputConfiguration(HashMap<Integer, Integer> bindings) {
            this.bindings = bindings;
        }
        
        /**
         * Gets the default configuration.
         * @return The default configuration.
         */
        public static InputConfiguration getDefault() {
            return KnownBindings.pong();
        }
        
        /**
         * Gets the key bindings from key codes to device keys.
         * @return The key bindings.
         */
        public Map<Integer, Integer> getBindings() {
            return Collections.unmodifiableMap(bindings);
        }

        /**
         * Adds a new key binding to the binding map.
         * @param keyCode The Swing virtual keycode.
         * @param deviceKey The Chip-8 key code (between 0 and 15).
         */
        public void bind(int keyCode, int deviceKey) {
            bindings.put(keyCode, deviceKey);
        }
        
        /**
         * Removes a key binding from the binding map.
         * @param keyCode The Swing virtual keycode to remove.
         */
        public void unbind(int keyCode) {
            bindings.remove(keyCode);
        }
        
        /**
         * Unbinds all keys.
         */
        public void unbindAll() {
            bindings.clear();
        }
        
        /**
         * Removes a key binding from the binding map via the device key.
         * @param deviceKey The Chip-8 key code (between 0 and 15).
         */
        public void unbindDeviceKey(int deviceKey) {
            Map<Integer, Integer> inverted = MapUtils.invert(bindings);
            for (Map.Entry<Integer, Integer> entry : inverted.entrySet()) {
                if (entry.getValue() == deviceKey) {
                    bindings.remove(entry.getKey());
                }
            }
        }
        
        @Override
        public InputConfiguration cloned() {
            return new InputConfiguration((HashMap<Integer, Integer>) bindings.clone());
        }
    }
    
    /**
     * Contains sound settings.
     */
    public static class SoundConfiguration implements DeepCloneable<SoundConfiguration> {

        private boolean enabled;
        
        @Override
        public SoundConfiguration cloned() {
            return new SoundConfiguration(enabled);
        }

        /**
         * Creates a new sound configuration.
         * @param enabled Sound sound be enabled?
         */
        public SoundConfiguration(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        /**
         * Gets the default sound configuration. Sounds are played by default.
         * @return The default configuration.
         */
        public static SoundConfiguration getDefault() {
            return new SoundConfiguration(true);
        }
    }
}
