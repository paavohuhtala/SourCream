
package paavohuh.sourcream.ui;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
    private JMenuItem pauseContinueItem;
    private final Configuration config;
    
    private final State[] saveStates;
    private final JMenuItem[] loadStateItems;

    /**
     * Creates a new main window.
     * @param config The configuration.
     */
    public MainWindow(Configuration config) {
        this.config = config;
        this.device = new Device(config);
        this.saveStates = new State[8];
        this.loadStateItems = new JMenuItem[8];
        
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
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
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
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(event -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        fileMenu.add(item);
        menubar.add(fileMenu);
        
        JMenu emulationMenu = new JMenu("Emulation");
        pauseContinueItem = new JMenuItem("Not running");
        pauseContinueItem.setAccelerator(KeyStroke.getKeyStroke("P"));
        pauseContinueItem.setEnabled(false);
        pauseContinueItem.addActionListener(event -> {
            if (device.isRunning()) {
                pause();
            } else {
                start();
            }
        });
        emulationMenu.add(pauseContinueItem);
        menubar.add(emulationMenu);
        
        JMenu settingsMenu = new JMenu("Options");
        item = new JMenuItem("Configuration...");
        item.addActionListener(event -> {
            boolean wasRunning = device.isRunning();
            
            device.stop();
            ConfigWindow window = new ConfigWindow(this, config);
            window.setVisible(true);
            emulatorPanel.updateBounds();
            pack();
            
            if (wasRunning) {
                device.start();
            }
        });
        settingsMenu.add(item);
        menubar.add(settingsMenu);
        
        JMenu saveStateMenu = new JMenu("Save states");
        
        for (int i = 1; i <= saveStates.length; i++) {
            final int index = i;
            JMenuItem saveItem = new JMenuItem("Save to slot " + i);
            saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1 + (i - 1), 0));
            saveItem.addActionListener(event -> {
                saveStates[index - 1] = device.getState();
                loadStateItems[index - 1].setEnabled(true);
            });
            saveStateMenu.add(saveItem);
        }
        
        saveStateMenu.addSeparator();
        
        for (int i = 1; i <= saveStates.length; i++) {
            final int index = i;
            JMenuItem loadItem = new JMenuItem("Load slot " + i);
            loadItem.setEnabled(false);
            loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1 + (i - 1), InputEvent.CTRL_DOWN_MASK));
            loadItem.addActionListener(event -> {
                device.setState(saveStates[index - 1]);
            });
            saveStateMenu.add(loadItem);
            loadStateItems[i - 1] = loadItem;
        }
        
        menubar.add(saveStateMenu);
        
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
        pauseContinueItem.setEnabled(true);
        start();
    }
    
    /**
     * Starts executing the Chip-8 VM.
     */
    public void start() {
        device.start();
        pauseContinueItem.setText("Pause");

    }
    
    /**
     * Pauses the Chip-8 VM.
     */
    public void pause() {
        device.stop();
        pauseContinueItem.setText("Continue");
    }
}
