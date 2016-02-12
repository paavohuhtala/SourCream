package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;

/**
 * The virtual CPU of the device. Contains the machine state, and handles the
 * fetch-decode-execute -loop.
 */
public class CPU {
    private State state;
    private InstructionDecoder decoder;
    
    public CPU(InstructionDecoder decoder, State state) {
        this.state = state;
    }
    
    /**
     * Runs the CPU for one cycle.
     * 1. Fetches an instruction
     * 2. Decodes the instruction
     * 3. Replaces current state with modified state
     * @throws paavohuh.sourcream.emulation.UnknownInstructionException
     */
    public void runCycle() throws UnknownInstructionException {
        UShort code = state.get16BitsAt(state.getProgramCounter());
        Optional<Instruction> decoded = decoder.decode(code);
        
        if (!decoded.isPresent()) {
            throw new UnknownInstructionException(code);
        }
        
        Instruction instuction = decoded.get();
        State newState = instuction.execute(state.withIncrementedPc());
        
        this.state = newState;
    }
}
