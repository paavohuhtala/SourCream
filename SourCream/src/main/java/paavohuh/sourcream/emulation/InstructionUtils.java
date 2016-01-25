package paavohuh.sourcream.emulation;

import org.joou.UShort;

public class InstructionUtils {
    /**
     * Sets a register byte at the given offset.
     * @param base The opcode to modify
     * @param reg The register id to replace
     * @param regOffset The 0-based offset of the register byte, counted from right.
     * @return 
     */
    public static UShort setRegister(UShort base, Register reg, int regOffset) {
        return UShort.valueOf(base.intValue() | (reg.id << (regOffset * 4)));
    }
}
