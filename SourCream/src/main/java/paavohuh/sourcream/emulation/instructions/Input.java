
package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

public class Input {
    
    public static class SkipIfKeyPressed extends Instruction.WithRegister {

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
    
    public static class SkipIfKeyNotPressed extends Instruction.WithRegister {

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
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
                getAllInstances(SkipIfKeyPressed::new),
                getAllInstances(SkipIfKeyNotPressed::new));
    }
}
