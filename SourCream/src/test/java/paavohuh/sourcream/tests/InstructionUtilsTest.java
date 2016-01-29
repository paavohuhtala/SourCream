package paavohuh.sourcream.tests;

import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import paavohuh.sourcream.emulation.InstructionUtils;
import paavohuh.sourcream.emulation.Register;

public class InstructionUtilsTest {
    
    public InstructionUtilsTest() {
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
    public void registersAreSetProperly() {
        UShort base = UShort.valueOf(0x8000);
        
        UShort modified = InstructionUtils.setRegister(base, Register.V1, 2);
        assertEquals(UShort.valueOf(0x8100), modified);
        
        modified = InstructionUtils.setRegister(modified, Register.V1, 1);
        assertEquals(UShort.valueOf(0x8110), modified);
    }
}
