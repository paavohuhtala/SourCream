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
     * Gets the code for this instructions.
     * @return A 16-bit bytecode.
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
        /**
         * The register used by this instruction.
         */
        public final Register register;

        protected abstract int getRegisterOffset();
        
        /**
         * Creates a new instruction that uses a single register.
         * @param register The register.
         */
        public WithRegister(Register register) {
            this.register = register;
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegisterOffset());
        }
        
        @FunctionalInterface
        /**
         * The constructor interface for WithRegister
         */
        public interface Constructor {
            WithRegister build(Register register);
        }
    }
    
    /**
     * An abstract instruction with two register parameters.
     */
    public static abstract class WithTwoRegisters extends Instruction.Parametrized {
        /**
         * The first register used by this instruction.
         */
        public final Register registerX;
        
        /**
         * The second register used by this instruction.
         */
        public final Register registerY;

        protected abstract int getRegXOffset();
        protected abstract int getRegYOffset();
        
        /**
         * Creates a new instruction that uses two registers. 
         * @param x
         * @param y 
         */
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
       
        /**
         * The constructor interface for WithTwoRegisters.
         */
        @FunctionalInterface
        public interface Constructor {
            WithTwoRegisters build(Register registerX, Register registerY);
        }
    }
    
    /**
     * An abstract instruction with two registers and one 4-bit constant.
     */
    public static abstract class WithTwoRegistersAnd4BitConstant extends WithTwoRegisters {
        
        public final UByte constant;
        
        protected abstract int getConstantOffset();
        
        /**
         * Creates a new instruction with two registers and a 4-bit constant.
         * @param x
         * @param y
         * @param constant 
         */
        public WithTwoRegistersAnd4BitConstant(Register x, Register y, UByte constant) {
            super(x, y);
            this.constant = constant;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                super.getCode().intValue() |
                (constant.intValue() << (getConstantOffset() * 4)));
        }
        
        @FunctionalInterface
        /**
         * The constructor interface for WithTwoRegistersAnd4BitConstant.
         */
        public interface Constructor {
            WithTwoRegistersAnd4BitConstant build(Register registerX, Register registerY, UByte constant);
        }
    }
    
    /**
     * An abstract instruction with one 12-bit address parameter.
     */
    public static abstract class WithAddress extends Instruction.Parametrized {
        public final UShort address;
        
        /**
         * Creates a new instruction with one 12-bit address.
         * @param address 
         */
        protected WithAddress(UShort address) {
            this.address = address;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                getBaseCode().intValue() |
                address.intValue());
        }
        
        @FunctionalInterface
        /**
         * The constructor interface for WithAddress.
         */
        public interface Constructor {
            WithAddress build(UShort address);
        }
    }
    
    /**
     * An abstract instruction with one 4-bit constant.
     */
    public static abstract class With4BitConstant extends Instruction.Parametrized {
        public final UByte constant;
        
        protected abstract int getOffset();
        
        /**
         * Creates a new instruction with one 4-bit constant.
         * @param constant 
         */
        protected With4BitConstant(UByte constant) {
            this.constant = constant;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(
                getBaseCode().intValue() | 
                constant.intValue() << (getOffset() * 4));
        }
        
        @FunctionalInterface
        /**
         * The constructor interface for With4BitConstant.
         */
        public interface Constructor {
            With4BitConstant build(UByte constant);
        }
    }
    
    /**
     * An abstract instruction with one register and one 8-bit constant.
     */
    public static abstract class WithRegisterAnd8BitConstant extends Instruction.Parametrized {
        public final Register register;
        public final UByte constant;

        protected abstract int getRegisterOffset();
        protected abstract int getConstantOffset();
        
        /**
         * Creates a new instruction with a register and a 8-bit constant.
         * @param register
         * @param constant 
         */
        protected WithRegisterAnd8BitConstant(Register register, UByte constant) {
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
        /**
         * The constructor interface for WithRegisterAnd8BitConstant.
         */
        public interface Constructor {
            WithRegisterAnd8BitConstant build(Register register, UByte constant);
        }
    }
    
    /**
     * NOT IMPLEMENTED: An abstract instruction with one 8-bit constant.
     */
    public static abstract class With8BitConstant implements Instruction {
        
    }
}
