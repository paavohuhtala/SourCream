
package paavohuh.sourcream.tests;

import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.Device;
import paavohuh.sourcream.emulation.ProgramBuilder;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.UnknownInstructionException;
import paavohuh.sourcream.emulation.instructions.Control;
import paavohuh.sourcream.emulation.instructions.Graphics;

public class DeviceTest {
    private final Device cpu;

    public DeviceTest() {
        State initialState = new State(DeviceConfiguration.getDefault());
        this.cpu = new Device(initialState);
    }
    
    @Test
    public void canRunCycle() {
        byte[] program = ProgramBuilder.assemble(new Control.JumpTo(UShort.valueOf(0x300)));
        cpu.setState(cpu.getState().withProgram(program));
        
        try {
            cpu.runCycle();
            Assert.assertEquals(0x300, cpu.getState().getProgramCounter().intValue());
        } catch (UnknownInstructionException ex) {
            Assert.fail("Threw unknown instruction.");
        }
    }
    
    @Test
    public void canRun() throws InterruptedException {
        byte[] loop = ProgramBuilder.assemble(new Control.JumpTo(UShort.valueOf(0x200)));
        cpu.setState(cpu.getState().withProgram(loop));
        
        cpu.start();
        Thread.sleep(100);
        cpu.stop();
        
        Assert.assertEquals(0x200, cpu.getState().getProgramCounter().intValue());
   }
    
   @Test
   public void invokesGraphicsCallback() throws UnknownInstructionException {
       Rc<Boolean> didInvoke = new Rc<>(false);
       cpu.onUpdateGraphics(buffer -> {didInvoke.setValue(true);});
       
       byte[] program = ProgramBuilder.assemble(new Graphics.ClearScreen());
       cpu.setState(cpu.getState().withProgram(program));
       cpu.runCycle();
       
       Assert.assertTrue("Callback should be invoked on screen clear.", didInvoke.getValue());
   }
   
   private class Rc<T> {
        private T value;
        private final Boolean lock;

        public Rc(T value) {
            this.value = value;
            this.lock = false;
        }

        public T getValue() {
            synchronized(lock) {
                return value;
            }
        }

        public void setValue(T value) {
            synchronized(lock) {
                this.value = value;
            }
        }
   }
}
