
package paavohuh.sourcream.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.emulation.Instruction;
import static paavohuh.sourcream.emulation.InstructionFactory.*;
import paavohuh.sourcream.emulation.instructions.Bitwise;
import paavohuh.sourcream.emulation.instructions.Control;

public class InstructionFactoryTest {
    public InstructionFactoryTest() {
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
        for (Instruction instr : getAllInstances(Bitwise.And::new)) {
            int code = instr.getCode().intValue();
            Assert.assertTrue("Code is at least 0", code >= 0);
            Assert.assertTrue("Code is less than 0xFFFF", code < 0xFFFF);
        }
    }
    
    @Test
    public void oneRegister8BitConstantInstructionsHaveValidCodes() {
        for (Instruction instr: getAllInstances(Control.SkipIfEquals::new)) {
            int code = instr.getCode().intValue();
            Assert.assertTrue("Code is at least 0", code >= 0);
            Assert.assertTrue("Code is less than 0xFFFF", code < 0xFFFF);
        }
    }
}
