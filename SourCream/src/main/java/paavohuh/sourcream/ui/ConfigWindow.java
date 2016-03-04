
package paavohuh.sourcream.ui;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import paavohuh.sourcream.configuration.*;

/**
 * The main configuration window of the program.
 */
public class ConfigWindow extends JDialog {
    private final Configuration globalConfig;
    private final Configuration modifiedConfig;
    private DeviceConfigPanel deviceSettings;
    private EmulatorConfigPanel emulatorSettings;
    
    /**
     * Creates a new modal configuration window.
     * @param owner The owner of the window.
     * @param config The configuration.
     * @throws HeadlessException 
     */
    public ConfigWindow(Frame owner, Configuration config) throws HeadlessException {
        super(owner);
        
        this.globalConfig = config;
        this.modifiedConfig = globalConfig.cloned();
        
        initComponents();
    }

    private void initComponents() {
        setTitle("Configuration");
        setPreferredSize(new Dimension(500, 500));
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        JTabbedPane tabs = new JTabbedPane();
        
        emulatorSettings = new EmulatorConfigPanel(this, modifiedConfig.getEmulatorConfig());
        deviceSettings = new DeviceConfigPanel(this, modifiedConfig.getDeviceConfig());
        
        tabs.addTab("Emulator", emulatorSettings);
        tabs.addTab("Device", deviceSettings);
        
        add(tabs, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(1, 3, 8, 8));
        
        JButton ok = new JButton("OK");
        ok.addActionListener(event -> {
            try {
                applyConfig();
                saveConfig();
                this.setVisible(false);
                this.dispose();
            } catch (IOException ex) { /* TODO: handle*/ }
        });
        
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(event -> {
            this.setVisible(false);
            this.dispose();
        });
        
        JButton apply = new JButton("Apply");
        cancel.addActionListener(event -> {
            applyConfig();
            try {
                applyConfig();
                saveConfig();
            } catch (IOException ex) { /* TODO: handle */ }
        });
        
        buttonPane.add(ok);
        buttonPane.add(cancel);
        buttonPane.add(apply);
        add(buttonPane, BorderLayout.SOUTH);
        
        pack();
    }
    
    private void applyConfig() {
        this.globalConfig.setEmulatorConfig(emulatorSettings.getConfig());
        this.globalConfig.setDeviceConfig(deviceSettings.getConfig());
    }
    
    private void saveConfig() throws IOException {
        ConfigurationManager.saveEmulatorConfiguration(modifiedConfig.getEmulatorConfig());
        ConfigurationManager.saveDeviceConfiguration(modifiedConfig.getDeviceConfig());
    }
}
