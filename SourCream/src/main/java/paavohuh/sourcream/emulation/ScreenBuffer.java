package paavohuh.sourcream.emulation;

import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.utils.ArrayUtils;

/**
 * Represents an immutable 1-bit buffer.
 * Used for the main display buffer and sprite drawing.
 * All drawing is done in XOR-mode.
 */
public class ScreenBuffer {
    private final int width;
    private final int height;
    private final boolean[][] buffer;
    
    // Set to true if a bit is flipped from 1 to 0 during blit.
    private final boolean flipped;
    
    /**
     * Creates a new screen buffer for the supplied device configuration.
     * @param config 
     */
    public ScreenBuffer(DeviceConfiguration config) {
        width = config.resolutionX;
        height = config.resolutionY;
        flipped = false;
        
        buffer = new boolean[height][width];
    }
    
    public ScreenBuffer(boolean[][] buffer, boolean flipped) {
        height = buffer.length;
        width = buffer[0].length;
        this.flipped = flipped;
        
        this.buffer = ArrayUtils.clone(buffer);
    }
    
    /**
     * Reads a sprite from the supplied block of memory.
     * One byte equals one row.
     * @param memory 
     */
    public ScreenBuffer(byte[] memory) {
        
        height = memory.length;
        width = 8;
        buffer = new boolean[memory.length][8];
        flipped = false;
        
        // For each row
        for (int y = 0; y < memory.length; y++) {
            // For each column
            for (int i = 0; i < 8; i++) {
                buffer[y][i] = ((memory[y] >> i) & 1) == 1;
            }
        }
    }
    
    /**
     * Returns an empty screen buffer, with the same dimensions as the previous
     * one.
     * @return An empty screen buffer
     */
    public ScreenBuffer cleared() {
        boolean[][] cleared = new boolean[height][width];
        return new ScreenBuffer(cleared, false);
    }
    
    /**
     * Returns a new screen buffer, with the supplied sprite XOR-drawn to it.
     * @param sprite
     * @param x
     * @param y
     * @return A new screen buffer
     */
    public ScreenBuffer blit(ScreenBuffer sprite, int x, int y) {
        
        boolean[][] newBuffer = ArrayUtils.clone(buffer);
        boolean newFlipped = false;
        
        // For each row in sprite
        for (int yi = 0; yi < sprite.height; yi++) {
            // For each column in sprite
            for (int xi = 0; xi < sprite.width; xi++) {
                // Calculate buffer coordinates. They will wrap around the screen.
                int bufferY = (y + yi) % height;
                int bufferX = (x + xi) & width;
                // Drawing is done in XOR mode.
                boolean spriteBit = sprite.buffer[yi][xi];
                boolean bufferBit = buffer[bufferX][bufferY];
                
                // If a pixel is cleared, set the cleared bit.
                if (!newFlipped) {
                    newFlipped = bufferBit && spriteBit;
                }
                
                newBuffer[bufferY][bufferX] = spriteBit != bufferBit;
            }
        }
        
        return new ScreenBuffer(newBuffer, newFlipped);
    }
}
