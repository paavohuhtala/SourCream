
package paavohuh.sourcream.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static <TLeft, TRight>  Map<TRight, TLeft> invert(Map<TLeft, TRight> map) {
        Map<TRight, TLeft> inverted = new HashMap<>(map.size());
        
        for (Map.Entry<TLeft, TRight> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        
        return inverted;
    }
}
