
package paavohuh.sourcream.configuration;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import static paavohuh.sourcream.configuration.EmulatorConfiguration.*;

/**
 * Contains premade input configurations for known applications and use cases.
 */
public class KnownBindings {
    /**
     * Returns the premade input configuration for Tetris.
     * Left and right arrow keys are used for movement. Up arrow is used for 
     * rotating the tetromino. Down arrow is used for accelerating the fall.
     * @return An input configuration
     */
    public static InputConfiguration tetris() {
        HashMap<Integer, Integer> bindings = new HashMap<>();
        
        bindings.put(KeyEvent.VK_LEFT, 5);
        bindings.put(KeyEvent.VK_RIGHT, 6);
        bindings.put(KeyEvent.VK_UP, 4);
        bindings.put(KeyEvent.VK_DOWN, 7);
        
        return new InputConfiguration(bindings);
    }
    
    /**
     * Returns the premade input configuration for 2-player Pong.
     * Player 1 uses W and S for movement, player 2 uses up and down arrow keys.
     * @return An input configuration
     */
    public static InputConfiguration pong() {
        HashMap<Integer, Integer> bindings = new HashMap<>();
        
        bindings.put(KeyEvent.VK_W, 1);
        bindings.put(KeyEvent.VK_S, 4);
        bindings.put(KeyEvent.VK_UP, 12);
        bindings.put(KeyEvent.VK_DOWN, 13);
            
        return new InputConfiguration(bindings);
    }
    
    /**
     * Returns an input configuration with every button bound to something.
     * Keys 0 - 9 are bound to Q - P on a standard WASD keyboard. Keys 10 - 15
     * are bound to A - H.
     * This configuration is useful if you don't know which keys should be
     * mapped.
     * @return 
     */
    public static InputConfiguration allBound() {
        HashMap<Integer, Integer> bindings = new HashMap<>();
        
        bindings.put(KeyEvent.VK_Q, 0);
        bindings.put(KeyEvent.VK_W, 1);
        bindings.put(KeyEvent.VK_E, 2);
        bindings.put(KeyEvent.VK_R, 3);
        bindings.put(KeyEvent.VK_T, 4);
        bindings.put(KeyEvent.VK_Y, 5);
        bindings.put(KeyEvent.VK_U, 6);
        bindings.put(KeyEvent.VK_I, 7);
        bindings.put(KeyEvent.VK_O, 8);
        bindings.put(KeyEvent.VK_P, 9);
        bindings.put(KeyEvent.VK_A, 10);
        bindings.put(KeyEvent.VK_S, 11);
        bindings.put(KeyEvent.VK_D, 12);
        bindings.put(KeyEvent.VK_F, 13);
        bindings.put(KeyEvent.VK_G, 14);
        bindings.put(KeyEvent.VK_H, 15);
        
        return new InputConfiguration(bindings);
    }
}
