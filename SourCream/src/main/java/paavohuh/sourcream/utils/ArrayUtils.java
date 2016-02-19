package paavohuh.sourcream.utils;

import org.joou.UByte;
import org.joou.UShort;

// Due to Java's lackluster generics (no generic instantation, no support for
// value types as generic arguments), this class is bit of a mess. Not much can
// be done about it.

/**
 * Contains utilities for manipulating arrays.
 */
public class ArrayUtils {   
    public static byte[] clone(byte[] array) {
        byte[] clone = new byte[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }
    
    public static UByte[] clone(UByte[] array) {
        UByte[] clone = new UByte[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }
    
    public static UShort[] clone(UShort[] array) {
        UShort[] clone = new UShort[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }
    
    public static boolean[][] clone(boolean[][] array) {
        boolean[][] clone = new boolean[array.length][];
        
        for (int y = 0; y < array.length; y++) {
            clone[y] = array[y].clone();
        }
        
        return clone;
    }
    
    public static <T> T[] clone(T[] array) {
        Object[] clone = new Object[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return (T[]) clone;
    }
    
    /**
     * Unboxes an array of bytes.
     * @param bytes
     * @return 
     */
    public static byte[] toPrimitive(Byte[] bytes) {
        byte[] array = new byte[bytes.length];
        
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i];
        }
        
        return array;
    }
    
    /**
     * Boxes an array of bytes.
     * @param bytes The bytes to box.
     * @return Boxed bytes.
     */
    public static Byte[] toBoxed(byte[] bytes) {
        Byte[] array = new Byte[bytes.length];
        
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i];
        }
        
        return array;
    }
}
