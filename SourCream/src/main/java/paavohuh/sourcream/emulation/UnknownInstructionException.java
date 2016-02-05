
package paavohuh.sourcream.emulation;

import org.joou.UShort;

class UnknownInstructionException extends Exception {
    public UnknownInstructionException(UShort code) {
        super(String.format("Tried to execute an unknown instruction: %04X", code.intValue()));
    }
}
