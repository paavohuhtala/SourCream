package paavohuh.sourcream.emulation;

import org.joou.UByte;
import org.joou.UShort;

import paavohuh.sourcream.configuration.VMConfiguration;
import paavohuh.sourcream.utils.ArrayUtils;

/**
 * Represents the state of the system. Exposes a fluent API, which is the only
 * way to mutate the state from outside.
 */
public class State implements Cloneable {
    // 4K (by default) of system RAM
    private byte[] ram;
    
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
    private UByte delayTimer;
    private UByte soundTimer;
    
    /**
     * Copy constructor. Doesn't modify the state at all.
     * @param previous The previous state.
     */
    public State(State previous) {
        this.ram = ArrayUtils.clone(previous.ram);
        this.registers = ArrayUtils.clone(previous.registers);
        this.addressRegister = previous.addressRegister;
        this.pc = previous.pc;
        this.sp = previous.sp;
        this.stack = ArrayUtils.clone(previous.stack);
        this.delayTimer = previous.delayTimer;
        this.soundTimer = previous.soundTimer;
    }
    
    public State(VMConfiguration config) {
        this.ram = new byte[config.ramSize];
        this.registers = new UByte[16];
        this.addressRegister = UShort.valueOf(0);
        this.pc = UShort.valueOf(0x200);
        this.sp = UByte.valueOf(0);
        this.stack = new UShort[16];
        this.delayTimer = UByte.valueOf(0);
        this.soundTimer = UByte.valueOf(0);
    }
    
    public UByte register(Register reg) {
        return registers[reg.id];
    }
    
    public State withRegister(Register reg, UByte value) {
        State state = new State(this);
        state.registers[reg.id] = value;
        
        return state;
    }
}
