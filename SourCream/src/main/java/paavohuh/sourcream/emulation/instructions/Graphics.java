package paavohuh.sourcream.emulation.instructions;

import org.jooq.lambda.Seq;
import org.joou.UByte;
import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import static paavohuh.sourcream.emulation.InstructionFactory.*;
import paavohuh.sourcream.emulation.ScreenBuffer;

/**
 * Contains graphics instructions.
 * This class shouldn't be instantiated.
 */
public final class Graphics {
    
    /**
     * Clears the screen.
     */
    public static class ClearScreen implements Instruction {

        @Override
        public State execute(State state) {
            return state.withScreenBuffer(state.getScreenBuffer().cleared());
        }

        @Override
        public UShort getCode() {
            return UShort.valueOf(0x00E0);
        }
    }
    
    /**
     * Draws the first N lines of a sprite stored at the location pointed by
     * the address register. The sprite will be drawn at (VX, VY).
     */
    public static class DrawSprite extends Instruction.WithTwoRegistersAnd4BitConstant {

        /**
         * Draws the first N lines of a sprite stored at the location pointed by
         * the address register.
         * @param x Contains the X coordinate.
         * @param y Contains the Y coordinate.
         * @param constant Number of lines to draw.
         */
        public DrawSprite(Register x, Register y, UByte constant) {
            super(x, y, constant);
        }

        @Override
        protected int getConstantOffset() {
            return 0;
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
            return UShort.valueOf(0xD000);
        }

        @Override
        public State execute(State state) {
            byte[] buffer = state.getMemoryFrom(state.getAddressRegister(), constant.intValue());
            
            ScreenBuffer sprite = new ScreenBuffer(buffer);
            ScreenBuffer modifiedScreen = state
                .getScreenBuffer()
                .blit(sprite, getRegisterX(state).intValue(), getRegisterY(state).intValue());
            
            boolean flipped = modifiedScreen.flipped;
            
            return state
                .withScreenBuffer(modifiedScreen)
                .withRegister(Register.VF, UByte.valueOf(flipped ? 1 : 0));
        }
    }
    
    /**
     * Sets the address register to the address of the character of the system
     * font equivalent to the value of a register.
     */
    public static class GetCharacterAddress extends Instruction.WithRegister {

        /**
         * Sets the address register to the address of the character of the system
         * font equivalent to the value of a register.
         * @param register The register.
         */
        public GetCharacterAddress(Register register) {
            super(register);
        }

        @Override
        protected int getRegisterOffset() {
            return 2;
        }

        @Override
        protected UShort getBaseCode() {
            return UShort.valueOf(0xF029);
        }

        @Override
        public State execute(State state) {
            // Each character takes 5 bytes in system ROM.
            // Therefore, each character can be found at RAM[VX * 5].
            int intOffset = getRegister(state).intValue() * 5;
            UShort charAddr = UShort.valueOf(intOffset);
            
            return state.withAddressRegister(charAddr);
        }
    }
    
    /**
     * Gets all graphics instructions.
     * @return A sequence of instructions.
     */
    public static Seq<Instruction> getAll() {
        return Seq.concat(
            Seq.of(new ClearScreen()),
            getAllInstances(DrawSprite::new),
            getAllInstances(GetCharacterAddress::new));
    }
}
