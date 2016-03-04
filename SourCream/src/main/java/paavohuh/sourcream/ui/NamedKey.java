
package paavohuh.sourcream.ui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A named key, consisting of a key code and a name.
 */
public class NamedKey {
    private final Integer keyCode;
    private final String name;
    private final boolean isNone;

    /**
     * Creates a new named key.
     * @param keyCode The Swing virtual keycode.
     * @param name The human-friendly name of the key.
     */
    public NamedKey(int keyCode, String name) {
        this.keyCode = keyCode;
        this.name = name;
        this.isNone = false;
    }
    
    private NamedKey() {
        this.isNone = true;
        this.keyCode = null;
        this.name = "None";
    }
    
    /**
     * Gets a list of all known named keys.
     * @return A list of named keys.
     */
    public static List<NamedKey> getKnown() {
        List<NamedKey> keys = new ArrayList<>();
        
        keys.add(none());
        keys.add(new NamedKey(KeyEvent.VK_W, "W"));
        keys.add(new NamedKey(KeyEvent.VK_A, "A"));
        keys.add(new NamedKey(KeyEvent.VK_S, "S"));
        keys.add(new NamedKey(KeyEvent.VK_D, "D"));
        
        keys.add(new NamedKey(KeyEvent.VK_UP, "Up"));
        keys.add(new NamedKey(KeyEvent.VK_LEFT, "Left"));
        keys.add(new NamedKey(KeyEvent.VK_DOWN, "Down"));
        keys.add(new NamedKey(KeyEvent.VK_RIGHT, "Right"));
        
        keys.add(new NamedKey(KeyEvent.VK_Z, "Z"));
        keys.add(new NamedKey(KeyEvent.VK_X, "X"));
        keys.add(new NamedKey(KeyEvent.VK_SPACE, "Space"));
        keys.add(new NamedKey(KeyEvent.VK_ENTER, "Enter"));
        
        return keys;
    }
    
    /**
     * Returns an unbound key. 
     * @return An unbound key.
     */
    public static NamedKey none() {
        return new NamedKey();
    }
    
    /**
     * Gets a NamedKey from a virtual key code.
     * @param code The virtual key code.
     * @return A NamedKey if it exists.
     */
    public static Optional<NamedKey> fromCode(int code) {
        return getKnown().stream()
            .filter(k -> k.keyCode != null && k.keyCode == code)
            .findFirst();
    }

    public Integer getKeyCode() {
        return keyCode;
    }

    public String getName() {
        return name;
    }

    public boolean isNone() {
        return isNone;
    }
    
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.keyCode);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + (this.isNone ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamedKey other = (NamedKey) obj;
        if (!Objects.equals(this.keyCode, other.keyCode)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.isNone != other.isNone) {
            return false;
        }
        return true;
    }
}
