package paavohuh.sourcream.emulation;

import paavohuh.sourcream.configuration.Configuration;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.utils.ArrayUtils;

/**
 * Represents an immutable 1-bit buffer.
 * Used for the main display buffer and sprite drawing.
 * All drawing is done in XOR-mode.
 */
public class ScreenBuffer {
    /**
     * The width of the buffer.
     */
    public final int width;
    
    /**
     * The height of the buffer.
     */
    public final int height;
    
    private final boolean[][] buffer;
    
    /**
     * Set to true if a bit is flipped from 1 to 0 during blit.
     */
    public final boolean flipped;
    
    /**
     * Set to true if the screen has been modified.
     */
    public final boolean modified;
    
    /**
     * Creates a new screen buffer for the supplied device configuration.
     * @param config The device configuration.
     */
    public ScreenBuffer(Configuration config) {
        DeviceConfiguration deviceConfig = config.getDeviceConfig();
        width = deviceConfig.getResolutionX();
        height = deviceConfig.getResolutionY();
        flipped = false;
        modified = false;
        
        buffer = new boolean[height][width];
    }
    
    /**
     * Creates a new screen buffer from a 2-dimensional boolean array.
     * @param buffer The internal boolean buffer of the screen buffer.
     * @param flipped Has a bit been flipped from 1 to 0 during during the
     * latest draw call?
     * @param modified Has the buffer been modified after latest render?
     */
    public ScreenBuffer(boolean[][] buffer, boolean flipped, boolean modified) {
        height = buffer.length;
        width = buffer[0].length;
        this.flipped = flipped;
        this.modified = modified;
        
        this.buffer = ArrayUtils.clone(buffer);
    }
    
    /**
     * Reads a sprite from the supplied block of memory.
     * One byte equals one row.
     * @param memory The buffer to read.
     */
    public ScreenBuffer(byte[] memory) {
        
        height = memory.length;
        width = 8;
        buffer = new boolean[memory.length][8];
        flipped = false;
        modified = false;
        
        // For each row
        for (int y = 0; y < memory.length; y++) {
            // For each column
            for (int i = 0; i < 8; i++) {
                buffer[y][7 - i] = ((memory[y] >> i) & 1) == 1;
            }
        }
    }
    
    /**
     * Returns an empty screen buffer, with the same dimensions as the previous
     * one.
     * @return An empty screen buffer
     */
    public ScreenBuffer cleared() {
        
        boolean anyWasSet = false;
        
        // We have to loop over all of the pixels to check if any of them is set
        findSetLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (buffer[y][x]) {
                    anyWasSet = true;
                    break findSetLoop;
                }
            }
        }
        
        boolean[][] cleared = new boolean[height][width];
        
        return new ScreenBuffer(cleared, anyWasSet, true);
    }
    
    /**
     * Returns a new screen buffer, with the supplied sprite XOR-drawn to it.
     * @param sprite The sprite.
     * @param x The X location where to draw the sprite.
     * @param y The Y location where to draw the sprite.
     * @return A new screen buffer.
     */
    public ScreenBuffer blit(ScreenBuffer sprite, int x, int y) {
        
        boolean[][] newBuffer = ArrayUtils.clone(buffer);
        boolean didFlip = false;
        
        // For each row in sprite
        for (int yi = 0; yi < sprite.height; yi++) {
            // For each column in sprite
            for (int xi = 0; xi < sprite.width; xi++) {
                // Calculate buffer coordinates. They will wrap around the screen.
                int bufferY = (y + yi) % height;
                int bufferX = (x + xi) % width;
                // Drawing is done in XOR mode.
                boolean spriteBit = sprite.buffer[yi][xi];
                boolean bufferBit = buffer[bufferY][bufferX];
                
                // If a pixel is cleared, set the cleared bit.
                if (!didFlip) {
                    didFlip = bufferBit && spriteBit;
                }
                
                newBuffer[bufferY][bufferX] = spriteBit != bufferBit;
            }
        }
        
        return new ScreenBuffer(newBuffer, didFlip, true);
    }
    
    /**
     * Gets a pixel from the screen buffer.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The value of the pixel.
     */
    public boolean get(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IllegalArgumentException("X was out of range");
        }
        
        if (y < 0 || y >= height) {
            throw new IllegalArgumentException("Y was out of range");
        }
        
        return buffer[y][x];
    }
    
    /**
     * Compares the buffers bit-by-bit for equality.
     * "Flipped" and "modified"- flags are ignored. 
     * @param other The other screenbuffer.
     * @return If the buffers are equal.
     */
    public boolean buffersEqual(ScreenBuffer other) {
        if (other.width != this.width || other.height != this.height) {
            return false;
        }
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (other.buffer[y][x] != buffer[y][x]) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
