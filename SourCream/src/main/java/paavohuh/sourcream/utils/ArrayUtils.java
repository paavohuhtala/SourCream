package paavohuh.sourcream.utils;

// Due to Java's lackluster generics (no generic instantation, no support for
// value types as generic arguments), this class is bit of a mess. Not much can
// be done about it.

public class ArrayUtils {   
    public static byte[] clone(byte[] array) {
        byte[] clone = new byte[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return clone;
    }
    
    public static <T> T[] clone(T[] array) {
        Object[] clone = new Object[array.length];
        System.arraycopy(array, 0, clone, 0, array.length);
        return (T[]) clone;
    }
}
