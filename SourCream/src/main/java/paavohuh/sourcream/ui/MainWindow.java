
package paavohuh.sourcream.ui;

import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import paavohuh.sourcream.configuration.EmulatorConfiguration;

/**
 * The main window of the emulator.
 */
public class MainWindow extends JFrame {
    
    private EmulatorPanel emulatorPanel;
    private EmulatorConfiguration config;

    public MainWindow(EmulatorConfiguration config) {
        this.config = config;
        initComponents();
    }

    public EmulatorPanel getEmulatorPanel() {
        return emulatorPanel;
    }
    
    private void initComponents() {
        setTitle("SourCream");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        
        JMenuItem item = new JMenuItem("Load ROM...");
        item.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(this);
        });
        menu.add(item);
        
        menu.addSeparator();
        
        item = new JMenuItem("Exit");
        item.addActionListener(event -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        menu.add(item);
        
        menubar.add(menu);
        
        emulatorPanel = new EmulatorPanel(config);
        add(emulatorPanel);
        pack();
        setVisible(true);
        
        this.setJMenuBar(menubar);
    }
}
