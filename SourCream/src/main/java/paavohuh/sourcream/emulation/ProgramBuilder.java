
package paavohuh.sourcream.emulation;

import org.joou.UShort;
import paavohuh.sourcream.emulation.instructions.Control;

/**
 * Contains functions for assembling bytecode programs from Chip-8 instructions.
 */
public class ProgramBuilder {
    /**
     * Builds a new program from a list of instructions.
     * @param instructions Variable number of instructions.
     * @return A byte array containing the code for the program.
     */
    public static byte[] assemble(Instruction... instructions) {
        byte[] byteCode = new byte[instructions.length * 2];
        int i = 0;
        
        for (Instruction instr : instructions) {
            int code = instr.getCode().intValue();
            byteCode[i++] = (byte) ((code >>> 8) & 0xFF);
            byteCode[i++] = (byte) (code & 0xFF);
        }
        
        return byteCode;
    }
    
    /**
     * Returns an idle loop, which only jumps onto itself.
     * @return A byte array containing the program.
     */
    public static byte[] getIdleLoop() {
        return assemble(new Control.JumpTo(UShort.valueOf(0x200)));
    }
}
