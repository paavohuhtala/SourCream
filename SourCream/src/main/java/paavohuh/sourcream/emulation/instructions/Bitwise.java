package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;

import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

/**
 * Contains bitwise instructions.
 * This class shouldn't be instantiated.
 */
public final class Bitwise {
    
    private Bitwise() { }
    
    public static class And extends Instruction.WithTwoRegisters {        

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
    
    public static class ShiftLeft extends Instruction.WithTwoRegisters {

        public ShiftLeft(Register x, Register y) {
            super(x, y);
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
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8006);
        }

        @Override
        public State execute(State state) {
            int registerYValue = getRegisterY(state).intValue();
            
            int shifted = registerYValue << 1;
            
            // The most significant (first) bit of register y
            int msb = registerYValue & 0b10000000;
            
            return state
                .withRegister(registerX, UByte.valueOf(shifted & 0xFF))
                .withRegister(Register.VF, UByte.valueOf(msb));
        }
    }
    
    public static class ShiftRight extends Instruction.WithTwoRegisters {

        public ShiftRight(Register x, Register y) {
            super(x, y);
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
        protected UShort getBaseCode() {
            return UShort.valueOf(0x800E);
        }

        @Override
        public State execute(State state) {
            int registerYValue = getRegisterY(state).intValue();
            
            int shifted = registerYValue >>> 1;
            
            // The least significant (last) bit of register y
            int lsb = registerYValue & 0b00000001;
            
            return state
                .withRegister(registerX, UByte.valueOf(shifted & 0xFF))
                .withRegister(Register.VF, UByte.valueOf(lsb));
        }
    }
    
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(And::new),
            getAllInstances(Or::new),
            getAllInstances(Xor::new),
            getAllInstances(ShiftLeft::new),
            getAllInstances(ShiftRight::new));
    }
}
