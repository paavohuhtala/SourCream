
package paavohuh.sourcream.tests;

import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.ArrayInstructionCache;
import paavohuh.sourcream.emulation.CPU;
import paavohuh.sourcream.emulation.ProgramBuilder;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.UnknownInstructionException;
import paavohuh.sourcream.emulation.instructions.AllInstructions;
import paavohuh.sourcream.emulation.instructions.Control;
import paavohuh.sourcream.emulation.instructions.Graphics;

public class CPUTest {
    private final CPU cpu;

    public CPUTest() {
        ArrayInstructionCache cache;
        cache = new ArrayInstructionCache();
        AllInstructions.get().forEach(cache::register);

        State initialState = new State(DeviceConfiguration.getDefault());
        
        this.cpu = new CPU(cache, initialState);
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
        
        Thread thread = new Thread(cpu);
        thread.start();
        
        Thread.sleep(100);
        cpu.stop();
        thread.join(1000);
        
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

        public Rc(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
   }
}
