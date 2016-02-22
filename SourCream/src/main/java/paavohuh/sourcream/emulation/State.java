package paavohuh.sourcream.emulation;

import java.io.Serializable;
import java.util.Arrays;
import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.Resource;

import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.utils.ArrayUtils;

/**
 * Represents the state of the system. Exposes a fluent API, which returns new 
 * mutated instances.
 */
public class State implements Cloneable, Serializable {
    
    // The current execution state. Defaults to PAUSED.
    private ExecutionState executionState;
    
    // 4K (by default) of system RAM
    private byte[] ram;
    
    // 64x32 (by default) 1-bit screen buffer
    private ScreenBuffer screen; 
    
    // 16 8-bit registers, from V0 to VF. VF is the carry flag.
    private UByte[] registers;
    
    // One 16-bit address register
    private UShort addressRegister;

    // Progam counter
    private UShort pc;
    
    // Stack pointer
    private UByte sp;
    
    // The stack. 16 values by default.
    private UShort[] stack;
    
    // Two 8-bit timers.
    private UByte delayTimerValue;
    private UByte soundTimerValue;
    
    // Set to true if an instruction changes the timers.
    private boolean delayTimerChanged;
    private boolean soundTimerChanged;
    
    private InputState inputState;
    
    /**
     * Copy constructor. Doesn't modify the state at all.
     * @param previous The previous state.
     */
    public State(State previous) {
        this.executionState = previous.executionState;
        this.ram = ArrayUtils.clone(previous.ram);
        this.screen = previous.screen;
        this.registers = ArrayUtils.clone(previous.registers);
        this.addressRegister = previous.addressRegister;
        this.pc = previous.pc;
        this.sp = previous.sp;
        this.stack = ArrayUtils.clone(previous.stack);
        this.delayTimerValue = previous.delayTimerValue;
        this.soundTimerValue = previous.soundTimerValue;
        this.delayTimerChanged = previous.delayTimerChanged;
        this.soundTimerChanged = previous.soundTimerChanged;
        this.inputState = previous.inputState;
    }
    
    /**
     * Returns a new bootup state, using the given device configuration.
     * The program counter is set to 0x200, everything else is zeroed out.
     * The execution state is set to PAUSED.
     * @param config 
     */
    public State(DeviceConfiguration config) {
        this.executionState = ExecutionState.paused();
        this.ram = new byte[config.getRamSize()];
        this.screen = new ScreenBuffer(config);
        this.registers = new UByte[16];
        
        for (int i = 0; i < registers.length; i++) {
            registers[i] = UByte.valueOf(0);
        }
        
        this.addressRegister = UShort.valueOf(0);
        this.pc = UShort.valueOf(0x200);
        this.sp = UByte.valueOf(0);
        this.stack = new UShort[16];
        this.delayTimerValue = UByte.valueOf(0);
        this.soundTimerValue = UByte.valueOf(0);
        this.delayTimerChanged = false;
        this.soundTimerChanged = false;
        this.inputState = new InputState();
        
        // We need the system font, so load it from resources and copy it.
        byte[] systemFont = Resource.getSystemFont();
        System.arraycopy(systemFont, 0, ram, 0, systemFont.length);
    }
    
    /**
     * Gets the value of the given register.
     * @param reg The register.
     * @return 
     */
    public UByte getRegister(Register reg) {
        return registers[reg.id];
    }
    
    /**
     * Returns a new state with the given register set to the given value.
     * @param reg The register.
     * @param value The value.
     * @return The new state.
     */
    public State withRegister(Register reg, UByte value) {
        State state = new State(this);
        state.registers[reg.id] = value;
        
        return state;
    }
    
    /**
     * Returns a new state with the address register set to the given value.
     * @param value
     * @return 
     */
    public State withAddressRegister(UShort value) {
        State state = new State(this);
        state.addressRegister = value;
        
        return state;
    }
    
    /**
     * Gets the program counter.
     * @return the program counter
     */
    public UShort getProgramCounter() {
        return pc;
    }
    
    /**
     * Gets 8 bits of memory from the given offset.
     * @param memoryOffset the offset
     * @return 8 bits of data at the given offset
     */
    public UByte get8BitsAt(UShort memoryOffset) {
        int offset = memoryOffset.intValue();
        
        if (offset < 0 || offset > ram.length) {
            throw new IllegalArgumentException();
        }
        
        return UByte.valueOf(ram[offset]);
    }
    
    /**
     * Gets 16 bits of memory from the given offset.
     * @param memoryOffset the offset
     * @return 16 bits of data at the given offset
     */
    public UShort get16BitsAt(UShort memoryOffset) {
        int offset = memoryOffset.intValue();
        
        if (offset < 0 || offset + 1 > ram.length) {
            // TODO: explain why
            throw new IllegalArgumentException();
        }
        
        return UShort.valueOf(Byte.toUnsignedInt(ram[offset]) << 8 | Byte.toUnsignedInt(ram[offset + 1]));
    }
    
    /**
     * Returns a new state with the program counter set to supplied value.
     * @param pc
     * @return The new state
     */
    public State withProgamCounter(UShort pc) {
        State state = new State(this);
        state.pc = pc;
        
        return state;
    }
    
    /**
     * Returns a new state with the program counter incremented by 2.
     * @return The new state
     */
    public State withIncrementedPc() {
        return withProgamCounter(UShort.valueOf(getProgramCounter().intValue() + 2));
    }

    /**
     * Gets the value of the address register
     * @return The value of the address register
     */
    public UShort getAddressRegister() {
        return addressRegister;
    }
    
    /**
     * Returns a new state with the given buffer copied to memory.
     * @param from The buffer to copy
     * @param destinationAddress The destination in RAM where the buffer is copied to
     * @return A new state
     */
    public State withCopiedMemory(byte[] from, UShort destinationAddress) {
        int destination = destinationAddress.intValue();
        
        if (destination < 0 || destination + from.length > ram.length) {
            throw new IllegalArgumentException();
        }
        
        State newState = new State(this);
        System.arraycopy(from, 0, newState.ram, destination, from.length);
        
        return newState;
    }
    
    /**
     * Reads a chunk of memory from the given address to a new buffer.
     * @param sourceAddress The address where to start reading
     * @param length The number of bytes to read
     * @return Bytes between [sourceAddress, sourceAddress.length[
     */
    public byte[] getMemoryFrom(UShort sourceAddress, int length) {
        int source = sourceAddress.intValue();
        
        if (source < 0 || source + length > ram.length) {
            throw new IllegalArgumentException();
        }
        
        byte[] buffer = new byte[length];
        System.arraycopy(ram, source, buffer, 0, length);
        
        return buffer;
    }
    
    /**
     * Returns a new state with the screen buffer set to the supplied buffer.
     * @param buffer
     * @return 
     */
    public State withScreenBuffer(ScreenBuffer buffer) {
        State state = new State(this);
        state.screen = buffer;
        
        return state;
    }
    
    /**
     * Gets the screen buffer.
     * @return The screen buffer
     */
    public ScreenBuffer getScreenBuffer() {
        return screen;
    }
    
    /**
     * Returns a new state with the given block of bytes copied at the initial
     * position of the program counter (0x200).
     * Memory and/or registers doesn't get cleared.
     * @param byteCode The program to copy
     * @return 
     */
    public State withProgram(byte[] byteCode) {
        return this.withCopiedMemory(byteCode, UShort.valueOf(0x200));
    }
    
    /**
     * Returns a new state with PC set to the given address and the current PC
     * is pushed to call stack. The stack point is also incremented.
     * @param subroutine The address to jump to.
     * @return The new state.
     */
    public State withCallTo(UShort subroutine) {
        State state = new State(this);
        state.stack[state.sp.intValue() + 1] = state.pc;
        state.sp = UByte.valueOf(state.sp.intValue() + 1);
        state.pc = subroutine;
        
        return state;
    }
    
    /**
     * Returns a new state with with PC set to the topmost item in the call
     * stack. The stack pointer is also decremetned.
     * @return The new state.
     */
    public State withReturn() {
        State state = new State(this);
        state.pc = state.stack[state.sp.intValue()];
        state.sp = UByte.valueOf(state.sp.intValue() - 1);
        
        return state;
    }
    
    public State withDelayTimer(UByte value, boolean setFlag) {
        State state = new State(this);
        state.delayTimerValue = value;
        state.delayTimerChanged = setFlag;
        
        return state;
    }
    
    public State withSoundTimer(UByte value, boolean setFlag) {
        State state = new State(this);
        state.soundTimerValue = value;
        state.delayTimerChanged = setFlag;
        
        return state;
    }
        
    public UByte getDelayTimer() {
        return delayTimerValue;
    }
    
    public UByte getSoundTimer() {
        return soundTimerValue;
    }
    
    public State asPaused() {
        State state = new State(this);
        state.executionState = ExecutionState.paused();
        
        return state;
    }
    
    public State asRunning() {
        State state = new State(this);
        state.executionState = ExecutionState.running();
        
        return state;
    }
    
    public State asWaitingForKey(Register storeIn) {
        State state = new State(this);
        state.executionState = ExecutionState.waitingForKey(storeIn);
        
        return state;
    }

    public InputState getInputState() {
        return inputState;
    }
    
    public State withClearedTimerFlags() {
        State state = new State(this);
        state.soundTimerChanged = false;
        state.delayTimerChanged = false;
        
        return state;
    }

    public boolean isDelayTimerChanged() {
        return delayTimerChanged;
    }

    public boolean isSoundTimerChanged() {
        return soundTimerChanged;
    }

    public State withInputState(InputState inputState) {
        State state = new State(this);
        state.inputState = inputState;
        
        return state;
    }
}
