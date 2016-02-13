
package paavohuh.sourcream.tests.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.InstructionFactory;
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
        initialState = new State(DeviceConfiguration.getDefault());
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
                State afterState = instr.execute(testState);
                assertEquals(expected, afterState.getRegister(Register.V0));
            }
        }
    }
    
    @Test
    public void andAllInstances() {
        Seq<And> instances = InstructionFactory.getAllInstances(And::new).cast(And.class);
        
        for (Instruction.WithTwoRegisters instr : instances) {
            if (instr.registerX.equals(instr.registerY)) {
                continue;
            }
            
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0b01111))
                .withRegister(instr.registerY, UByte.valueOf(0b00101));
            State afterState = instr.execute(testState);
            assertEquals(UByte.valueOf(0b00101), afterState.getRegister(instr.registerX));
        }
    }
    
    @Test
    public void orAllInstances() {
        Seq<Or> instances = InstructionFactory.getAllInstances(Or::new).cast(Or.class);
        
        for (Instruction.WithTwoRegisters instr : instances) {
            if (instr.registerX.equals(instr.registerY)) {
                continue;
            }
            
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0xF0))
                .withRegister(instr.registerY, UByte.valueOf(0x0F));
            State afterState = instr.execute(testState);
            assertEquals(0xFF, afterState.getRegister(instr.registerX).intValue());
        }
    }

    @Test
    public void xorAllInstances() {
        Seq<Xor> instances = InstructionFactory.getAllInstances(Xor::new).cast(Xor.class);
        
        for (Instruction.WithTwoRegisters instr : instances) {
            if (instr.registerX.equals(instr.registerY)) {
                continue;
            }
            
            State testState =
                initialState
                .withRegister(instr.registerX, UByte.valueOf(0xF0))
                .withRegister(instr.registerY, UByte.valueOf(0x0F));
            State afterState = instr.execute(testState);
            assertEquals(0xF0 ^ 0x0F, afterState.getRegister(instr.registerX).intValue());
        }
    }
}
