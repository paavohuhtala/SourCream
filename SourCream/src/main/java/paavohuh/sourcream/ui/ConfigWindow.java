
package paavohuh.sourcream.ui;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import paavohuh.sourcream.configuration.*;

/**
 * The main configuration window of the program.
 */
public class ConfigWindow extends JDialog {
    private final Configuration config;
    private ConfigWindowResult result;

    /**
     * Creates a new modal configuration window.
     * @param owner The owner of the window.
     * @param config The configuration.
     * @throws HeadlessException 
     */
    public ConfigWindow(Frame owner, Configuration config) throws HeadlessException {
        super(owner);
        
        this.config = config;
        this.result = ConfigWindowResult.CANCEL;
        
        initComponents();
    }

    private void initComponents() {
        setTitle("Configuration");
        setPreferredSize(new Dimension(500, 500));
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        JTabbedPane tabs = new JTabbedPane();
        
        EmulatorConfigPanel emulatorSettings = new EmulatorConfigPanel(this, config.getEmulatorConfig());
        DeviceConfigPanel deviceSettings = new DeviceConfigPanel(this, config.getDeviceConfig());
        
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
                this.result = ConfigWindowResult.OK;
                this.setVisible(false);
                this.dispose();
            } catch (IOException ex) { /* TODO: handle*/ }
        });
        
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(event -> {
            this.result = ConfigWindowResult.CANCEL;
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
    
    public Configuration showDialog() {
        this.setVisible(true);
        
        if (result == ConfigWindowResult.OK) {
            return config;
        }
        
        return null;
    }
    
    public enum ConfigWindowResult {
        OK,
        CANCEL
    }
}
