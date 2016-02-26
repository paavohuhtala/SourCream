
package paavohuh.sourcream.tests.utils;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.utils.OptionalUtils;

public class OptionalUtilsTest {
    
    @Test
    public void ofThrowingTest() {
        Optional<String> test1 = OptionalUtils.ofThrowing(() -> "hello");
        Assert.assertEquals("hello", test1.get());
        
        Optional<String> test2 = OptionalUtils.ofThrowing(() -> {
            throw new Exception("test");
        });
        
        Assert.assertEquals(Optional.<String>empty(), test2);
    }
}
