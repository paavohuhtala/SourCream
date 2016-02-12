    
package paavohuh.sourcream.emulation.instructions;

import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;

/**
 * Contains arithmetic instructions.
 * This class shouldn't be instantiated.
 */
public final class Arithmetic {
    
    private Arithmetic() { }
    
    /**
     * Adds a constant to a register. *Doesn't* set the carry flag.
     */
    public class AddConstantToRegister extends Instruction.WithRegisterAnd8BitConstant {

        public AddConstantToRegister(Register register, UByte constant) {
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
            return UShort.valueOf(0x7000);
        }

        @Override
        public State execute(State state) {
            int registerValue = state.getRegister(register).intValue();
            
            // This instruction doesn't set the carry flag. Instead, it merely
            // mods by 256.
            UByte sum = UByte.valueOf((registerValue + constant.intValue()) % 256);
            
            return state.withRegister(register, sum);
        }
    }
    
    /**
     * Adds two registers together. Sets the carry flag if the sum exceeds 255.
     */
    public class AddRegisterToRegister extends Instruction.WithTwoRegisters {

        public AddRegisterToRegister(byte registerX, byte registerY) {
            super(registerX, registerY);
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
                .withRegister(Register.Carry, UByte.valueOf(carry ? 1 : 0))
                .withRegister(registerX, UByte.valueOf(sum & 0xFF));
        }        
    }
    
    public class SubtractRegisterYFromX extends Instruction.WithTwoRegisters {
        
        public SubtractRegisterYFromX(byte registerX, byte registerY) {
            super(registerX, registerY);
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
            boolean borrow = subtraction < 0;
            
            return state
                .withRegister(Register.Borrow, UByte.valueOf(borrow ? 1 : 0))
                .withRegister(registerX, UByte.valueOf(subtraction & 0xFF));
        }  
    }
    
    // CONSIDER: Shares 99% of code with the class above. However, very hard to
    // abstract without additional inheritance, which I'd like to avoid.
    public class SubtractRegisterXFromY extends Instruction.WithTwoRegisters {
        
        public SubtractRegisterXFromY(byte registerX, byte registerY) {
            super(registerX, registerY);
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
            boolean borrow = subtraction < 0;
            
            return state
                .withRegister(Register.Borrow, UByte.valueOf(borrow ? 1 : 0))
                .withRegister(registerX, UByte.valueOf(subtraction & 0xFF));
        }  
    }
}
