
package paavohuh.sourcream.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    /**
     * Inverts a map, from key-value to value-key.
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     * @param map The map to invert.
     * @return The inverted map.
     */
    public static <K, V>  Map<V, K> invert(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>(map.size());
        
        for (Map.Entry<K, V> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        
        return inverted;
    }
}
