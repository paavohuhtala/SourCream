package paavohuh.sourcream.utils;

import java.util.Arrays;

/**
 * Contains utilities for manipulating and cloning arrays.
 */
public class ArrayUtils {
    
    /**
     * Clones a byte array.
     * @param array The array to clone.
     * @return A clone of the array.
     */
    public static byte[] clone(byte[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    /**
     * Clones a boolean array.
     * @param array The array to clone.
     * @return A clone of the array.
     */
    public static boolean[] clone(boolean[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    /**
     * Clones a generic array.
     * @param <T> The type of the array elements.
     * @param array The array to clone.
     * @return A clone of the array.
     */
    public static <T> T[] clone(T[] array) {
        return Arrays.copyOf(array, array.length);
    }
    
    /**
     * Clones a 2-dimensional boolean array.
     * @param array The array to clone.
     * @return A clone of the array.
     */
    public static boolean[][] clone(boolean[][] array) {
        boolean[][] clone = new boolean[array.length][];
        
        for (int y = 0; y < array.length; y++) {
            clone[y] = array[y].clone();
        }
        
        return clone;
    }
        
    /**
     * Unboxes an array of bytes.
     * @param bytes The array to unbox.
     * @return An array of primitives.
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
     * @param bytes The array to box.
     * @return An array of boxed primitives.
     */
    public static Byte[] toBoxed(byte[] bytes) {
        Byte[] array = new Byte[bytes.length];
        
        for (int i = 0; i < bytes.length; i++) {
            array[i] = bytes[i];
        }
        
        return array;
    }
}
