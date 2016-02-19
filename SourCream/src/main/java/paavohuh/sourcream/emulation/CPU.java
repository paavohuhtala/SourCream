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
    private boolean shouldRun;
    
    /**
     * Creates a new CPU.
     * @param decoder
     * @param state 
     */
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
     */
    public void runCycle() throws UnknownInstructionException {
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
        shouldRun = true;
        
        try {
            while (shouldRun) {
                runCycle();
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("CPU error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Signals the CPU that it should stop executing instructions.
     */
    public void stop() {
        this.shouldRun = false;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
}
