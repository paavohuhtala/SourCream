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
        protected WithRegister(Register register) {
            this.register = register;
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegisterOffset());
        }
        
        protected UByte getRegister(State state) {
            return state.getRegister(register);
        }
        
        @FunctionalInterface
        /**
         * The constructor interface for WithRegister
         */
        public interface Constructor {
            /**
             * Builds a new instruction.
             * @param register The register.
             * @return The new instruction.
             */
            WithRegister build(Register register);
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", this.getClass().getSimpleName(), register.toString());
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
        protected WithTwoRegisters(Register x, Register y) {
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
            /**
             * Builds a new instruction.
             * @param registerX The register X.
             * @param registerY The register Y.
             * @return The new instruction.
             */
            WithTwoRegisters build(Register registerX, Register registerY);
        }
        
        @Override
        public String toString() {
            return String.format("%s(%s, %s)",
                this.getClass().getSimpleName(),
                registerX.toString(),
                registerY.toString());
        }
    }
    
    /**
     * An abstract instruction with two registers and one 4-bit constant.
     */
    public static abstract class WithTwoRegistersAnd4BitConstant extends WithTwoRegisters {
        /**
         * The constant.
         */
        public final UByte constant;
        
        protected abstract int getConstantOffset();
        
        /**
         * Creates a new instruction with two registers and a 4-bit constant.
         * @param x
         * @param y
         * @param constant 
         */
        protected WithTwoRegistersAnd4BitConstant(Register x, Register y, UByte constant) {
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
            /**
             * Builds a new instruction.
             * @param registerX The register X.
             * @param registerY The register Y.
             * @param constant The constant.
             * @return The new instruction.
             */
            WithTwoRegistersAnd4BitConstant build(Register registerX, Register registerY, UByte constant);
        }
        
        @Override
        public String toString() {
            return String.format("%s(%02X)",
                this.getClass().getSimpleName(),
                constant.intValue());
        }
    }
    
    /**
     * An abstract instruction with one 12-bit address parameter.
     */
    public static abstract class WithAddress extends Instruction.Parametrized {
        /**
         * The address.
         */
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
            /**
             * Builds a new instruction.
             * @param address The address.
             * @return The new instruction.
             */
            WithAddress build(UShort address);
        }
        
        @Override
        public String toString() {
            return String.format("%s(%04X)",
                this.getClass().getSimpleName(),
                address.intValue());
        }
    }
    
    /**
     * An abstract instruction with one 4-bit constant.
     */
    public static abstract class With4BitConstant extends Instruction.Parametrized {
        /**
         * The 4-bit constant.
         */
        public final UByte constant;
        
        protected abstract int getOffset();
        
        /**
         * Creates a new instruction with one 4-bit constant.
         * @param constant The constant.
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
            /**
             * Builds a new instruction. 
             * @param constant The constant.
             * @return The new instruction.
             */
            With4BitConstant build(UByte constant);
        }
        
        @Override
        public String toString() {
            return String.format("%s(%1X)",
                this.getClass().getSimpleName(),
                constant.intValue());
        }
    }
    
    /**
     * An abstract instruction with one register and one 8-bit constant.
     */
    public static abstract class WithRegisterAnd8BitConstant extends Instruction.Parametrized {
        /**
         * The register.
         */
        public final Register register;
        
        /**
         * The 8-bit constant.
         */
        public final UByte constant;

        protected abstract int getRegisterOffset();
        protected abstract int getConstantOffset();
        
        /**
         * Creates a new instruction with a register and a 8-bit constant.
         * @param register The register.
         * @param constant The constant.
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
            /**
             * Builds a new instruction. 
             * @param register The register.
             * @param constant The constant.
             * @return The new instruction.
             */
            WithRegisterAnd8BitConstant build(Register register, UByte constant);
        }
        
        @Override
        public String toString() {
            return String.format("%s(%s, %02X)",
                this.getClass().getSimpleName(),
                register.toString(),
                constant.intValue());
        }
    }
}
