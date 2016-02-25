
package paavohuh.sourcream.ui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Map;

import paavohuh.sourcream.configuration.EmulatorConfiguration.*;
import paavohuh.sourcream.emulation.Device;

/**
 * Listens for key presses globally and dispatches them to the emulated device.
 */
public class InputMapper implements KeyEventDispatcher {
    
    private final Map<Integer, Integer> bindings;
    private final Device device;

    /**
     * Creates a new input mapper.
     * @param device The device which will receive the key presses.
     * @param config The input configuration containing the key bindings.
     */
    public InputMapper(Device device, InputConfiguration config) {
        this.bindings = config.getBindings();
        this.device = device;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        
        if (!bindings.containsKey(e.getKeyCode())) {
            return false;
        }
        
        int key = bindings.get(e.getKeyCode());
        
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            device.sendInput(key, true);
            return true;
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            device.sendInput(key, false);
            return true;
        }
        
        return false;
    }
}
