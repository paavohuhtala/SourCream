
package paavohuh.sourcream.tests.instructions;

import org.joou.UByte;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import paavohuh.sourcream.configuration.VMConfiguration;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.instructions.Bitwise;
import paavohuh.sourcream.emulation.instructions.Bitwise.*;

public class BitwiseTest {
    
    private State initialState;
    
    public BitwiseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        initialState = new State(VMConfiguration.getDefault());
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void andWithZero() {
        State testState =
            initialState
            .withRegister(Register.V0, UByte.valueOf(0xFF))
            .withRegister(Register.V1, UByte.valueOf(0x00));
        State afterState = new Bitwise.And(Register.V0, Register.V1).execute(testState);
        
        assertEquals(UByte.valueOf(0x00), afterState.getRegister(Register.V0));
        assertEquals(UByte.valueOf(0x00), afterState.getRegister(Register.V1));
    }
    
    @Test
    public void andAllArguments() {
        Instruction instr = new Bitwise.And(Register.V0, Register.V1);
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                UByte expected = UByte.valueOf(x & y);
                State testState =
                    initialState
                    .withRegister(Register.V0, UByte.valueOf(x))
                    .withRegister(Register.V1, UByte.valueOf(y));
                State afterState =  instr.execute(testState);
                assertEquals(expected, afterState.getRegister(Register.V0));
            }
        }
    }
    
    @Test
    /**
     * Evaluates and verifies ALL valid mutations of Bitwise.Or 
     */
    public void orAllArguments() {
        Instruction instr = new Bitwise.Or(Register.V0, Register.V1);
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                UByte expected = UByte.valueOf(x | y);
                State testState =
                    initialState
                    .withRegister(Register.V0, UByte.valueOf(x))
                    .withRegister(Register.V1, UByte.valueOf(y));
                State afterState =  instr.execute(testState);
                assertEquals(expected, afterState.getRegister(Register.V0));
            }
        }
    }
    
    @Test
    /**
     * Evaluates and verifies ALL valid mutations of Bitwise.Xor 
     */
    public void xorAllArguments() {
        Instruction instr = new Bitwise.Xor(Register.V0, Register.V1);
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                UByte expected = UByte.valueOf(x ^ y);
                State testState =
                    initialState
                    .withRegister(Register.V0, UByte.valueOf(x))
                    .withRegister(Register.V1, UByte.valueOf(y));
                State afterState =  instr.execute(testState);
                assertEquals(expected, afterState.getRegister(Register.V0));
            }
        }
    }
    
    @Test
    public void andAllInstances() {
        for (Instruction.WithTwoRegisters instr : Instruction.WithTwoRegisters.getAllInstances(And::new)) {
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0b1111))
                .withRegister(instr.registerY, UByte.valueOf(0b0101));
            State afterState = instr.execute(testState);
            assertEquals(UByte.valueOf(0b0101), afterState.getRegister(instr.registerX));
        }
    }
    
    @Test
    @Ignore
    public void orAllInstances() {
        for (Instruction.WithTwoRegisters instr : Instruction.WithTwoRegisters.getAllInstances(Or::new)) {
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0b1010))
                .withRegister(instr.registerY, UByte.valueOf(0b0101));
            State afterState = instr.execute(testState);
            assertEquals(UByte.valueOf(0b1111 | 0b0101), afterState.getRegister(instr.registerX));
        }
    }
    
    @Test
    @Ignore
    public void xorAllInstances() {
        for (Instruction.WithTwoRegisters instr : Instruction.WithTwoRegisters.getAllInstances(Xor::new)) {
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0b1100))
                .withRegister(instr.registerY, UByte.valueOf(0b0011));
            State afterState = instr.execute(testState);
            assertEquals(UByte.valueOf(0b1100 ^ 0b0011), afterState.getRegister(instr.registerX));
        }
    }
}
