
package paavohuh.sourcream.emulation;

import org.jooq.lambda.Seq;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.instructions.Bitwise;
import paavohuh.sourcream.emulation.instructions.Transfer;

/**
 * The composition root of the emulator. 
 * Creates the CPU and initial state and builds the instruction cache.
 */
public class Device {
    private final CPU cpu;

    public Device(DeviceConfiguration config) {
        State bootupState = new State(config);
        InstructionCache cache = new ArrayInstructionCache();
        registerAllInstructions(cache);
        
        this.cpu = new CPU(cache, bootupState);
    }
    
    public void start() {
        
    }
    
    private Seq<Instruction> getAllInstructions() {
        return Seq.concat(
            Bitwise.getAll(),
            Transfer.getAll());
    }
    
    private void registerAllInstructions(InstructionCache cache) {
        getAllInstructions().forEach(cache::register);
    }
}
