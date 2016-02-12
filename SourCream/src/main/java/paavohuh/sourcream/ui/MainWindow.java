
package paavohuh.sourcream.ui;

import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The main window of the emulator.
 */
public class MainWindow extends JFrame {

    public MainWindow() {        
        initComponents();
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
        
        EmulatorPanel mainCanvas = new EmulatorPanel();
        add(mainCanvas);
        pack();
        setVisible(true);
        
        this.setJMenuBar(menubar);
    }
}
