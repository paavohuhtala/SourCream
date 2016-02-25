
package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

/**
 * Contains instructions related to input.
 */
public final class Input {
    
    /**
     * Skips the next instruction if the key with the id VX is pressed.
     */
    public static class SkipIfKeyPressed extends Instruction.WithRegister {

        /**
         * Skips the next instruction if the key with the id VX is pressed.
         * @param register Contains the key id.
         */
        public SkipIfKeyPressed(Register register) {
            super(register);
        }
        
        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xE09E);
        }

        @Override
        public State execute(State state) {
            int key = getRegister(state).intValue();
            
            if (state.getInputState().getKey(key)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if the key with the id VX is up.
     */
    public static class SkipIfKeyNotPressed extends Instruction.WithRegister {

        /**
         * Skips the next instruction if the key with the id VX is up.
         * @param register Contains the key id.
         */
        public SkipIfKeyNotPressed(Register register) {
            super(register);
        }
        
        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xE0A1);
        }

        @Override
        public State execute(State state) {
            int key = getRegister(state).intValue();
            
            if (!state.getInputState().getKey(key)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Gets all input instructions.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAll() {
        return Seq.concat(
                getAllInstances(SkipIfKeyPressed::new),
                getAllInstances(SkipIfKeyNotPressed::new));
    }
}
