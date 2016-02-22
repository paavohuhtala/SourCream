    
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
            
            // This instruction doesn't set the carry flag. Instead, it merely
            // mods by 256.
            UByte sum = UByte.valueOf((registerValue + constant.intValue()) & 0xFF);
            
            return state.withRegister(register, sum);
        }
    }
    
    /**
     * Sets register X to register X + register Y. Sets the carry flag if the sum exceeds 255.
     */
    public static class AddRegisterToRegister extends Instruction.WithTwoRegisters {

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
    
    // CONSIDER: Shares 99% of code with the class above. However, very hard to
    // abstract without additional inheritance, which I'd like to avoid.
    /**
     * Sets register X to register Y - register X. Sets the borrow flag if
     * required.
     */
    public static class SubtractRegisterXFromY extends Instruction.WithTwoRegisters {

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
    
    public static class AddRegisterToAddressRegister extends Instruction.WithRegister {

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
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(AddConstantToRegister::new),
            getAllInstances(AddRegisterToRegister::new),
            getAllInstances(SubtractRegisterXFromY::new),
            getAllInstances(SubtractRegisterYFromX::new),
            getAllInstances(AddRegisterToAddressRegister::new));
    }
}
