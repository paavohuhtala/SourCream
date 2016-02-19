package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;

/**
 * Decodes a 16-bit integer into a instruction.
 */
public interface InstructionDecoder {
    /**
     * Tries to decode an instruction from an unsigned 16-bit bytecode.
     * @param code The code
     * @return The instruction if succesful
     */
    Optional<Instruction> decode(UShort code);
}
