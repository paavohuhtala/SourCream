
package paavohuh.sourcream.ui;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import paavohuh.sourcream.configuration.ConfigurationManager;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.configuration.EmulatorConfiguration;

public class ConfigWindow extends JDialog {
    private final EmulatorConfiguration emulatorConfig;
    private final DeviceConfiguration deviceConfig;

    public ConfigWindow(Frame owner, EmulatorConfiguration emulatorConfig, DeviceConfiguration deviceConfig) throws HeadlessException {
        super(owner);
        
        this.emulatorConfig = emulatorConfig;
        this.deviceConfig = deviceConfig;
        initComponents();

    }

    private void initComponents() {
        setTitle("Configuration");
        setPreferredSize(new Dimension(500, 400));
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        JTabbedPane tabs = new JTabbedPane();
        
        EmulatorConfigPanel emulatorSettings = new EmulatorConfigPanel(this, emulatorConfig);
        DeviceSettingsPanel deviceSettings = new DeviceSettingsPanel(this, deviceConfig);
        
        tabs.addTab("Emulator", emulatorSettings);
        tabs.addTab("Device", deviceSettings);
        
        add(tabs, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(1, 3, 8, 8));
        
        JButton ok = new JButton("OK");
        ok.addActionListener(event -> {
            try {
                ConfigurationManager.saveEmulatorConfiguration(emulatorSettings.getConfig());
                ConfigurationManager.saveDeviceConfiguration(deviceSettings.getConfig());
                this.setVisible(false);
            } catch (IOException ex) { /* TODO: handle*/ }
        });
        
        JButton cancel = new JButton("Cancel");
        
        cancel.addActionListener(event -> {
            this.setVisible(false);
            this.dispose();
        });
        
        JButton apply = new JButton("Apply");
        apply.setEnabled(false);
        
        buttonPane.add(ok);
        buttonPane.add(cancel);
        buttonPane.add(apply);
        add(buttonPane, BorderLayout.SOUTH);
        
        pack();
    }
}
