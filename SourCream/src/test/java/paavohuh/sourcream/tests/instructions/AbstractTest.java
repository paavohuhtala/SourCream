
package paavohuh.sourcream.tests.instructions;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.instructions.Bitwise;

public class AbstractTest {
    public AbstractTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void twoRegisterInstructionsHaveValidCodes() {
        for (Instruction instr : Instruction.WithTwoRegisters.getAllInstances(Bitwise.And::new)) {
            int code = instr.getCode().intValue();
            Assert.assertTrue("Code is at least 0", code >= 0);
            Assert.assertTrue("Code is less than 0xFFFF", code < 0xFFFF);
        }
    }
}
