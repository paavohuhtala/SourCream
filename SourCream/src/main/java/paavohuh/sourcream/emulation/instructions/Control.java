
package paavohuh.sourcream.emulation.instructions;

import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;

/**
 * Contains control instructions.
 * This class shouldn't be instantiated.
 */
public final class Control {
    private Control() { }
    
    /**
     * Jumps to address + V0.
     */
    public class JumpTo extends Instruction.WithAddress {

        public JumpTo(UShort address) {
            super(address);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xB000);
        }

        @Override
        public State execute(State state) {
            return state.withProgamCounter(address);
        }   
    }
    
    /**
     * Skips the next instruction if value at register equals constant.
     */
    public class SkipIfEquals extends Instruction.WithRegisterAnd8BitConstant {

        public SkipIfEquals(Register register, UByte constant) {
            super(register, constant);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected int getConstantOffset() {
            return 1;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x3000);
        }

        @Override
        public State execute(State state) {
            UByte registerValue = state.getRegister(register);
            
            if (registerValue.equals(constant)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if value at register doesn't equal constant.
     */
    public class SkipIfNotEquals extends Instruction.WithRegisterAnd8BitConstant {

        public SkipIfNotEquals(Register register, UByte constant) {
            super(register, constant);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected int getConstantOffset() {
            return 1;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x4000);
        }

        @Override
        public State execute(State state) {
            UByte registerValue = state.getRegister(register);
            
            if (!registerValue.equals(constant)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if the values of registers X and Y equal.
     */
    public class SkipIfEqualsRegister extends Instruction.WithTwoRegisters {

        public SkipIfEqualsRegister(Register x, Register y) {
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
            return UShort.valueOf(0x5000);
        }

        @Override
        public State execute(State state) {
            UByte registerXValue = state.getRegister(registerX);
            UByte registerYValue = state.getRegister(registerY);
            
            if (registerXValue.equals(registerYValue)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
        
    }
}
