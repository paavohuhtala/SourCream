
package paavohuh.sourcream.emulation;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;

/**
 * Provides static utility methods for generating instances of instructions.
 */
public class InstructionFactory {
    
    /**
     * Gets all instances of an instruction with a register parameter.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.WithRegister.Constructor ctor) {
        return
            Seq.range(0, 16)
            .map(Register::new)
            .map(ctor::build);
    }
    
    /**
     * Gets all instances of an instruction with two register parameters.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.WithTwoRegisters.Constructor ctor) {
        return
            Seq.range(0, 16)
            .map(Register::new)
            .crossJoin(Seq.range(0, 16).map(Register::new))
            .map(t -> ctor.build(t.v1, t.v2));
    }
    
    /**
     * Gets all instances of an instruction with an address parameter.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.WithAddress.Constructor ctor) {
        return
            Seq.range(0, 4096)
            .map(UShort::valueOf)
            .map(ctor::build);
    }
    
    /**
     * Gets all instances of an instruction with a 4-bit constant parameter.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.With4BitConstant.Constructor ctor) {
        return
            Seq.range(0, 16)
            .map(UByte::valueOf)
            .map(ctor::build);
    }
    
    /**
     * Gets all instances of an instruction with a register parameter and an
     * 8-bit constant parameter.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.WithRegisterAnd8BitConstant.Constructor ctor) {
        return
            Seq.range(0, 16)
            .map(Register::new)
            .crossJoin(Seq.range(0, 256).map(UByte::valueOf))
            .map(t -> ctor.build(t.v1, t.v2));
    }
    
    /**
     * Gets all instances of an instruction with two register parameters and a
     * 4-bit constant parameter.
     * @param ctor The constructor of the instruction.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAllInstances(Instruction.WithTwoRegistersAnd4BitConstant.Constructor ctor) {
        return
            Seq.range(0, 16)
            .map(Register::new)
            .crossJoin(Seq.range(0, 16).map(Register::new))
            .crossJoin(Seq.range(0, 16).map(UByte::valueOf))
            .map(t -> ctor.build(t.v1.v1, t.v1.v2, t.v2));
    }
}
