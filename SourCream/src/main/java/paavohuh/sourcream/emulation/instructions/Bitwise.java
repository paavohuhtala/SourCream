package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;

import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

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
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(And::new),
            getAllInstances(Or::new),
            getAllInstances(Xor::new));
    }
}
