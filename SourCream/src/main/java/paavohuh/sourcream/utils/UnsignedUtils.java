
package paavohuh.sourcream.utils;

import org.joou.UByte;
import org.joou.UShort;

/**
 * Contains utility functions for dealing with org.joou unsigned numeric types.
 */
public class UnsignedUtils {
    /**
     * Sums an unsigned byte and an integer.
     * @param a The byte.
     * @param b The integer.
     * @return The sum.
     */
    public static UByte add(UByte a, int b) {
        return UByte.valueOf(a.intValue() + b);
    }
    
    /**
     * Sums an integer and an unsigned byte.
     * @param a The byte.
     * @param b The integer.
     * @return The sum.
     */
    public static UByte add(int a, UByte b) {
        return UByte.valueOf(a + b.intValue());
    }
    
    /**
     * Sums an unsigned short and an integer.
     * @param a The byte.
     * @param b The integer.
     * @return The sum.
     */
    public static UShort add(UShort a, int b) {
        return UShort.valueOf(a.intValue() + b);
    }
    
    /**
     * Sums an integer and unsigned short.
     * @param a The byte.
     * @param b The integer.
     * @return The sum.
     */
    public static UShort add(int a, UShort b) {
        return UShort.valueOf(a + b.intValue());
    }
    
    /**
     * Converts an integer to an unsigned byte. The integer must be between
     * 0 and 255 (or 0xFF).
     * @param a The integer.
     * @return The converted byte.
     */
    public static UByte ubyte(int a) {
        return UByte.valueOf(a);
    }
    
    /**
     * Converts an integer to an unsigned short. The integer must be between
     * 0 and 65535 (or 0xFFFF).
     * @param a The integer.
     * @return The converted ushort.
     */
    public static UShort ushort(int a) {
        return UShort.valueOf(a);
    }
}
