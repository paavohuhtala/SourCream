
package paavohuh.sourcream.ui;

import java.awt.Dialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import paavohuh.sourcream.configuration.DeviceConfiguration;

class DeviceSettingsPanel extends JPanel {
    private final DeviceConfiguration config;

    public DeviceSettingsPanel(Dialog parent, DeviceConfiguration config) {
        add(new JLabel("These settings can and will affect emulation."));
        this.config = config;
    }

    public DeviceConfiguration getConfig() {
        return config;
    }
}
