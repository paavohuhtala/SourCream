
package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import paavohuh.sourcream.emulation.Instruction;

public class AllInstructions {
    /**
     * Returns all instances of all instructions supported by the CPU.
     * @return A sequence of instructions
     */
    public static Seq<Instruction> get() {
        return Seq.concat(
            Arithmetic.getAll(),
            Bitwise.getAll(),
            Control.getAll(),
            Graphics.getAll(),
            Transfer.getAll(),
            Input.getAll());
    }
}
