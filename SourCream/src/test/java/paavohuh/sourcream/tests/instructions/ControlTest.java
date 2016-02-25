package paavohuh.sourcream.tests.instructions;

import org.joou.UByte;
import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.*;
import paavohuh.sourcream.emulation.instructions.Arithmetic.*;
import paavohuh.sourcream.emulation.instructions.Control.*;
import paavohuh.sourcream.tests.StateTest;

public class ControlTest extends StateTest {
    
    @Test
    public void skipIfEqualsHasValidCode() {
        Instruction instr = new SkipIfEquals(Register.VA, UByte.valueOf(0xFF));
        Assert.assertEquals(UShort.valueOf(0x3AFF), instr.getCode());
    }
    
    @Test
    public void jumpWorks() throws UnknownInstructionException {
        byte[] program = ProgramBuilder.assemble(
            new AddConstantToRegister(Register.V0, UByte.valueOf(1)),
            new JumpTo(UShort.valueOf(0x200)));
        
        Device device = new Device(DeviceConfiguration.getDefault());
        device.setState(device.getState().withProgram(program).asRunning());
        
        device.runCycle();
        Assert.assertEquals(0x202, device.getState().getProgramCounter().intValue());
        device.runCycle();
        Assert.assertEquals(0x200, device.getState().getProgramCounter().intValue());
        Assert.assertEquals(1, device.getState().getRegister(Register.V0).intValue());
        device.runCycle();
        Assert.assertEquals(2, device.getState().getRegister(Register.V0).intValue());
    }
    
    @Test
    public void callWorks() throws UnknownInstructionException {
        byte[] program = ProgramBuilder.assemble(
            new AddConstantToRegister(Register.V0, UByte.valueOf(1)), // 200
            new CallSubroutine(UShort.valueOf(0x300)), // 202
            new AddConstantToRegister(Register.V0, UByte.valueOf(1))); // 204
        
        byte[] subroutine = ProgramBuilder.assemble(
            new AddConstantToRegister(Register.V0, UByte.valueOf(2)),
            new Return());
        
        Device device = new Device(DeviceConfiguration.getDefault());
        device.setState(device.getState()
            .withProgram(program)
            .withCopiedMemory(subroutine, UShort.valueOf(0x300))
            .asRunning());
        
        device.runCycle();
        Assert.assertEquals(1, device.getState().getRegister(Register.V0).intValue());
        device.runCycle();
        Assert.assertEquals(0x300, device.getState().getProgramCounter().intValue());
        Assert.assertEquals(1, device.getState().getStackPointer().intValue());
        device.runCycle();
        Assert.assertEquals(3, device.getState().getRegister(Register.V0).intValue());
        device.runCycle();
        Assert.assertEquals(0x204, device.getState().getProgramCounter().intValue());
        device.runCycle();
        Assert.assertEquals(4, device.getState().getRegister(Register.V0).intValue());
    }
}
