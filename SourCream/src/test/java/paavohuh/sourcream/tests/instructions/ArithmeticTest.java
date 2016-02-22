package paavohuh.sourcream.tests.instructions;

import paavohuh.sourcream.tests.TestWithState;
import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.emulation.InstructionFactory;
import paavohuh.sourcream.emulation.Instruction.*;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.instructions.Arithmetic.*;

public class ArithmeticTest extends TestWithState {
    
    @Test
    public void addConstantToRegisterAllInstances() {
        Seq<AddConstantToRegister> instances =
            InstructionFactory.getAllInstances(AddConstantToRegister::new)
            .cast(AddConstantToRegister.class);
        
        for (WithRegisterAnd8BitConstant instr : instances) {
            UByte expected = UByte.valueOf((127 + instr.constant.intValue()) & 0xFF);
            
            State testState = initialState.withRegister(instr.register, UByte.valueOf(127));
            
            State afterState = instr.execute(testState);
            Assert.assertEquals(expected, afterState.getRegister(instr.register));
        }
    }
    
    @Test
    public void addRegisterToRegisterAllInstances() {
        Seq<AddRegisterToRegister> instances =
            InstructionFactory.getAllInstances(AddRegisterToRegister::new)
            .cast(AddRegisterToRegister.class);
        
        State testState = initialState;
        
        // Set registers 0..16 to powers of two
        for (int i = 0; i < 16; i++) {
            testState = testState.withRegister(new Register(i), UByte.valueOf(i * i));
        } 
        
        for (WithTwoRegisters instr : instances) {
            
            if (instr.registerX.equals(Register.CARRY)) {
                continue;
            }
            
            int sum =
                testState.getRegister(instr.registerX).intValue() + 
                testState.getRegister(instr.registerY).intValue();
            
            UByte expected = UByte.valueOf(sum & 0xFF);
            boolean isCarrySet = sum > 0xFF;
            
            State afterState = instr.execute(testState);
            
            Assert.assertEquals(expected, afterState.getRegister(instr.registerX));
            Assert.assertEquals(isCarrySet ? 1 : 0, afterState.getRegister(Register.CARRY).intValue());
        }
    }
}
