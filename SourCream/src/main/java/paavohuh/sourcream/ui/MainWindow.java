
package paavohuh.sourcream.ui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.apache.commons.io.FileUtils;
import paavohuh.sourcream.*;
import paavohuh.sourcream.configuration.*;
import paavohuh.sourcream.emulation.*;

/**
 * The main window of the emulator.
 */
public class MainWindow extends JFrame {
    
    private final Device device;
    private EmulatorPanel emulatorPanel;
    private final EmulatorConfiguration emulatorConfig;
    private final DeviceConfiguration deviceConfig;

    /**
     * Creates a new main window.
     * @param emulatorConfig The emulator configuration.
     * @param deviceConfig The device configuration.
     */
    public MainWindow(EmulatorConfiguration emulatorConfig, DeviceConfiguration deviceConfig) {
        this.emulatorConfig = emulatorConfig;
        this.deviceConfig = deviceConfig;
        this.device = new Device(deviceConfig);
        
        InputMapper mapper = new InputMapper(device, emulatorConfig);
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(mapper);
        
        initComponents();
        setDefaultBuffer();
        device.onUpdateGraphics(emulatorPanel::updateScreenBuffer);
    }

    public EmulatorPanel getEmulatorPanel() {
        return emulatorPanel;
    }
    
    private void initComponents() {
        setTitle("SourCream");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem item = new JMenuItem("Load ROM...");
        item.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("../roms"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    loadProgram(FileUtils.readFileToByteArray(chooser.getSelectedFile()));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Loading ROM " + chooser.getSelectedFile().getName() + " failed.",
                        "Error while loading ROM", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(item);
        
        fileMenu.addSeparator();
        
        item = new JMenuItem("Exit");
        item.addActionListener(event -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        fileMenu.add(item);
        menubar.add(fileMenu);
        
        JMenu settingsMenu = new JMenu("Options");
        
        item = new JMenuItem("Configuration...");
        item.addActionListener(event -> {
            ConfigWindow window = new ConfigWindow(this, emulatorConfig, deviceConfig);
            window.setVisible(true);
        });
        settingsMenu.add(item);
        menubar.add(settingsMenu);
        add(menubar, BorderLayout.NORTH);
        
        emulatorPanel = new EmulatorPanel(emulatorConfig, deviceConfig);
        add(emulatorPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    private void setDefaultBuffer() {
        emulatorPanel.updateScreenBuffer(Resource.getLogo());
    }
    
    /**
     * Loads a program to the Chip-8 VM.
     * @param program 
     */
    public void loadProgram(byte[] program) {
        device.stop();  
        device.setState(new State(deviceConfig).withProgram(program));
        start();
    }
    
    /**
     * Starts executing the Chip-8 VM.
     */
    public void start() {
        device.setState(device.getState().asRunning());
        device.start();
    }
}
