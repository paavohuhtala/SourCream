
package paavohuh.sourcream.emulation;

import java.util.Optional;

/**
 * Provides the ability to load a screen buffer from a string.
 */
public class ScreenBufferLoader {
    private static final char ZERO = '0';
    private static final char ONE = '1';
    private static final char DELIMITER = '\n';
    
    /**
     * Attempts to load a screen buffer from a string. Returns a None if it
     * fails.
     * @param str A string describing the screen buffer. Consists of ones,
     * zeroes, and newlines.
     * @return A screen buffer, if the load succeeds.
     */
    public static Optional<ScreenBuffer> tryLoad(String str) {
        String[] rows =
            str.replace("\r", "")
                .split(Character.toString(DELIMITER));
        
        boolean[][] buffer = new boolean[rows.length][rows[0].length()];
        
        int rowLength = rows[0].length();
        
        for (int y = 0; y < rows.length; y++) {
            if (rows[y].length() != rowLength) return Optional.empty();
            
            rowLength = rows[y].length();
            
            for (int x = 0; x < rowLength; x++) {
                char c = rows[y].charAt(x);
                
                if (c == ZERO) {
                    buffer[y][x] = false;
                } else if (c == ONE) {
                    buffer[y][x] = true;
                } else {
                    return Optional.empty();
                }
            }
        }
        
        return Optional.of(new ScreenBuffer(buffer, false, false));
    }
    
    /**
     * This is a utility class - it shouldn't be instantianed.
     */
    private ScreenBufferLoader () { }
}
