package paavohuh.sourcream.emulation.instructions;

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
            
            return state.withCopiedMemory(registers, addressRegister);
        }

        @Override
        protected int getOffset() {
            return 2;
        }
        
    }
    
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            getAllInstances(SetAddressRegister::new),
            getAllInstances(CopyRegister::new),
            getAllInstances(SetRegister::new),
            getAllInstances(StoreRegisters::new));
    }
}
