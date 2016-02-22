package paavohuh.sourcream.emulation;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.joou.UByte;

import org.joou.UShort;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.instructions.AllInstructions;

/**
 * The virtual CPU of the device. Contains the machine state, and handles the
 * fetch-decode-execute -loop.
 */
public class Device {
    private State state;
    private final InstructionDecoder decoder;
    private final List<Consumer<ScreenBuffer>> onUpdateGraphicsHandlers;
    
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> ticker;
    
    private boolean isRunning;
    
    private static final int CLOCK_RATE = 500;
    private static final int TIMER_RATE = 60;
    
    private final Timer delayTimer;
    private final Timer soundTimer;
    private final InputState inputState;
    
    private final HashSet<Instruction> instructionSet;
    
    /**
     * Creates a new device.
     * @param state Initial state of the device.
     */
    public Device(State state) {
        InstructionCache cache = new ArrayInstructionCache();
        AllInstructions.get().forEach(cache::register);
        
        this.decoder = cache;
        this.state = state;
        this.onUpdateGraphicsHandlers = new ArrayList<>(1);
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        this.isRunning = false;
        this.ticker = null;
        this.delayTimer = new Timer(0, TIMER_RATE);
        this.soundTimer = new Timer(0, TIMER_RATE);
        this.inputState = new InputState();
        
        this.instructionSet = new HashSet<>();
    }
    
    public Device(DeviceConfiguration configuration) {
        this(new State(configuration));
    }
    
    /**
     * Runs the device for one cycle.
     * 1. Fetches an instruction
     * 2. Decodes the instruction
     * 3. Replaces current state with modified state
     * @throws paavohuh.sourcream.emulation.UnknownInstructionException
     */
    public void runCycle() throws UnknownInstructionException {
        
        /*try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        UShort code = state.get16BitsAt(state.getProgramCounter());
        Optional<Instruction> decoded = decoder.decode(code);
        
        if (!decoded.isPresent()) {
            throw new UnknownInstructionException(code);
        }
        
        Instruction instruction = decoded.get();
        //instructionSet.add(instruction);
        //System.out.println(instruction);
        
        //System.out.println(delayTimer.getValue());
        
        State preInstructionState = state
            .withIncrementedPc()
            .withInputState(inputState)
            .withDelayTimer(UByte.valueOf(delayTimer.getValue()), false)
            .withSoundTimer(UByte.valueOf(soundTimer.getValue()), false);
        
        State newState = instruction.execute(preInstructionState);
        
        ScreenBuffer buffer = newState.getScreenBuffer();
        
        if (buffer.modified) {
            updateGraphics(buffer);
        }
        
        if (newState.isDelayTimerChanged()) {
            delayTimer.setValue(newState.getDelayTimer().intValue());
        }
        
        if (newState.isSoundTimerChanged()) {
            soundTimer.setValue(newState.getSoundTimer().intValue());
        }
        
        this.state = newState.withClearedTimerFlags();
    }

    private void updateGraphics(ScreenBuffer buffer) {
        onUpdateGraphicsHandlers.stream().forEach((handler) -> {
            handler.accept(buffer);
        });
    }
    
    /**
     * Registers a new graphics handler. The caller should call .notify on the 
     * screen buffer after drawing.
     * @param handler 
     */
    public void onUpdateGraphics(Consumer<ScreenBuffer> handler) {
        onUpdateGraphicsHandlers.add(handler);
    }
    
    public void start() {
        if (this.isRunning) {
            return;
        }
        
        this.state = this.state.asRunning();
        this.delayTimer.start();
        this.soundTimer.start();
        this.ticker = scheduler.scheduleAtFixedRate(this::tryRunCycle, 0, 1000 / CLOCK_RATE, TimeUnit.MILLISECONDS);
        this.isRunning = true;
    }
    
    private void tryRunCycle() {
        try {
            runCycle();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("CPU error: " + Arrays.toString(e.getStackTrace()));
            stop();
        }
    }

    /**
     * Signals the CPU that it should stop executing instructions.
     */
    public void stop() {
        if (!this.isRunning) {
            return;
        }
        
        this.state = this.state.asPaused();
        this.delayTimer.stop();
        this.soundTimer.stop();
        this.ticker.cancel(false);
        this.isRunning = false;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }

    public void sendInput(int key, boolean keyState) {
        inputState.setKey(key, keyState);
    }
}
