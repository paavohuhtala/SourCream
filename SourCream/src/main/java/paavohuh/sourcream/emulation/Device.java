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
    private final DeviceConfiguration configuration;
    
    private State state;
    private final InstructionDecoder decoder;
    private final List<Consumer<ScreenBuffer>> onUpdateGraphicsHandlers;
    
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> ticker;
    
    private boolean isRunning;
        
    private final Timer delayTimer;
    private final Timer soundTimer;
    private final InputState inputState;
    
    // REMOVE BEFORE RELEASE
    private final HashSet<Instruction> instructionSet;
    private final List<Instruction> instructions;
    
    private static final int TIMER_RATE = 60;
    
    /**
     * Creates a new device with initial state.
     * @param configuration The configuration for the device.
     * @param state Initial state of the device.
     */
    public Device(DeviceConfiguration configuration, State state) {
        InstructionCache cache = new ArrayInstructionCache();
        AllInstructions.get().forEach(cache::register);
        
        this.decoder = cache;
        this.configuration = configuration;
        this.state = state;
        this.onUpdateGraphicsHandlers = new ArrayList<>(1);
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        this.isRunning = false;
        this.ticker = null;
        this.delayTimer = new Timer(0, TIMER_RATE);
        this.soundTimer = new Timer(0, TIMER_RATE);
        this.inputState = new InputState();
        
        // REMOVE BEFORE RELEASE
        this.instructionSet = new HashSet<>();
        this.instructions = new ArrayList<>();
    }
    
    /**
     * Creates a new device. The state is initialized to the default.
     * @param configuration The configuration for the device.
     */
    public Device(DeviceConfiguration configuration) {
        this(configuration, new State(configuration));
    }
    
    /**
     * Runs the device for one cycle.
     * 1. Fetches an instruction
     * 2. Decodes the instruction
     * 3. Replaces current state with modified state
     * @throws paavohuh.sourcream.emulation.UnknownInstructionException
     */
    public void runCycle() throws UnknownInstructionException {
        
        // If the state paused, do nothing.
        if (state.getExecutionState() == ExecutionState.PAUSED) {
            return;
        }
        
        // If we are waiting for a key, check if a key is down. If it is,
        // put it in the register and continue. Otherwise, continue doing
        // nothing.
        if (state.getExecutionState() == ExecutionState.WAITING_FOR_KEY) {
            delayTimer.stop();
            soundTimer.stop();
            
            Optional<Integer> key = inputState.isAKeyDown();
            
            if (!key.isPresent()) {
                return;
            }
            
            delayTimer.start();
            soundTimer.start();
            
            // The register should always be present in this state.
            Register reg = state.getStoreKeyAfterHaltRegister().get();
            state = state.withRegister(reg, UByte.valueOf(key.get()));
        }
        
        // Fetch an instruction from the RAM where the program counter points.
        UShort code = state.get16BitsAt(state.getProgramCounter());
        Optional<Instruction> decoded = decoder.decode(code);
        
        // If decoding an instruction fails, throw an exception.
        if (!decoded.isPresent()) {
            throw new UnknownInstructionException(code);
        }
        
        Instruction instruction = decoded.get();
        System.out.println(state.getProgramCounter() + ": " + instruction);
        
        // REMOVE BEFORE RELEASE
        /*instructionSet.add(instruction);
        instructions.add(instruction);*/

        // Before executing the instruction:
        // 1. Increment the program counter
        // 2. Update current input state
        // 3. Update the timers
        State preInstructionState = state
            .withIncrementedPc()
            .withInputState(inputState)
            .withDelayTimer(UByte.valueOf(delayTimer.getValue()), false)
            .withSoundTimer(UByte.valueOf(soundTimer.getValue()), false);
        
        State newState = instruction.execute(preInstructionState);
        
        // After executing the instruction, fetch the new screen buffer.
        ScreenBuffer buffer = newState.getScreenBuffer();
        
        // If the buffer has been modified, notify handlers.
        if (buffer.modified) {
            updateGraphics(buffer);
        }
        
        // If the timers should be synchronized, do so.
        
        if (newState.shouldDelayTimerBeSynced()) {
            delayTimer.setValue(newState.getDelayTimer().intValue());
        }
        
        if (newState.shouldSoundTimerBeSynced()) {
            soundTimer.setValue(newState.getSoundTimer().intValue());
        }
        
        // Clear the timer flags, and assign the modified state as the current
        // one.
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
    
    /**
     * Starts the device.
     * Sets the execution state as running, starts the timers and tells the 
     * scheduler to start executing instructions at the configured rate.
     */
    public void start() {
        if (this.isRunning) {
            return;
        }
        
        this.state = this.state.asRunning();
        this.delayTimer.start();
        this.soundTimer.start();
        this.ticker = scheduler.scheduleAtFixedRate(this::tryRunCycle, 0, 1000 / configuration.getClockSpeed(), TimeUnit.MILLISECONDS);
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

    /**
     * Sends key input to the device.
     * @param key The key index (between 0 and 15).
     * @param keyState The state of the key.
     */
    public void sendInput(int key, boolean keyState) {
        inputState.setKey(key, keyState);
    }
}
