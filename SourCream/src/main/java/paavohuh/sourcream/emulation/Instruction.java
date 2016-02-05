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
        
        public WithRegister(byte register) {
            this.register = new Register(register);
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegOffset());
        }
        
        @FunctionalInterface
        public interface Constructor {
            WithRegister build(Byte register);
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
        
        public WithTwoRegisters(byte registerX, byte registerY) {
            this(new Register(registerX), new Register(registerY));
        }
        
        public WithTwoRegisters(Register x, Register y) {
            this.registerX = x;
            this.registerY = y;
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(InstructionUtils.setRegister(getBaseCode(), registerX, getRegXOffset()), registerY, getRegYOffset());
        }
       
        @FunctionalInterface
        public interface Constructor {
            WithTwoRegisters build(Byte registerX, Byte registerY);
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
            return UShort.valueOf(getBaseCode().intValue() | address.intValue());
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
            return UShort.valueOf(getBaseCode().intValue() | (constant.intValue() << (getOffset() * 4)));
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
        
        @FunctionalInterface
        public interface Constructor {
            With4BitConstant build(Byte register, UByte constant);
        }
    }
    
    public static abstract class With8BitConstant implements Instruction {
        
    }
}
