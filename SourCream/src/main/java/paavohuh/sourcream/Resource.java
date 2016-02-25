
package paavohuh.sourcream;

import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.emulation.ScreenBufferLoader;
import paavohuh.sourcream.utils.ArrayUtils;
import paavohuh.sourcream.utils.Cached;
import paavohuh.sourcream.utils.ResourceUtils;

/**
 * Contains methods for getting known built in resources (such as the logo or 
 * the system font) from the .jar.
 */
public class Resource {
    /**
     * The path to the SourCream logo.
     */
    public static final String LOGO = "/buffers/logo.buffer";
    private static final Cached<ScreenBuffer> CACHED_LOGO;
    
    /**
     * Gets the SourCream logo as a screen buffer.
     * @return The screen buffer.
     */
    public static ScreenBuffer getLogo() {
        return CACHED_LOGO.get();
    }
    
    /**
     * The path to the Chip-8 system font.
     */
    public static final String SYSTEM_FONT = "/systemfont.bin";
    private static final Cached<byte[]> CACHED_SYSTEM_FONT;
    
    /**
     * Gets the system font as a byte array.
     * @return 
     */
    public static byte[] getSystemFont() {
        return CACHED_SYSTEM_FONT.get();
    }
    
    /**
     * Initializes the cached resources.
     */
    static {
        CACHED_LOGO = new Cached<>(() ->
            ResourceUtils.getAsString(LOGO).flatMap(ScreenBufferLoader::tryLoad).get());
        
        CACHED_SYSTEM_FONT = new Cached<>(() ->
            ArrayUtils.toPrimitive(ResourceUtils.getAsBytes(SYSTEM_FONT).get()));
    }
}
