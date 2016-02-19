
package paavohuh.sourcream.emulation;

public class ProgramBuilder {
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
}
