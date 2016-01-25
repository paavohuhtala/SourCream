package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;
import paavohuh.sourcream.emulation.instructions.Bitwise;

public class InstructionCache {
    
    private final Instruction[] cache;
    
    public InstructionCache() {
        cache = new Instruction[Short.MAX_VALUE];
        Bitwise.registerAll(this);
    }
    
    public void register(Instruction instr) {
        cache[instr.getCode().intValue()] = instr;
    }
    
    public Optional<Instruction> decode(UShort code) {
        return Optional.ofNullable(cache[code.intValue()]);
    }
}
