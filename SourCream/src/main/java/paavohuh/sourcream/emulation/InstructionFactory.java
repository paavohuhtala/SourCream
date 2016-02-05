
package paavohuh.sourcream.emulation;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;

public class InstructionFactory {
    public static Seq<Instruction> getAllInstances(Instruction.WithRegister.Constructor ctor) {
        return Seq.range((byte)0, (byte)16).map(ctor::build);
    }
    
    public static Seq<Instruction> getAllInstances(Instruction.WithTwoRegisters.Constructor ctor) {
        return
            Seq.range((byte)0, (byte)16).crossJoin(Seq.range((byte)0, (byte)15)) 
            .map(t -> ctor.build(t.v1, t.v2));
    }
    
    public static Seq<Instruction> getAllInstances(Instruction.WithAddress.Constructor ctor) {
        return
            Seq.range(0, 4096)
            .map(UShort::valueOf)
            .map(ctor::build);
    }
    
    public static Seq<Instruction> getAllInstances(Instruction.With4BitConstant.Constructor ctor) {
        return
            Seq.range((byte)0, (byte)16)
            .map(UByte::valueOf)
            .map(ctor::build);
    }
}
