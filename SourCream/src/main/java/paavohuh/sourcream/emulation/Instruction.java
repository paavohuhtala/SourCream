package paavohuh.sourcream.emulation;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.jooq.lambda.Seq;
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
     * An abstract instruction with one register parameter.
     */
    public static abstract class WithOneRegister implements Instruction {
        public final Register register;

        protected abstract UShort getBaseCode();
        protected abstract int getRegOffset();
        
        public WithOneRegister(byte register) {
            this.register = new Register(register);
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegOffset());
        }
        
        public static Seq<WithOneRegister> getAllInstances(Function<Byte, WithOneRegister> ctor) {
            return Seq.range((byte)0, (byte)16).map(ctor);
        }
    }
    
    /**
     * An abstract instruction with two register parameters.
     */
    public static abstract class WithTwoRegisters implements Instruction {
        public final Register registerX;
        public final Register registerY;

        protected abstract UShort getBaseCode();
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
        
        public static Seq<WithTwoRegisters> getAllInstances(BiFunction<Byte, Byte, WithTwoRegisters> ctor) {
            return
                Seq.range((byte)0, (byte)16).crossJoin(Seq.range((byte)0, (byte)15)) 
                .map(t -> ctor.apply(t.v1, t.v2));
        }
    }
    
    /**
     * An abstract instruction with one 12-bit address parameter.
     */
    public static abstract class WithAddress implements Instruction {
        public final UShort address;
        
        protected abstract UShort getBaseCode();
        
        public WithAddress(UShort address) {
            this.address = address;
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(getBaseCode().intValue() | address.intValue());
        }
        
        public static Seq<WithAddress> getAllInstances(Function<UShort, WithAddress> ctor) {
            return
                Seq.range(0, 4096)
                .map(i -> ctor.apply(UShort.valueOf(i)));
        }
    }
    
    public static abstract class With4BitConstant implements Instruction {
        public final UByte constant;
        
        protected abstract int getOffset();
        protected abstract UShort getBaseCode();

        public With4BitConstant(byte constant) {
            this.constant = UByte.valueOf(constant);
        }
        
        @Override
        public UShort getCode() {
            return UShort.valueOf(getBaseCode().intValue() | (constant.intValue() << (getOffset() * 4)));
        }
        
        // TODO: Same as WithOneRegister.getAllInstances
        public static Seq<With4BitConstant> getAllInstances(Function<Byte, With4BitConstant> ctor) {
            return Seq.range((byte)0, (byte)16).map(ctor);
        }
    }
    
    public static abstract class With8BitConstant implements Instruction {
        
    }
}
