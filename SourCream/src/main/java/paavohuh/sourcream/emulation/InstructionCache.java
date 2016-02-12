package paavohuh.sourcream.emulation;


/**
 * An instruction cache is a instruction decoder with caching support.
 * Instruction can be added to the cache with the register method.
 */
public interface InstructionCache extends InstructionDecoder {

    void register(Instruction instr);
    
}
