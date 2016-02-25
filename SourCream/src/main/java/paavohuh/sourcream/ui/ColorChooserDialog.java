
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

/**
 * A modal dialog, containing a color chooser.
 */
public class ColorChooserDialog extends JDialog {

    private final JColorChooser chooser;

    /**
     * Creates a new modal color chooser dialog.
     * @param owner The owner of the dialog.
     * @param initial The initial color of the chooser.
     */
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
    
    /**
     * Makes the panel visible, asks for a color and returns it after the panel
     * is closed.
     * @return The color.
     */
    public Color showAsDialog() {
        setVisible(true);
        return getColor();
    }
}
