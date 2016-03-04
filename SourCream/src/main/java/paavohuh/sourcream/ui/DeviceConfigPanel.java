
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
        setLayout(new BorderLayout(15, 15));
        
        JPanel clockSpeedGroup = new JPanel(new GridLayout(1, 2, 15, 15));
        
        clockSpeedGroup.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        SpinnerNumberModel clockSpeedSpinnerModel =
            new SpinnerNumberModel(
                config.getClockSpeed(),
                100,
                1000,
                100);
        
        clockSpeedGroup.add(new JLabel("Emulated clock speed"));
        
        JSpinner clockSpeedSpinner = new JSpinner(clockSpeedSpinnerModel);
        clockSpeedSpinner.addChangeListener(event -> {
            int newClockSpeed = clockSpeedSpinnerModel.getNumber().intValue();
            config.setClockSpeed(newClockSpeed);
        });
        clockSpeedGroup.add(clockSpeedSpinner);
        
        add(clockSpeedGroup, BorderLayout.NORTH);
        
        this.config = config;
    }

    public DeviceConfiguration getConfig() {
        return config;
    }
}
