
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import paavohuh.sourcream.configuration.EmulatorConfiguration;
import paavohuh.sourcream.configuration.KnownBindings;
import paavohuh.sourcream.utils.MapUtils;

/**
 * The emulator config panel.
 */
public class EmulatorConfigPanel extends JPanel {

    private final EmulatorConfiguration config;
    private List<JComboBox<NamedKey>> keyBindBoxes;
    
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
        JComboBox presetBox = new JComboBox(new String[]{"Custom", "Pong", "Tetris"});
        
        presetBox.addActionListener(event -> {
            String selected = (String) presetBox.getModel().getSelectedItem();
            
            switch (selected) {
                case "Pong":
                    config.setInput(KnownBindings.pong());
                    break;
                case "Tetris":
                    config.setInput(KnownBindings.tetris());
                    break;
            }
            
            updateKeyBinds();
        });
        
        bindingsGroup.add(presetBox, BorderLayout.NORTH);
        
        JPanel subBindingsGroup = new JPanel(new GridLayout(8, 2, 4, 4));
        
        List<NamedKey> namedKeyList = NamedKey.getKnown();
        NamedKey[] keys = namedKeyList.toArray(new NamedKey[namedKeyList.size()]);

        keyBindBoxes = new ArrayList<>(16);
        
        for (int i = 0; i < 16; i++) {
            final int index = i;
            
            JPanel bindingPanel = new JPanel(new BorderLayout(4, 0));
            bindingPanel.add(new JLabel(Integer.toString(i + 1)), BorderLayout.WEST);
            
            JComboBox<NamedKey> keyBindBox = new JComboBox(keys);
            keyBindBox.setPreferredSize(new Dimension(80, 0));
            keyBindBox.addItemListener(event -> {
                NamedKey key = (NamedKey) event.getItem();
                if (!key.isNone()) {
                    config.getInput().bind(key.getKeyCode(), index);
                }
            });
            
            bindingPanel.add(keyBindBox, BorderLayout.EAST);
            subBindingsGroup.add(bindingPanel);
            keyBindBoxes.add(keyBindBox);
        }
        
        updateKeyBinds();
        
        bindingsGroup.add(subBindingsGroup, BorderLayout.CENTER);
        rightColumn.add(bindingsGroup, BorderLayout.CENTER);
        
        JPanel ghostingGroup = new JPanel(new GridLayout(3, 2, 4, 4));
        ghostingGroup.setBorder(BorderFactory.createTitledBorder("Ghosting"));
        
        JCheckBox enableGhosting = new JCheckBox("Enable?");
        
        enableGhosting.setSelected(config.getScreen().getEmulateGhosting());
        enableGhosting.addActionListener(event -> {
            config.getScreen().setEmulateGhosting(enableGhosting.isSelected());
        });
        
        ghostingGroup.add(enableGhosting);
        ghostingGroup.add(new JLabel());
        
        SpinnerModel fadeInModel = new SpinnerNumberModel(
            config.getScreen().getGhosting().getAddBy(), 0.0, 1.0, 0.1);
        fadeInModel.addChangeListener(event -> {
            double addBy = (double) fadeInModel.getValue();
            config.getScreen().getGhosting().setAddBy((float) addBy);
        });
        
        SpinnerModel fadeOutModel = new SpinnerNumberModel(
            config.getScreen().getGhosting().getSubtractBy(), 0.0, 1.0, 0.1);
        fadeOutModel.addChangeListener(event -> {
            double subBy = (double) fadeOutModel.getValue();
            config.getScreen().getGhosting().setSubtractBy((float) subBy);
        });
        
        JSpinner fadeIn = new JSpinner(fadeInModel);
        JSpinner fadeOut = new JSpinner(fadeOutModel);
        
        ghostingGroup.add(new JLabel("Fade in"));
        ghostingGroup.add(fadeIn);
        ghostingGroup.add(new JLabel("Fade out"));
        ghostingGroup.add(fadeOut);

        leftColumn.add(ghostingGroup);
        
        JPanel scaleGroup = new JPanel(new GridLayout(1, 2));
        scaleGroup.add(new JLabel("Scale factor"));
        SpinnerModel scaleModel = new SpinnerNumberModel(
            config.getScreen().getDisplayScale(), 1, 50, 1);
        scaleModel.addChangeListener(event -> {
            int scale = (int) scaleModel.getValue();
            config.getScreen().setScaleFactor(scale);
        });
        scaleGroup.add(new JSpinner(scaleModel));
        leftColumn.add(scaleGroup);
                
        add(leftColumn);
        add(rightColumn);
    }

    public EmulatorConfiguration getConfig() {
        return config;
    }
    
    private void updateKeyBinds() {
        Map<Integer, Integer> invertedBindings = MapUtils.invert(config.getInput().getBindings());

        for (int i = 0; i < keyBindBoxes.size(); i++) {
            NamedKey key;

            if (invertedBindings.containsKey(i)) {
                System.out.println(i);
                key = NamedKey.fromCode(invertedBindings.get(i)).get();
            } else {
                key = NamedKey.none();
            }
            
            keyBindBoxes.get(i).setSelectedItem(key);
        }
    }
}
