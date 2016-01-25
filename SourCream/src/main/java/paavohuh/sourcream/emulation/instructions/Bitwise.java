package paavohuh.sourcream.emulation.instructions;

import org.joou.UByte;
import org.joou.UShort;

import paavohuh.sourcream.emulation.InstructionCache;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.State;

public class Bitwise {
    public static class And extends Instruction.TwoRegister {
        public static final UShort CODE = UShort.valueOf(0x8002);
        
        public And(byte registerX, byte registerY) {
            super(registerX, registerY);
        }
        
        @Override
        public State execute(State state) {
            return state.withRegister(registerX, UByte.valueOf(state.register(registerX).intValue() & state.register(registerY).intValue()));
        }

        @Override
        protected UShort getBaseCode() { return CODE; }

        @Override
        protected int getRegXOffset() { return 2; }

        @Override
        protected int getRegYOffset() { return 1; }
    }
    
    public static class Or extends Instruction.TwoRegister {
        public static final UShort CODE = UShort.valueOf(0x8001);

        public Or(byte registerX, byte registerY) {
            super(registerX, registerY);
        }
        
        @Override
        public State execute(State state) {
            return state.withRegister(registerX, UByte.valueOf(state.register(registerX).intValue() | state.register(registerY).intValue()));
        }
        
        @Override
        protected UShort getBaseCode() { return CODE; }

        @Override
        protected int getRegXOffset() { return 2; }

        @Override
        protected int getRegYOffset() { return 1; }
    }
    
    public static void registerAll(InstructionCache decoder) {
        Instruction.TwoRegister.getAllInstances(And::new).forEach(decoder::register);
        Instruction.TwoRegister.getAllInstances(Or::new).forEach(decoder::register);
    }
}
