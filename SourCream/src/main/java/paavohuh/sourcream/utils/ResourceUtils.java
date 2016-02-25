
package paavohuh.sourcream.utils;

import java.io.InputStream;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

/**
 * Contains utility methods for working with JAR resources.
 */
public class ResourceUtils {
    /**
     * Gets a JAR resource as a string.
     * @param name The path to the resource.
     * @return Optional containing the resource if the file exists and is valid
     * UTF-8.
     */
    public static Optional<String> getAsString(String name) {
        InputStream stream = ResourceUtils.class.getResourceAsStream(name);
        return OptionalUtils.ofThrowing(() -> IOUtils.toString(stream, "UTF-8"));
    }
    
    /**
     * Gets a JAR resource as a byte array.
     * @param name The path to the resource.
     * @return Optional containing the resource if the file exists.
     */
    public static Optional<byte[]> getAsBytes(String name) {
        InputStream stream = ResourceUtils.class.getResourceAsStream(name);
        return OptionalUtils.ofThrowing(() -> IOUtils.toByteArray(stream));
    }
}
