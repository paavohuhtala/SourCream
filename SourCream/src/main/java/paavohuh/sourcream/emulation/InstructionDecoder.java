package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;

/**
 * Decodes a 16-bit integer into a instruction.
 */
public interface InstructionDecoder {
    Optional<Instruction> decode(UShort code);
}
