
package paavohuh.sourcream.ui;

import java.awt.Dialog;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import paavohuh.sourcream.configuration.EmulatorConfiguration;

public class EmulatorConfigPanel extends JPanel {

    private final EmulatorConfiguration config;
    
    public EmulatorConfigPanel(Dialog parent, EmulatorConfiguration config) {
        this.config = config;
        
        setLayout(new GridLayout(2, 0));
        add(new JLabel("These settings change how information is displayed."));
        add(new JTextField("oispa"));
        ColorChooserButton bg = new ColorChooserButton(parent, "Background", config.getScreen().getColors().getBackground());
        ColorChooserButton fg = new ColorChooserButton(parent, "Foreground", config.getScreen().getColors().getForeground());
        
        bg.onChangeColor(color -> {
            config.getScreen().getColors().setBackground(color);
        });
        
        fg.onChangeColor(color -> {
            config.getScreen().getColors().setForeground(color);
        });
        
        add(bg);
        add(fg);
    }

    public EmulatorConfiguration getConfig() {
        return config;
    }
}
