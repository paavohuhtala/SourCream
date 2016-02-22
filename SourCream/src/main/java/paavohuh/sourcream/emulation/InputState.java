
package paavohuh.sourcream.emulation;

import paavohuh.sourcream.utils.ArrayUtils;
import paavohuh.sourcream.utils.DeepCloneable;

public class InputState implements DeepCloneable<InputState> {
    private final boolean[] keyStates;

    public InputState() {
        this.keyStates = new boolean[16];
    }
    
    public InputState(InputState previous) {
        this.keyStates = ArrayUtils.clone(previous.keyStates);
    }
    
    public void setKey(int i, boolean state) {
        if (i < 0 || i >= keyStates.length) {
            throw new IllegalArgumentException();
        }
        
        keyStates[i] = state;
    }
    
    public boolean getKey(int i) {
        if (i < 0 || i>= keyStates.length) {
            throw new IllegalArgumentException();
        }
        
        return keyStates[i];
    }

    @Override
    public InputState cloned() {
        return new InputState(this);
    }
}
