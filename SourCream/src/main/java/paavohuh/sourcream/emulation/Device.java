
package paavohuh.sourcream.emulation;

import java.util.function.Consumer;
import org.jooq.lambda.Seq;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.instructions.*;

/**
 * The composition root of the emulator. 
 * Creates the CPU and the initial state and builds the instruction cache.
 */
public class Device {
    private final CPU cpu;
    
    public Device(DeviceConfiguration config, byte[] program) {
        State bootupState = new State(config);
        bootupState = bootupState.withCopiedMemory(program, bootupState.getProgramCounter());
        
        InstructionCache cache = new ArrayInstructionCache();
        registerAllInstructions(cache);
        
        cpu = new CPU(cache, bootupState);
    }
    
    public void start() {
        Thread thread = new Thread(cpu, "CPU Emulation Thread");
        thread.start();
    }
    
    /**
     * Registers a handler to be called when the screen is updated.
     * @param handler 
     */
    public void onUpdateGraphics(Consumer<ScreenBuffer> handler) {
        cpu.onUpdateGraphics(handler);
    }
    
    private Seq<Instruction> getAllInstructions() {
        return Seq.concat(
            Arithmetic.getAll(),
            Bitwise.getAll(),
            Control.getAll(),
            Graphics.getAll(),
            Transfer.getAll());
    }
    
    private void registerAllInstructions(InstructionCache cache) {
        getAllInstructions().forEach(cache::register);
    }
}
