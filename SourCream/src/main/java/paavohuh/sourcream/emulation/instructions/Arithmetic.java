    
package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

/**
 * Contains arithmetic instructions.
 * This class shouldn't be instantiated.
 */
public final class Arithmetic {
    
    private Arithmetic() { }    
    
    /**
     * Sets register X to register X + constant. *Doesn't* set the carry flag.
     */
    public static class AddConstantToRegister extends Instruction.WithRegisterAnd8BitConstant {

        /**
        * Sets register X to register X + constant. *Doesn't* set the carry flag.
         * @param register The register.
         * @param constant The constant.
        */
        public AddConstantToRegister(Register register, UByte constant) {
            super(register, constant);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected int getConstantOffset() {
            return 0;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x7000);
        }

        @Override
        public State execute(State state) {
            int registerValue = state.getRegister(register).intValue();
            
            UByte sum = UByte.valueOf((registerValue + constant.intValue()) & 0xFF);
            
            return state.withRegister(register, sum);
        }
    }
    
    /**
     * Sets register X to register X + register Y. Sets the carry flag if the sum exceeds 255.
     */
    public static class AddRegisterToRegister extends Instruction.WithTwoRegisters {

        /**
         * Sets register X to register X + register Y. Sets the carry flag if the sum exceeds 255.
         * @param x Register X.
         * @param y Register Y.
         */
        public AddRegisterToRegister(Register x, Register y) {
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
            return UShort.valueOf(0x8004);
        }

        @Override
        public State execute(State state) {
            int registerXValue = getRegisterX(state).intValue();
            int registerYValue = getRegisterY(state).intValue();
            
            int sum = registerXValue + registerYValue;
            boolean carry = sum > 0xFF;
            
            return state
                .withRegister(Register.CARRY, UByte.valueOf(carry ? 1 : 0))
                .withRegister(registerX, UByte.valueOf(sum & 0xFF));
        }        
    }
    
    /**
     * Sets register X to register X - register Y. Sets the borrow flag if
     * required.
     */
    public static class SubtractRegisterYFromX extends Instruction.WithTwoRegisters {
        /**
         * Sets register X to register X - register Y. Sets the borrow flag if
         * required.
         * @param x Register X.
         * @param y Register Y.
         */
        public SubtractRegisterYFromX(Register x, Register y) {
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
            return UShort.valueOf(0x8005);
        }

        @Override
        public State execute(State state) {
            int registerXValue = getRegisterX(state).intValue();
            int registerYValue = getRegisterY(state).intValue();
            
            int subtraction = registerXValue - registerYValue;
            boolean borrow = registerYValue > registerXValue;
            
            return state
                .withRegister(Register.BORROW, UByte.valueOf(borrow ? 0 : 1))
                .withRegister(registerX, UByte.valueOf(subtraction & 0xFF));
        }  
    }

    /**
     * Sets register X to register Y - register X. Sets the borrow flag if
     * required.
     */
    public static class SubtractRegisterXFromY extends Instruction.WithTwoRegisters {

        /**
         * Sets register X to register Y - register X. Sets the borrow flag if
         * required.
         * @param x Register X.
         * @param y Register Y.
         */
        public SubtractRegisterXFromY(Register x, Register y) {
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
            return UShort.valueOf(0x8007);
        }

        @Override
        public State execute(State state) {
            int registerXValue = getRegisterX(state).intValue();
            int registerYValue = getRegisterY(state).intValue();
            
            int subtraction = registerYValue - registerXValue;
            boolean borrow = registerXValue > registerYValue;
            
            return state
                .withRegister(Register.BORROW, UByte.valueOf(borrow ? 0 : 1))
                .withRegister(registerX, UByte.valueOf(subtraction & 0xFF));
        }  
    }
    
    /**
     * Increments the address register by the value of a register.
     */
    public static class AddRegisterToAddressRegister extends Instruction.WithRegister {

        /**
         * Increments the address register by the value of a register.
         * @param register The register.
         */
        public AddRegisterToAddressRegister(Register register) {
            super(register);
        }
        
        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF01E);
        }

        @Override
        public State execute(State state) {
            
            int sum = getRegister(state).intValue() + state.getAddressRegister().intValue();
            
            return state.withAddressRegister(UShort.valueOf(sum));
        }
    }
    
    /**
     * Gets all arithmetic instructions.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(AddConstantToRegister::new),
            getAllInstances(AddRegisterToRegister::new),
            getAllInstances(SubtractRegisterXFromY::new),
            getAllInstances(SubtractRegisterYFromX::new),
            getAllInstances(AddRegisterToAddressRegister::new));
    }
}
