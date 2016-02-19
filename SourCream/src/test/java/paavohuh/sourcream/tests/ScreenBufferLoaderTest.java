
package paavohuh.sourcream.tests;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.emulation.ScreenBufferLoader;

public class ScreenBufferLoaderTest {
    @Test
    public void testBasicLoad() {
        // The number 0 as a bitmap
        String asciiBuffer = "11110000\n10010000\n10010000\n10010000\n11110000";
        ScreenBuffer expectedBuffer = new ScreenBuffer(new byte[]
            {(byte)0xF0, (byte)0x90, (byte)0x90, (byte)0x90, (byte)0xF0});
        
        Optional<ScreenBuffer> bufferOption = ScreenBufferLoader.tryLoad(asciiBuffer);
        
        if (!bufferOption.isPresent()) {
            Assert.fail("Loading a valid buffer failed");
        }
        
        ScreenBuffer buffer = bufferOption.get();
        Assert.assertTrue("Buffers should equal.", buffer.buffersEqual(expectedBuffer));
    }
}
