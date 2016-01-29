package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;

/**
 * An instruction cache, impelemented as an array. Provides O(1) instruction 
 * lookup.
 */
public class ArrayInstructionCache implements InstructionCache {
    
    private final Instruction[] cache;
    
    public ArrayInstructionCache() {
        cache = new Instruction[UShort.MAX_VALUE + 1];
    }
    
    @Override
    public void register(Instruction instr) {
        int code = instr.getCode().intValue();
        
        verifyCodeBounds(code);
        
        if (cache[code] != null) {
            throw new IllegalArgumentException(String.format("An instruction with code %04X has already been registered.", code));
        }
        
        cache[code] = instr;
    }
    
    @Override
    public Optional<Instruction> decode(UShort code) {
        int intCode = code.intValue();
        
        verifyCodeBounds(intCode);
        
        return Optional.ofNullable(cache[code.intValue()]);
    }
    
    private void verifyCodeBounds(int code) {
        if (code < 0 ||  code >= cache.length) {
            throw new IllegalArgumentException(String.format("Code %d is out of range (%d is maximum)", code, cache.length - 1));
        }
    }
}
