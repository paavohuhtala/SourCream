
package paavohuh.sourcream.ui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Map;

import paavohuh.sourcream.configuration.EmulatorConfiguration.*;
import paavohuh.sourcream.emulation.Device;

public class InputMapper implements KeyEventDispatcher {
    
    private final Map<Integer, Integer> mappings;
    private final Device device;

    public InputMapper(Device device, InputConfiguration mapping) {
        this.mappings = mapping.getBindings();
        this.device = device;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        
        if (!mappings.containsKey(e.getKeyCode())) {
            return false;
        }
        
        int key = mappings.get(e.getKeyCode());
        
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
