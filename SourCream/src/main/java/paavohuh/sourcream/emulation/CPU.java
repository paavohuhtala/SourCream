package paavohuh.sourcream.emulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import org.joou.UShort;

/**
 * The virtual CPU of the device. Contains the machine state, and handles the
 * fetch-decode-execute -loop.
 */
public class CPU implements Runnable {
    private State state;
    private final InstructionDecoder decoder;
    private final List<Consumer<ScreenBuffer>> onUpdateGraphicsHandlers;
    
    public CPU(InstructionDecoder decoder, State state) {
        this.decoder = decoder;
        this.state = state;
        this.onUpdateGraphicsHandlers = new ArrayList<>(1);
   }
    
    /**
     * Runs the CPU for one cycle.
     * 1. Fetches an instruction
     * 2. Decodes the instruction
     * 3. Replaces current state with modified state
     * @throws paavohuh.sourcream.emulation.UnknownInstructionException
     * @throws java.lang.InterruptedException
     */
    public void runCycle() throws UnknownInstructionException, InterruptedException {
        UShort code = state.get16BitsAt(state.getProgramCounter());
        Optional<Instruction> decoded = decoder.decode(code);
        
        if (!decoded.isPresent()) {
            throw new UnknownInstructionException(code);
        }
        
        Instruction instuction = decoded.get();
        State newState = instuction.execute(state.withIncrementedPc());
        
        ScreenBuffer buffer = newState.getScreenBuffer();
        
        if (buffer.modified) {
            updateGraphics(buffer);
        }
        
        this.state = newState;
        
        Thread.sleep(20);
    }

    private void updateGraphics(ScreenBuffer buffer) {
        for (Consumer<ScreenBuffer> handler : onUpdateGraphicsHandlers) {
            handler.accept(buffer);
        }
    }
    
    /**
     * Registers a new graphics handler. The caller should call .notify on the 
     * screen buffer after drawing.
     * @param handler 
     */
    public void onUpdateGraphics(Consumer<ScreenBuffer> handler) {
        onUpdateGraphicsHandlers.add(handler);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                runCycle();
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("CPU error: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
