
package paavohuh.sourcream.tests;

import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.emulation.InstructionUtils;
import paavohuh.sourcream.emulation.Register;

public class RegisterTest {
        
    public RegisterTest() {
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
    public void canCreateValidRegisters() {
        try {
            for (int i = 0; i < 16; i++) {
                Register register = new Register(i);
                assertEquals(i, register.id);
            }
        } catch(IllegalArgumentException e) {
            Assert.fail("Couldn't create valid register.");
        }
    }

    @Test
    public void cantCreateNegativeRegister() {
        try {
            Register reg1 = new Register(-1);
            Assert.fail("Could create register with id < 0");
        } catch(IllegalArgumentException e) { }
    }

    @Test
    public void cantCreateRegistersOver15() {
        try {
            Register reg1 = new Register(16);
            Assert.fail("Could create register with id > 15");
        } catch(IllegalArgumentException e) { }
    }
    
    @Test
    public void equalityWorks() {
        Register regA = new Register(0);
        Register regB = new Register(0);
        Register regC = new Register(1);
        
        Assert.assertTrue("Registers with the same id are equal.", regA.equals(regB));
        Assert.assertTrue("Registers with the same id are equal, with register shorthand.", regA.equals(Register.V0));
        Assert.assertFalse("Registers with different ids are not equal.", regA.equals(regC));
        Assert.assertFalse("Registers with different ids are not equal, with register shorthand.", regA.equals(Register.V1));
    }
    
    @Test
    public void formattingWorks() {
        assertEquals("V0", Register.V0.toString());
        
        for (int i = 0; i < 15; i++) {
            String expected = String.format("V%01X", i);
            assertEquals(expected, new Register(i).toString());
        }
    }
    
    @Test
    public void hashCodeIsId() {
        Register reg = new Register(6);
        assertEquals(6, reg.id);
        assertEquals(6, reg.hashCode());
    }
}
