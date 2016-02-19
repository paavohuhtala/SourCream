
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
    
    /**
     * Creates a new device.
     * @param config
     * @param program 
     */
    public Device(DeviceConfiguration config, byte[] program) {
        State bootupState = new State(config);
        bootupState = bootupState.withCopiedMemory(program, bootupState.getProgramCounter());
        
        InstructionCache cache = new ArrayInstructionCache();
        registerAllInstructions(cache);
        
        cpu = new CPU(cache, bootupState);
    }
    
    /**
     * Creates a new CPU thread, and starts executing it.
     * @return A new CPU thread.
     */
    public Thread start() {
        Thread thread = new Thread(cpu, "CPU Emulation Thread");
        thread.start();
        
        return thread;
    }
    
    /**
     * Signals the CPU that it should stop executing instructions.
     * This will stop the CPU thread after the current cycle is finished.
     */
    public void stop() {
        cpu.stop();
    }
    
    /**
     * Registers a handler to be called when the screen is updated.
     * @param handler 
     */
    public void onUpdateGraphics(Consumer<ScreenBuffer> handler) {
        cpu.onUpdateGraphics(handler);
    }

    private void registerAllInstructions(InstructionCache cache) {
        AllInstructions.get().forEach(cache::register);
    }
}
