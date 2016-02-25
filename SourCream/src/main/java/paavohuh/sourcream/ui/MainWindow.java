
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.configuration.EmulatorConfiguration;

/**
 * The main window of the emulator.
 */
public class MainWindow extends JFrame {
    
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

        initComponents();
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
            chooser.showOpenDialog(this);
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
}
