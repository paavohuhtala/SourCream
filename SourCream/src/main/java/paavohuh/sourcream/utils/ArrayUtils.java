package paavohuh.sourcream.utils;

import java.util.Arrays;

/**
 * Contains utilities for manipulating arrays.
 */
public class ArrayUtils {   
    public static byte[] clone(byte[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    public static boolean[] clone(boolean[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    public static <T> T[] clone(T[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    public static boolean[][] clone(boolean[][] array) {
        boolean[][] clone = new boolean[array.length][];
        
        for (int y = 0; y < array.length; y++) {
            clone[y] = array[y].clone();
        }
        
        return clone;
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
