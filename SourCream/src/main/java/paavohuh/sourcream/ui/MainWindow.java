
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
    private final Configuration config;

    /**
     * Creates a new main window.
     * @param config The configuration.
     */
    public MainWindow(Configuration config) {
        this.config = config;
        this.device = new Device(config);
        
        InputMapper mapper = new InputMapper(device, config);
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
            device.stop();
            ConfigWindow window = new ConfigWindow(this, config);
            window.setVisible(true);
            device.start();
        });
        
        settingsMenu.add(item);
        menubar.add(settingsMenu);
        add(menubar, BorderLayout.NORTH);
        
        emulatorPanel = new EmulatorPanel(config);
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
        device.setState(new State(config).withProgram(program));
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
