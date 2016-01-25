package paavohuh.sourcream.emulation;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.jooq.lambda.Seq;
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
    public static abstract class OneRegister implements Instruction {
        protected final Register register;

        protected abstract UShort getBaseCode();
        protected abstract int getRegOffset();
        
        public OneRegister(byte register) {
            this.register = new Register(register);
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(getBaseCode(), register, getRegOffset());
        }
        
        public static Seq<Instruction> getAllInstances(Function<Byte, Instruction> ctor) {
            return Seq.range((byte)0, (byte)16).map(ctor);
        }
    }
    
    /**
     * An abstract instruction with two register parameters.
     */
    public static abstract class TwoRegister implements Instruction {
        protected final Register registerX;
        protected final Register registerY;

        protected abstract UShort getBaseCode();
        protected abstract int getRegXOffset();
        protected abstract int getRegYOffset();
        
        public TwoRegister(byte registerX, byte registerY) {
            this.registerX = new Register(registerX);
            this.registerY = new Register(registerY);
        }
        
        @Override
        public UShort getCode() {
            return InstructionUtils.setRegister(InstructionUtils.setRegister(getBaseCode(), registerX, getRegXOffset()), registerY, getRegYOffset());
        }
        
        public static Seq<Instruction> getAllInstances(BiFunction<Byte, Byte, Instruction> ctor) {
            return
                Seq.range((byte)0, (byte)16).crossJoin(Seq.range((byte)0, (byte)15)) 
                .map(t -> ctor.apply(t.v1, t.v2));
        }
    }
    
    /**
     * An abstract instruction with one 12-bit address parameter.
     */
    public static abstract class Address implements Instruction {
        protected final UShort address;
        
        public Address(UShort address) {
            this.address = address;
        }
        
        public static Seq<Instruction> getAllInstances(Function<UShort, Instruction> ctor) {
            return
                Seq.range(0, 4096)
                .map(i -> ctor.apply(UShort.valueOf(i)));
        }
    }
}
