
package paavohuh.sourcream.emulation;

import java.util.Optional;
import paavohuh.sourcream.utils.ArrayUtils;
import paavohuh.sourcream.utils.DeepCloneable;

/**
 * Represents the mutable input state of the device.
 */
public class InputState implements DeepCloneable<InputState> {
    private final boolean[] keyStates;

    /**
     * Creates a new input state. Every key starts 
     */
    public InputState() {
        this.keyStates = new boolean[16];
    }
    
    public InputState(InputState previous) {
        this.keyStates = ArrayUtils.clone(previous.keyStates);
    }
    
    /**
     * Sets key state of a key.
     * @param i The index of they key (between 0 and 15).
     * @param state Is the button pressed or not?
     */
    public void setKey(int i, boolean state) {
        if (i < 0 || i >= keyStates.length) {
            throw new IllegalArgumentException();
        }
        
        keyStates[i] = state;
    }
    
    /**
     * Gets the state of a key.
     * @param i The index of the key (between 0 and 15).
     * @return True if the key is pressed, false if not.
     */
    public boolean getKey(int i) {
        if (i < 0 || i>= keyStates.length) {
            throw new IllegalArgumentException();
        }
        
        return keyStates[i];
    }

    /**
     * If any buttons are down, returns the key with the lowest index. If no
     * keys are down, returns an empty option.
     * @return An optional key index
     */
    public Optional<Integer> isAKeyDown() {
        for (int i = 0; i < keyStates.length; i++) {
            if (keyStates[i]) {
                return Optional.of(i);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public InputState cloned() {
        return new InputState(this);
    }
}
