
package paavohuh.sourcream.ui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Map;
import paavohuh.sourcream.configuration.Configuration;

import paavohuh.sourcream.emulation.Device;

/**
 * Listens for key presses globally and dispatches them to the emulated device.
 */
public class InputMapper implements KeyEventDispatcher {
    
    private final Device device;
    private final Configuration config;

    /**
     * Creates a new input mapper.
     * @param device The device which will receive the key presses.
     * @param config The emulator configuration containing the key bindings.
     */
    public InputMapper(Device device, Configuration config) {
        this.config = config;
        this.device = device;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        
        Map<Integer, Integer> bindings = config.getEmulatorConfig().getInput().getBindings();
        
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
