package paavohuh.sourcream.emulation.instructions;

import org.joou.UShort;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.State;

/**
 * Contains graphics instructions.
 * This class shouldn't be instantiated.
 */
public final class Graphics {
    
    /**
     * Clears the screen.
     */
    public class ClearScreen implements Instruction {

        @Override
        public State execute(State state) {
            return state.withScreenBuffer(state.getScreenBuffer().cleared());
        }

        @Override
        public UShort getCode() {
            return UShort.valueOf(0x00E0);
        }
    }
}
