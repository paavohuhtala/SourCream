package paavohuh.sourcream.emulation.instructions;

import org.joou.UByte;
import org.joou.UShort;

import paavohuh.sourcream.emulation.ArrayInstructionCache;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.InstructionCache;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;

public class Bitwise {
    public static class And extends Instruction.WithTwoRegisters {        

        public And(byte registerX, byte registerY) {
            super(registerX, registerY);
        }
        
        public And(Register x, Register y) {
            super(x, y);
        }
        
        @Override
        public State execute(State state) {
            return
                state
                .withRegister(registerX, UByte.valueOf(state.getRegister(registerX).intValue() & state.getRegister(registerY).intValue()));
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8002);
        }

        @Override
        protected int getRegXOffset() {
            return 2;
        }

        @Override
        protected int getRegYOffset() {
            return 1;
        }
    }
    
    public static class Or extends Instruction.WithTwoRegisters {
        public Or(byte registerX, byte registerY) {
            super(registerX, registerY);
        }

        public Or(Register x, Register y) {
            super(x, y);
        }
        
        @Override
        public State execute(State state) {
            return
                state
                .withRegister(registerX, UByte.valueOf(state.getRegister(registerX).intValue() | state.getRegister(registerY).intValue()));
        }
        
        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8001);
        }

        @Override
        protected int getRegXOffset() {
            return 2;
        }

        @Override
        protected int getRegYOffset() {
            return 1;
        }
    }
    
    public static class Xor extends Instruction.WithTwoRegisters {
        public Xor(byte registerX, byte registerY) {
            super(registerX, registerY);
        }

        public Xor(Register x, Register y) {
            super(x, y);
        }
        
        @Override
        public State execute(State state) {
            return state.withRegister(registerX, UByte.valueOf(state.getRegister(registerX).intValue() ^ state.getRegister(registerY).intValue()));
        }
        
        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8003);
        }

        @Override
        protected int getRegXOffset() {
            return 2;
        }

        @Override
        protected int getRegYOffset() {
            return 1;
        }
    }
    
    public static void registerAll(InstructionCache cache) {
        Instruction.WithTwoRegisters.getAllInstances(And::new).forEach(cache::register);
        Instruction.WithTwoRegisters.getAllInstances(Or::new).forEach(cache::register);
        Instruction.WithTwoRegisters.getAllInstances(Xor::new).forEach(cache::register);
    }
}
