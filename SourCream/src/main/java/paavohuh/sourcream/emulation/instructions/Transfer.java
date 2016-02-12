package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;
import paavohuh.sourcream.emulation.Register;

/**
 * Contains transfer instructions.
 * This class shouldn't be instantiated.
 */
public class Transfer {
    
    /**
     * Sets the address register (VI) to a constant. 
     */
    public static class SetAddressRegister extends Instruction.WithAddress {

        public SetAddressRegister(UShort address) {
            super(address);
        }

        @Override
        public State execute(State state) {
            return state.withAddressRegister(address);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xA000);
        }
    }
    
    /**
     * Sets register VX to register VY.
     */
    public static class MoveRegister extends Instruction.WithTwoRegisters {

        public MoveRegister(Register x, Register y) {
            super(x, y);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8000);
        }

        @Override
        protected int getRegXOffset() {
            return 2;
        }

        @Override
        protected int getRegYOffset() {
            return 1;
        }

        @Override
        public State execute(State state) {
            return state.withRegister(registerX, state.getRegister(registerY));
        }    
    }
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(SetAddressRegister::new),
            getAllInstances(MoveRegister::new));
    }
}