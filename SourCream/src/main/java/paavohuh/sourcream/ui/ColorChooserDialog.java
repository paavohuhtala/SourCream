
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

public class ColorChooserDialog extends JDialog {

    private final JColorChooser chooser;
    
    public ColorChooserDialog(Dialog owner, Color initial) {
        super(owner);
        setTitle("Color chooser");
        setModal(true);
        setResizable(false);
        setPreferredSize(new Dimension(500, 500));
        
        setLayout(new BorderLayout(20, 20));
        chooser = new JColorChooser(initial);
        
        JButton ok = new JButton("OK");
        
        ok.addActionListener(event -> {
            setVisible(false);
            dispose();
        });
        
        add(chooser, BorderLayout.CENTER);
        add(ok, BorderLayout.SOUTH);
        pack();
    }
    
    public Color getColor() {
        return chooser.getColor();
    }
    
    public Color showAsDialog() {
        this.setVisible(rootPaneCheckingEnabled);
        return getColor();
    }
}
