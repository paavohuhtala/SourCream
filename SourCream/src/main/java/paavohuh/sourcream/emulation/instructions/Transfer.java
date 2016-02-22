package paavohuh.sourcream.emulation.instructions;

import java.util.Random;
import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;
import paavohuh.sourcream.emulation.Register;

/**
 * Contains transfer instructions.
 * This class shouldn't be instantiated.
 */
public class Transfer {
    
    /**
     * Sets the address register (VI) to a constant. 
     */
    public static class SetAddressRegister extends Instruction.WithAddress {

        public SetAddressRegister(UShort address) {
            super(address);
        }

        @Override
        public State execute(State state) {
            return state.withAddressRegister(address);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xA000);
        }
    }
    
    /**
     * Copies register VX to register VY.
     */
    public static class CopyRegister extends Instruction.WithTwoRegisters {

        public CopyRegister(Register x, Register y) {
            super(x, y);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0x8000);
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
        public State execute(State state) {
            return state.withRegister(registerX, state.getRegister(registerY));
        }    
    }
    
    /**
     * Sets register VX to constant NN.
     */
    public static class SetRegister extends Instruction.WithRegisterAnd8BitConstant {

        public SetRegister(Register register, UByte constant) {
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
            return UShort.valueOf(0x6000);
        }

        @Override
        public State execute(State state) {
            return state.withRegister(register, constant);
        }    
    }
    
    /**
     * Copies registers V0 - VX to RAM, beginning from address register I.
     */
    public static class StoreRegisters extends Instruction.With4BitConstant {

        public StoreRegisters(UByte constant) {
            super(constant);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF055);
        }

        @Override
        public State execute(State state) {
            UShort addressRegister = state.getAddressRegister();
            byte[] registers = new byte[constant.intValue() + 1];
            
            // Copy registers V0 - VX to a temporary buffer 
            for (int i = 0; i < registers.length; i++) {
                registers[i] = (byte) state.getRegister(new Register(i)).intValue();
            }
            
            UShort newAddressRegister = UShort.valueOf(addressRegister.intValue() + constant.intValue() + 1);
            
            return state
                .withCopiedMemory(registers, addressRegister)
                .withAddressRegister(newAddressRegister);
        }

        @Override
        protected int getOffset() {
            return 2;
        }   
    }
    
    public static class CopyToRegisters extends Instruction.WithRegister {

        public CopyToRegisters(Register register) {
            super(register);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF065);
        }

        @Override
        public State execute(State state) {
            State newState = state;
            int addressRegister = state.getAddressRegister().intValue();
            
            for (int i = 0; i < register.id; i++) {
                UShort address = UShort.valueOf(addressRegister + i);
                Register reg = new Register(i);
                newState = newState.withRegister(reg, UByte.valueOf(state.getMemoryFrom(address, 1)[0]));
            }
            
            UShort newAddressRegister = UShort.valueOf(addressRegister + register.id + 1);
            
            return newState.withAddressRegister(newAddressRegister);
        }
    }
    
    public static class StoreBinaryCodedDecimal extends Instruction.WithRegister {

        public StoreBinaryCodedDecimal(Register register) {
            super(register);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF033);
        }

        @Override
        public State execute(State state) {
            String digits = String.format("%03d", getRegister(state).intValue());
            
            byte[] digitBytes = new byte[3];
            digitBytes[0] = (byte) Character.getNumericValue(digits.charAt(0));
            digitBytes[1] = (byte) Character.getNumericValue(digits.charAt(1));
            digitBytes[2] = (byte) Character.getNumericValue(digits.charAt(2));
            
            return state.withCopiedMemory(digitBytes, state.getAddressRegister());
        }
    }
    
    public static class SetDelayTimer extends Instruction.WithRegister {

        public SetDelayTimer(Register register) {
            super(register);
        }
        
        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF015);
        }

        @Override
        public State execute(State state) {
            return state.withDelayTimer(getRegister(state), true);
        }
    }
    
    public static class SetSoundTimer extends Instruction.WithRegister {

        public SetSoundTimer(Register register) {
            super(register);
        }
        
        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF018);
        }

        @Override
        public State execute(State state) {
            return state.withSoundTimer(getRegister(state), true);
        }
    }
    
    public static class SetRegisterToDelayTimer extends Instruction.WithRegister {
        
        public SetRegisterToDelayTimer(Register register) {
            super(register);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF007);
        }

        @Override
        public State execute(State state) {
            return state.withRegister(register, state.getDelayTimer());
        }
    }
    
    public static class SetRegisterRandom extends Instruction.WithRegisterAnd8BitConstant {

        private static final Random rng = new Random();
        
        public SetRegisterRandom(Register register, UByte constant) {
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
            return UShort.valueOf(0xC000);
        }

        @Override
        public State execute(State state) {
            int randomByte = rng.nextInt(256);
            int mask = constant.intValue();
            int maskedRandom = randomByte & mask;
            
            return state.withRegister(register, UByte.valueOf(maskedRandom));
        }
        
    }
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(SetAddressRegister::new),
            getAllInstances(CopyRegister::new),
            getAllInstances(SetRegister::new),
            getAllInstances(StoreRegisters::new),
            getAllInstances(CopyToRegisters::new),
            getAllInstances(StoreBinaryCodedDecimal::new),
            getAllInstances(SetSoundTimer::new),
            getAllInstances(SetDelayTimer::new),
            getAllInstances(SetRegisterToDelayTimer::new),
            getAllInstances(SetRegisterRandom::new));
    }
}
