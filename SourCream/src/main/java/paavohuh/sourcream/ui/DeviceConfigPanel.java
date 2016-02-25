
package paavohuh.sourcream.ui;

import java.awt.Dialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import paavohuh.sourcream.configuration.DeviceConfiguration;

/**
 * The panel for the device configuration.
 */
class DeviceConfigPanel extends JPanel {
    private final DeviceConfiguration config;

    /**
     * Creates a new device configuration panel.
     * @param parent The parent.
     * @param config The device configuration.
     */
    public DeviceConfigPanel(Dialog parent, DeviceConfiguration config) {
        add(new JLabel("These settings can and will affect emulation."));
        this.config = config;
    }

    public DeviceConfiguration getConfig() {
        return config;
    }
}
