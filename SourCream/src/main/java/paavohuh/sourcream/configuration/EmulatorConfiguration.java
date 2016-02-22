
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
     * @param screen 
     * @param mapping 
     */
    public EmulatorConfiguration(ScreenConfiguration screen, InputConfiguration mapping) {
        this.screen = screen;
        this.input = mapping;
    }
    
    /**
     * Returns the default emulator configuration.
     * @return 
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

    /**
     * @return the screen
     */
    public ScreenConfiguration getScreen() {
        return screen;
    }

    /**
     * @param screen the screen to set
     */
    public void setScreen(ScreenConfiguration screen) {
        this.screen = screen;
    }

    /**
     * @return the input
     */
    public InputConfiguration getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
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
         * @param scaleFactor
         * @param colors
         * @param emulateGhosting
         * @param ghosting 
         */
        public ScreenConfiguration(int scaleFactor, ColorScheme colors, boolean emulateGhosting, GhostingConfiguration ghosting) {
            this.scaleFactor = scaleFactor;
            this.colors = colors;
            this.emulateGhosting = emulateGhosting;
            this.ghosting = ghosting;
        }
        
        /**
         * Returns the default screen configuration.
         * @return 
         */
        public static ScreenConfiguration getDefault() {
            return new ScreenConfiguration(10, ColorScheme.getDefault(), true, GhostingConfiguration.getDefault());
        }

        @Override
        public ScreenConfiguration cloned() {
            return new ScreenConfiguration(
                                getDisplayScale(),
                getColors().cloned(), isEmulateGhosting(), getGhosting().cloned());
        }

        /**
         * @return the scaleFactor
         */
        public int getDisplayScale() {
            return scaleFactor;
        }

        /**
         * @param scaleFactor the scaleFactor to set
         */
        public void setScaleFactor(int scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        /**
         * @return the colors
         */
        public ColorScheme getColors() {
            return colors;
        }

        /**
         * @param colors the colors to set
         */
        public void setColors(ColorScheme colors) {
            this.colors = colors;
        }

        /**
         * @return the emulateGhosting
         */
        public boolean isEmulateGhosting() {
            return emulateGhosting;
        }

        /**
         * @param emulateGhosting the emulateGhosting to set
         */
        public void setEmulateGhosting(boolean emulateGhosting) {
            this.emulateGhosting = emulateGhosting;
        }

        /**
         * @return the ghosting
         */
        public GhostingConfiguration getGhosting() {
            return ghosting;
        }

        /**
         * @param ghosting the ghosting to set
         */
        public void setGhosting(GhostingConfiguration ghosting) {
            this.ghosting = ghosting;
        }
        
        /**
         * Contains the colors for the emulated screen.
         */
        public static class ColorScheme implements DeepCloneable<ColorScheme>{
            private Color background;
            private Color foreground;

            public ColorScheme(Color background, Color foreground) {
                this.background = background;
                this.foreground = foreground;
            }
            
            public static ColorScheme getDefault() {
                return new ColorScheme(Color.green, Color.darkGray);
            }

            @Override
            public ColorScheme cloned() {
                return new ColorScheme(getBackground(), getForeground());
            }

            /**
             * @return the background
             */
            public Color getBackground() {
                return background;
            }

            /**
             * @param background the background to set
             */
            public void setBackground(Color background) {
                this.background = background;
            }

            /**
             * @return the foreground
             */
            public Color getForeground() {
                return foreground;
            }

            /**
             * @param foreground the foreground to set
             */
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
         * @return 
         */
        public static GhostingConfiguration getDefault() {
            return new GhostingConfiguration(0.50f, 0.015f);
        }

        @Override
        public GhostingConfiguration cloned() {
            return new GhostingConfiguration(getAddBy(), getSubtractBy());
        }

        /**
         * @return the addBy
         */
        public float getAddBy() {
            return addBy;
        }

        /**
         * @param addBy the addBy to set
         */
        public void setAddBy(float addBy) {
            this.addBy = addBy;
        }

        /**
         * @return the subtractBy
         */
        public float getSubtractBy() {
            return subtractBy;
        }

        /**
         * @param subtractBy the subtractBy to set
         */
        public void setSubtractBy(float subtractBy) {
            this.subtractBy = subtractBy;
        }
    }
    
    public static class InputConfiguration implements DeepCloneable<InputConfiguration> {
        private final HashMap<Integer, Integer> bindings;

        public InputConfiguration(HashMap<Integer, Integer> bindings) {
            this.bindings = bindings;
        }
        
        public static InputConfiguration getDefault() {
            HashMap<Integer, Integer> bindings = new HashMap<>(16);
            
            // pong:
            // player 1 up: 1
            // player 1 down: 4
            // player 2 up: 12
            // player 2 down: 13
            
            /*mapping.put(KeyEvent.VK_Q, 0);
            mapping.put(KeyEvent.VK_W, 1);
            mapping.put(KeyEvent.VK_E, 2);
            mapping.put(KeyEvent.VK_R, 3);
            mapping.put(KeyEvent.VK_T, 4);
            mapping.put(KeyEvent.VK_Y, 5);
            mapping.put(KeyEvent.VK_U, 6);
            mapping.put(KeyEvent.VK_I, 7);
            mapping.put(KeyEvent.VK_O, 8);
            mapping.put(KeyEvent.VK_P, 9);
            mapping.put(KeyEvent.VK_A, 10);
            mapping.put(KeyEvent.VK_S, 11);
            mapping.put(KeyEvent.VK_D, 12);
            mapping.put(KeyEvent.VK_F, 13);
            mapping.put(KeyEvent.VK_G, 14);
            mapping.put(KeyEvent.VK_H, 15);*/
            
            bindings.put(KeyEvent.VK_W, 1);
            bindings.put(KeyEvent.VK_S, 4);
            
            bindings.put(KeyEvent.VK_UP, 12);
            bindings.put(KeyEvent.VK_DOWN, 13);
            
            return new InputConfiguration(bindings);
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
