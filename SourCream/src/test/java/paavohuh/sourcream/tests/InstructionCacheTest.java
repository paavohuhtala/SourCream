
package paavohuh.sourcream.tests;

import java.util.Optional;
import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;

import paavohuh.sourcream.emulation.ArrayInstructionCache;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.instructions.Arithmetic;
import paavohuh.sourcream.emulation.instructions.Bitwise;
import paavohuh.sourcream.emulation.instructions.Control;
import paavohuh.sourcream.emulation.instructions.Graphics;
import paavohuh.sourcream.emulation.instructions.Transfer;

public class InstructionCacheTest {

    @Test
    public void canCreateInstance() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
    }
    
    @Test
    public void canCacheBitwise() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Bitwise.getAll().forEach(cache::register);
    }
    
        
    @Test
    public void canCacheTransfer() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Transfer.getAll().forEach(cache::register);
    }
    
    @Test
    public void canCacheArithmetic() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Arithmetic.getAll().forEach(cache::register);
    }
    
    @Test
    public void canCacheControl() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Control.getAll().forEach(cache::register);
    }
    
        
    @Test
    public void canCacheGraphics() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Graphics.getAll().forEach(cache::register);
    }
    
    @Test
    public void cantRegisterCodesOver64K() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        try {
            cache.register(new DummyInstruction(UShort.MAX_VALUE + 1));
            Assert.fail("Could register an instruction with code over USHORT.MAX");
        } catch (IllegalArgumentException e) { }
    }
    
    @Test
    public void cantRegisterNegativeCodes() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        try {
            cache.register(new DummyInstruction(-1));
            Assert.fail("Could register an instruction with code less than zero");
        } catch (IllegalArgumentException e) { }
    }
    
    @Test
    public void cantRegisterCodeTwice() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        try {
            cache.register(new DummyInstruction(1));
            cache.register(new DummyInstruction(1));
            Assert.fail("Could register two instructions with the same code");
        } catch (IllegalArgumentException e) { }
    }
    
    @Test
    public void canRegisterAllValidCodes() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        
        for (int i = 0; i <= UShort.MAX_VALUE; i++) {
            cache.register(new DummyInstruction(i));
        }
    }
    
    @Test
    public void findsValidCode() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Instruction instr = new DummyInstruction(0x1010);
        cache.register(instr);
        Optional<Instruction> fetcted = cache.decode(UShort.valueOf(0x1010));
        Assert.assertTrue("Instruction was found", fetcted.isPresent());
        Assert.assertEquals(instr, fetcted.get());
    }
    
    @Test
    public void doesntFindMissingCode() {
        ArrayInstructionCache cache = new ArrayInstructionCache();
        Optional<Instruction> fetched = cache.decode(UShort.valueOf(0x0001));
        Assert.assertTrue("Instruction was not found", !fetched.isPresent());
    }
    
    private class DummyInstruction implements Instruction {

        private final int code;

        public DummyInstruction(int code) {
            this.code = code;
        }
        
        @Override
        public State execute(State state) {
            return state;
        }

        @Override
        public UShort getCode() {
            return UShort.valueOf(code);
        }
        
    }
}
