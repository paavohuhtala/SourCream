
package paavohuh.sourcream.ui;

import paavohuh.sourcream.configuration.EmulatorConfiguration;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.utils.MathUtils;

/**
 * An emulated LCD buffer, with simulated ghosting.
 */
class EmulatedLcdBuffer {
    private final EmulatorConfiguration config;
    float[][] buffer;
    final int width;
    final int height;

    /**
     * Creates a new emulated LCD buffer.
     * @param config The emulator configuration
     * @param buffer Initial ScreenBuffer.
     */
    public EmulatedLcdBuffer(EmulatorConfiguration config, ScreenBuffer buffer) {
        this.config = config;
        this.buffer = new float[buffer.height][buffer.width];
        this.width = buffer.width;
        this.height = buffer.height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.buffer[y][x] = buffer.get(x, y) ? 1.0f : 0.0f;
            }
        }
    }

    /**
     * Updates the LCD buffer with new values from the screen buffer.
     * The values are clamped between 0.0 and 1.0.
     * @param buffer
     */
    public void updateWith(ScreenBuffer buffer) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float addBy = config.screen.ghosting.addBy;
                float subtractBy = config.screen.ghosting.subtractBy;
                this.buffer[y][x] = MathUtils.clamp(0.0f, this.buffer[y][x] + (buffer.get(x, y) ? addBy : -subtractBy), 1.0f);
            }
        }
    }

}
