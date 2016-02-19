
package paavohuh.sourcream.utils;

import java.io.InputStream;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

public class ResourceUtils {
    public static Optional<String> getAsString(String name) {
        InputStream stream = ResourceUtils.class.getResourceAsStream(name);
        return OptionalUtils.ofThrowing(() -> IOUtils.toString(stream, "UTF-8"));
    }
    
    public static Optional<Byte[]> getAsBytes(String name) {
        InputStream stream = ResourceUtils.class.getResourceAsStream(name);
        return OptionalUtils.ofThrowing(() -> ArrayUtils.toBoxed(IOUtils.toByteArray(stream)));
    }
}
