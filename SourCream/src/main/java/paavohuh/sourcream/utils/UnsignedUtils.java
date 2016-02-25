
package paavohuh.sourcream.utils;

import org.joou.UByte;
import org.joou.UShort;

/**
 * Contains utility functions for dealing with org.joou unsigned numeric types.
 */
public class UnsignedUtils {
    public static UByte add(UByte a, int b) {
        return UByte.valueOf(a.intValue() + b);
    }
    
    public static UByte add(int a, UByte b) {
        return UByte.valueOf(a + b.intValue());
    }
    
    public static UShort add(UShort a, int b) {
        return UShort.valueOf(a.intValue() + b);
    }
    
    public static UShort add(int a, UShort b) {
        return UShort.valueOf(a + b.intValue());
    }
    
    public static UByte ubyte(int a) {
        return UByte.valueOf(a);
    }
    
    public static UShort ushort(int a) {
        return UShort.valueOf(a);
    }
}
