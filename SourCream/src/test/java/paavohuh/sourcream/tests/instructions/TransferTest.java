
package paavohuh.sourcream.tests.instructions;

import org.joou.UByte;
import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;

import paavohuh.sourcream.emulation.*;
import paavohuh.sourcream.emulation.instructions.Transfer.*;
import paavohuh.sourcream.tests.StateTest;
import static paavohuh.sourcream.utils.UnsignedUtils.*;

public class TransferTest extends StateTest {
    
    @Test
    public void bcdConversionWorks() {
        assertBcdTest(0, "000");
        assertBcdTest(1, "001");
        assertBcdTest(15, "015");
        assertBcdTest(102, "102");
        assertBcdTest(255, "255");
    }
    
    private void assertBcdTest(int value, String expected) {
        Instruction instruction = new StoreBinaryCodedDecimal(Register.V0);
        State testState = initialState
            .withRegister(Register.V0, UByte.valueOf(value))
            .withAddressRegister(UShort.valueOf(0x300));
        
        State afterState = instruction.execute(testState);
        UShort address = afterState.getAddressRegister();
        
        String[] parts = {
            afterState.get8BitsAt(add(address, 0)).toString(),
            afterState.get8BitsAt(add(address, 1)).toString(),
            afterState.get8BitsAt(add(address, 2)).toString()
        };
        
        Assert.assertEquals(0, afterState.get8BitsAt(add(address, -1)).intValue());
        Assert.assertEquals(0, afterState.get8BitsAt(add(address, 3)).intValue());
        Assert.assertEquals(expected, String.join("", parts));
    }
}
