
package paavohuh.sourcream.emulation;

import org.joou.UShort;

/**
 * Thrown when the CPU tries to execute an unknown instruction.
 */
public class UnknownInstructionException extends Exception {
    /**
     * Creates a new unknown instruction exception.
     * @param code The bytecode of the instruction.
     */
    public UnknownInstructionException(UShort code) {
        super(String.format("Tried to execute an unknown instruction: %04X", code.intValue()));
    }
}
