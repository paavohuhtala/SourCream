
package paavohuh.sourcream.ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

    public MainWindow() {
        initComponents();
    }

    private void initComponents() {
        setSize(640, 320);
        setTitle("SourCream");
        setBackground(java.awt.Color.GREEN);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
