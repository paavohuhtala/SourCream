
package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;

/**
 * Contains control instructions.
 * This class shouldn't be instantiated.
 */
public final class Control {
    private Control() { }
    
    /**
     * Jumps to address.
     */
    public static class JumpTo extends Instruction.WithAddress {

        public JumpTo(UShort address) {
            super(address);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x1000);
        }

        @Override
        public State execute(State state) {
            return state.withProgamCounter(address);
        }   
    }
    
    /**
     * Jumps to address + V0.
     */
    public static class JumpToWithOffset extends Instruction.WithAddress {

        public JumpToWithOffset(UShort address) {
            super(address);
        }
        
        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xB000);
        }

        @Override
        public State execute(State state) {
            int newAddress = address.intValue() + state.getRegister(Register.V0).intValue();
            return state.withProgamCounter(UShort.valueOf(newAddress));
        }   
    }
    
    /**
     * Skips the next instruction if value at register equals constant.
     */
    public static class SkipIfEquals extends Instruction.WithRegisterAnd8BitConstant {

        public SkipIfEquals(Register register, UByte constant) {
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
            return UShort.valueOf(0x3000);
        }

        @Override
        public State execute(State state) {
            UByte registerValue = state.getRegister(register);
            
            if (registerValue.intValue() == constant.intValue()) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if value at register doesn't equal constant.
     */
    public static class SkipIfNotEquals extends Instruction.WithRegisterAnd8BitConstant {

        public SkipIfNotEquals(Register register, UByte constant) {
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
            return UShort.valueOf(0x4000);
        }

        @Override
        public State execute(State state) {
            UByte registerValue = state.getRegister(register);
            
            if (registerValue.intValue() != constant.intValue()) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if the values of registers X and Y are equal.
     */
    public static class SkipIfEqualsRegister extends Instruction.WithTwoRegisters {

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
            if (getRegisterX(state).intValue() ==  getRegisterY(state).intValue()) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
    }
    
    /**
     * Skips the next instruction if the values of register X and Y are not equal.
     */
    public static class SkipIfNotEqualsRegister extends Instruction.WithTwoRegisters {

        public SkipIfNotEqualsRegister(Register x, Register y) {
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
            return UShort.valueOf(0x9000);
        }

        @Override
        public State execute(State state) {
            if (getRegisterX(state) != getRegisterY(state)) {
                return state.withIncrementedPc();
            } else {
                return new State(state);
            }
        }
        
    }
    
    public static class CallSubroutine extends Instruction.WithAddress {

        public CallSubroutine(UShort address) {
            super(address);
        }
        
        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x2000);
        }

        @Override
        public State execute(State state) {
            return state.withCallTo(address);
        }
    }
    
    public static class Return implements Instruction {

        @Override
        public State execute(State state) {
            return state.withReturn();
        }

        @Override
        public UShort getCode() {
            return UShort.valueOf(0x00EE);
        }
        
    }
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            Seq.of(new Return()),
            getAllInstances(JumpTo::new),
            getAllInstances(JumpToWithOffset::new),
            getAllInstances(SkipIfEquals::new),
            getAllInstances(SkipIfNotEquals::new),
            getAllInstances(SkipIfEqualsRegister::new),
            getAllInstances(SkipIfNotEqualsRegister::new),
            getAllInstances(CallSubroutine::new));
    }
}
