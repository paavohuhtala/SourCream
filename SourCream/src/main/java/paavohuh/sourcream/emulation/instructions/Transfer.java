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

        /**
         * Sets the address register.
         * @param address The address.
         */
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

        /**
         * Copies register VX to register VY. 
         * @param x Register X.
         * @param y Register Y.
         */
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

        /**
         * Sets register VX to constant NN.
         * @param register The register.
         * @param constant The constant.
         */
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
    public static class StoreRegisters extends Instruction.WithRegister {

        /**
         * Copies registers V0 - VX to RAM, beginning from address register I.
         * @param register The last register to copy.
         */
        public StoreRegisters(Register register) {
            super(register);
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF055);
        }

        @Override
        public State execute(State state) {
            UShort addressRegister = state.getAddressRegister();
            byte[] registers = new byte[register.id + 1];
            
            // Copy registers V0 - VX to a temporary buffer 
            for (int i = 0; i < registers.length; i++) {
                registers[i] = (byte) state.getRegister(new Register(i)).intValue();
            }
            
            UShort newAddressRegister = UShort.valueOf(addressRegister.intValue() + register.id + 1);
            
            return state
                .withCopiedMemory(registers, addressRegister)
                .withAddressRegister(newAddressRegister);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }
    }
    
    /**
     * Copies memory beginning from I to N registers.
     */
    public static class CopyToRegisters extends Instruction.WithRegister {

        /**
         * Copies memory beginning from I to N registers.
         * @param register The last register to copy to.
         */
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
            
            for (int i = 0; i <= register.id; i++) {
                UShort address = UShort.valueOf(addressRegister + i);
                Register reg = new Register(i);
                newState = newState.withRegister(reg, UByte.valueOf(state.getMemoryFrom(address, 1)[0]));
            }
            
            UShort newAddressRegister = UShort.valueOf(addressRegister + register.id + 1);
            
            return newState.withAddressRegister(newAddressRegister);
        }
    }
    
    /**
     * Converts the value of a register to its binary coded decimal
     * representation, and stores the digits in [I, (I + 2)]. The representation
     * uses leading zeroes.
     */
    public static class StoreBinaryCodedDecimal extends Instruction.WithRegister {

        /**
        * Converts the value of a register to its binary coded decimal
        * representation.
        * @param register The register.
        */
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
    
    /**
     * Sets the delay timer to the value of a register.
     */
    public static class SetDelayTimer extends Instruction.WithRegister {

        /**
         * Sets the delay timer to the value of a register.
         * @param register The register.
         */
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
    
    /**
     * Sets the sound timer to the value of a register.
     */
    public static class SetSoundTimer extends Instruction.WithRegister {

        /**
         * Sets the sound timer to the value of a register.
         * @param register The register.
         */
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
    
    /**
     * Sets a register to the value of the delay timer.
     */
    public static class SetRegisterToDelayTimer extends Instruction.WithRegister {
        
        /**
        * Sets a register to the value of the delay timer.
        * @param register The register.
        */
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
    
    /**
     * Sets a register to a random value masked (bitwise ANDed) with a constant.
     */
    public static class SetRegisterRandom extends Instruction.WithRegisterAnd8BitConstant {

        private static final Random RNG = new Random();
        
        /**
        * Sets a register to a random value masked (bitwise ANDed) with a
        * constant.
         * @param register The register.
         * @param constant The mask.
        */
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
            int randomByte = RNG.nextInt(256);
            int mask = constant.intValue();
            int maskedRandom = randomByte & mask;
            
            return state.withRegister(register, UByte.valueOf(maskedRandom));
        }
    }
    
    /**
     * Gets all transfer instructions.
     * @return A sequence of instructions.
     */
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
