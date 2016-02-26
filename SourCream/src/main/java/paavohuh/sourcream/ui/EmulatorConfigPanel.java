
package paavohuh.sourcream.ui;


import java.awt.*;
import javax.swing.*;
import paavohuh.sourcream.configuration.EmulatorConfiguration;

/**
 * The emulator config panel.
 */
public class EmulatorConfigPanel extends JPanel {

    private final EmulatorConfiguration config;
    
    /**
     * Creates a new emulator config panel.
     * @param parent The parent. 
     * @param config The emulator configuration.
     */
    public EmulatorConfigPanel(Dialog parent, EmulatorConfiguration config) {
        this.config = config;
        
        setLayout(new GridLayout(1, 2, 4, 4));
        
        JPanel leftColumn = new JPanel(new GridLayout(4, 1));
        leftColumn.setBorder(BorderFactory.createTitledBorder("Screen"));
        JPanel rightColumn = new JPanel(new BorderLayout());
        rightColumn.setBorder(BorderFactory.createTitledBorder("Input"));
        
        JPanel colorGroup = new JPanel(new GridLayout(1, 2, 15, 15));
        colorGroup.setBorder(BorderFactory.createTitledBorder("Colors"));
        
        ColorChooserButton bg = new ColorChooserButton(parent, "Background", config.getScreen().getColors().getBackground());
        ColorChooserButton fg = new ColorChooserButton(parent, "Foreground", config.getScreen().getColors().getForeground());
        
        bg.onChangeColor(color -> {
            config.getScreen().getColors().setBackground(color);
        });
        
        fg.onChangeColor(color -> {
            config.getScreen().getColors().setForeground(color);
        });
        
        colorGroup.add(bg);
        colorGroup.add(fg);
        leftColumn.add(colorGroup);
        
        JPanel bindingsGroup = new JPanel(new BorderLayout(4, 4));
        bindingsGroup.add(new JComboBox(new String[]{"Custom", "Pong", "Tetris"}), BorderLayout.NORTH);
        JPanel subBindingsGroup = new JPanel(new GridLayout(8, 4, 4, 4));
        
        String[] keys = {"None", "Up", "Down", "Left", "Right", "W", "A", "S", "D"};
        
        for (int i = 0; i < 16; i++) {
            subBindingsGroup.add(new JLabel(Integer.toString(i + 1)));
            subBindingsGroup.add(new JComboBox(keys));
        }
        
        bindingsGroup.add(subBindingsGroup, BorderLayout.CENTER);
        
        rightColumn.add(bindingsGroup, BorderLayout.CENTER);
        
        JPanel ghostingGroup = new JPanel(new GridLayout(3, 2, 4, 4));
        ghostingGroup.setBorder(BorderFactory.createTitledBorder("Ghosting"));
        ghostingGroup.add(new JCheckBox("Enable?"));
        ghostingGroup.add(new JLabel());
        
        SpinnerModel fadeInModel = new SpinnerNumberModel(
            config.getScreen().getGhosting().getAddBy(), 0.1, 1.0, 0.1);
        JSpinner fadeIn = new JSpinner(fadeInModel);
        JSpinner fadeOut = new JSpinner();
        
        ghostingGroup.add(new JLabel("Fade in"));
        ghostingGroup.add(fadeIn);
        ghostingGroup.add(new JLabel("Fade out"));
        ghostingGroup.add(fadeOut);

        leftColumn.add(ghostingGroup);
        
        JPanel scaleGroup = new JPanel(new GridLayout(1, 2));
        scaleGroup.add(new JLabel("Scale factor"));
        scaleGroup.add(new JSpinner(new SpinnerNumberModel(10, 1, 50, 1)));
        leftColumn.add(scaleGroup);
                
        add(leftColumn);
        add(rightColumn);
    }

    public EmulatorConfiguration getConfig() {
        return config;
    }
}
