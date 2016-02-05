
package paavohuh.sourcream.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class EmulatorPanel extends JPanel {
    
    public EmulatorPanel() {
        super(true);
        setPreferredSize(new Dimension(640, 320));
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
