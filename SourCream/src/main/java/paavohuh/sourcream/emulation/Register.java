package paavohuh.sourcream.emulation;

/*
 * Represents an immutable register id.
 */
public class Register {
    public final int id;
    
    public Register(int offset) {
        if (offset < 0 || offset >= 16) {
            throw new IllegalArgumentException("Register offset must be between 0 and 15.");
        }
        this.id = offset;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Register other = (Register) obj;
        return this.id == other.id;
    }
    
    @Override
    public String toString() {
        return String.format("V%01X", id);
    }

    // Shorthands for registers
    public static final Register V0 = new Register(0);
    public static final Register V1 = new Register(1);
    public static final Register V2 = new Register(2);
    public static final Register V3 = new Register(3);
    public static final Register V4 = new Register(4);
    public static final Register V5 = new Register(5);
    public static final Register V6 = new Register(6);
    public static final Register V7 = new Register(7);
    public static final Register V8 = new Register(8);
    public static final Register V9 = new Register(9);
    public static final Register VA = new Register(10);
    public static final Register VB = new Register(11);
    public static final Register VC = new Register(12);
    public static final Register VD = new Register(13);
    public static final Register VE = new Register(14);
    
    // All of these are the same register; aliases are supplied for improevd
    // readability.
    public static final Register VF = new Register(15);
    public static final Register CARRY = VF;
    public static final Register BORROW = VF;
}
