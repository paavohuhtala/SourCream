package paavohuh.sourcream.emulation;

import org.joou.UByte;
import org.joou.UShort;

/**
 An instruction is a combination of an opcode and its arguments. 
 */
public interface Instruction {
    /**
     * Executes the instruction by taking the current state of the CPU and
     * returning a new one, without modifying the original.
     * @param state System state
     * @return System after after execution
     */
    State execute(State state);
    
    /**
     * @return Returns the bytecode for this instruction.
    */
    UShort getCode();
    
    /**
     * Represents a parametrized instruction. All instances of the instruction
     * have a shared opcode, which is modified to produce the final code.
     */
    public abstract class Parametrized implements Instruction {
        protected abstract UShort getBaseCode();
    }
    
    /**
     * An abstract instruction with one register parameter.
     */
    public static abstract class WithRegister extends Instruction.Parametrized {
        public final Register register;

        protected abstract int getRegOffset();
        
        public WithRegister(Register register) {
            this.register = register;
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegOffset());
        }
        
        @FunctionalInterface
        public interface Constructor {
            WithRegister build(Register register);
        }
    }
    
    /**
     * An abstract instruction with two register parameters.
     */
    public static abstract class WithTwoRegisters extends Instruction.Parametrized {
        public final Register registerX;
        public final Register registerY;

        protected abstract int getRegXOffset();
        protected abstract int getRegYOffset();
        
        public WithTwoRegisters(Register x, Register y) {
            this.registerX = x;
            this.registerY = y;
        }
        
        protected UByte getRegisterX(State state) {
            return state.getRegister(registerX);
        }
        
        protected UByte getRegisterY(State state) {
            return state.getRegister(registerY);
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(InstructionUtils.setRegister(getBaseCode(), registerX, getRegXOffset()), registerY, getRegYOffset());
        }
       
        @FunctionalInterface
        public interface Constructor {
            WithTwoRegisters build(Register registerX, Register registerY);
        }
    }
    
    /**
     * An abstract instruction with one 12-bit address parameter.
     */
    public static abstract class WithAddress extends Instruction.Parametrized {
        public final UShort address;
                
        public WithAddress(UShort address) {
            this.address = address;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                getBaseCode().intValue() |
                address.intValue());
        }
        
        @FunctionalInterface
        public interface Constructor {
            WithAddress build(UShort address);
        }
    }
    
    public static abstract class With4BitConstant extends Instruction.Parametrized {
        public final UByte constant;
        
        protected abstract int getOffset();
        
        public With4BitConstant(UByte constant) {
            this.constant = constant;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                getBaseCode().intValue() | 
                constant.intValue() << (getOffset() * 4));
        }
        
        @FunctionalInterface
        public interface Constructor {
            With4BitConstant build(UByte constant);
        }
    }
    
    public static abstract class WithRegisterAnd8BitConstant extends Instruction.Parametrized {
        public final Register register;
        public final UByte constant;

        protected abstract int getRegisterOffset();
        protected abstract int getConstantOffset();
        
        public WithRegisterAnd8BitConstant(Register register, UByte constant) {
            this.register = register;
            this.constant = constant;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                getBaseCode().intValue() |
                (constant.intValue() << getConstantOffset() * 4) |
                (register.id << getRegisterOffset() * 4));
        }
        
        @FunctionalInterface
        public interface Constructor {
            WithRegisterAnd8BitConstant build(Register register, UByte constant);
        }
    }
    
    public static abstract class With8BitConstant implements Instruction {
        
    }
}
