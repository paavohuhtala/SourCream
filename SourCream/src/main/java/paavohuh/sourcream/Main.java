
package paavohuh.sourcream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import paavohuh.sourcream.ui.MainWindow;

/**
 * The entry point for SourCream.
 */
public class Main {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, ex);
            // TODO: do something
        }
        
        MainWindow window = new MainWindow();
    }
}
