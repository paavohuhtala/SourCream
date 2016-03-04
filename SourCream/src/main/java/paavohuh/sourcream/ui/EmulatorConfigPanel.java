
package paavohuh.sourcream.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
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
    private final List<JComboBox<NamedKey>> keyBindBoxes;
    private final Integer[] keyBindings;
    
    /**
     * Creates a new emulator config panel.
     * @param parent The parent. 
     * @param config The emulator configuration.
     */
    public EmulatorConfigPanel(Dialog parent, EmulatorConfiguration config) {
        this.config = config;
        
        setLayout(new GridLayout(1, 2, 4, 4));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 1.0f;
        
        JPanel leftColumn = new JPanel(new GridBagLayout());
        JPanel screenGroup = new JPanel(new GridBagLayout());
        
        screenGroup.setBorder(BorderFactory.createTitledBorder("Screen"));
        
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
        
        c.weighty = 0.7;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        
        screenGroup.add(colorGroup, c);
        
        JPanel bindingsGroup = new JPanel(new BorderLayout(4, 4));
        JComboBox presetBox = new JComboBox(new String[]{"Custom", "Pong", "Tetris"});
        
        presetBox.addActionListener(event -> {
            String selected = (String) presetBox.getModel().getSelectedItem();
            
            switch (selected) {
                case "Pong":
                    updateConfigBindings(KnownBindings.pong());
                    break;
                case "Tetris":
                    updateConfigBindings(KnownBindings.tetris());
                    break;
            }
            
            updateDisplayedBindings();
        });
        
        bindingsGroup.add(presetBox, BorderLayout.NORTH);
        
        JPanel subBindingsGroup = new JPanel(new GridLayout(8, 2, 4, 4));
        
        List<NamedKey> namedKeyList = NamedKey.getKnown();
        NamedKey[] keys = namedKeyList.toArray(new NamedKey[namedKeyList.size()]);

        keyBindBoxes = new ArrayList<>(16);
        keyBindings = new Integer[16];
        
        for (int i = 0; i < 16; i++) {
            final int index = i;
            
            JPanel bindingPanel = new JPanel(new BorderLayout(4, 0));
            bindingPanel.add(new JLabel(Integer.toString(i + 1)), BorderLayout.WEST);
            
            JComboBox<NamedKey> keyBindBox = new JComboBox(keys);
            keyBindBox.setPreferredSize(new Dimension(80, 0));
            keyBindBox.addItemListener(event -> {
                NamedKey key = (NamedKey) event.getItem();
                keyBindings[index] = key.getKeyCode();
                updateConfigBindings();
            });
            
            bindingPanel.add(keyBindBox, BorderLayout.EAST);
            subBindingsGroup.add(bindingPanel);
            keyBindBoxes.add(keyBindBox);
        }
        
        updateDisplayedBindings();
        
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

        c.weighty = 0.5;
        c.gridy = 2;
        c.gridheight = 2;
        
        screenGroup.add(ghostingGroup, c);
        
        JPanel scaleGroup = new JPanel(new GridLayout(1, 2));
        scaleGroup.add(new JLabel("Scale factor"));
        SpinnerModel scaleModel = new SpinnerNumberModel(
            config.getScreen().getDisplayScale(), 1, 50, 1);
        scaleModel.addChangeListener(event -> {
            int scale = (int) scaleModel.getValue();
            config.getScreen().setScaleFactor(scale);
        });
        scaleGroup.add(new JSpinner(scaleModel));
        
        c.weighty = 0.8;
        c.gridy = 4;
        c.gridheight = 1;
        
        screenGroup.add(scaleGroup, c);
        
        c.weighty = 0.6f;
        c.gridy = 0;
        c.gridheight = 4;
        
        leftColumn.add(screenGroup, c);
        
        JPanel soundGroup = new JPanel(new GridLayout(1, 1));
        soundGroup.setBorder(BorderFactory.createTitledBorder("Sound"));
        JCheckBox enableBox = new JCheckBox("Enable?", config.getSound().isEnabled());
        enableBox.addActionListener(event -> {
            config.getSound().setEnabled(enableBox.isSelected());
        });
        soundGroup.add(enableBox);
        
        c.weighty = 0.4f;
        c.gridy = 4;
        c.gridheight = 1;
        
        leftColumn.add(soundGroup, c);
        
        c.weighty = 1.0f;
        c.gridy = 5;
        leftColumn.add(new JPanel(), c);
        
        add(leftColumn);
        add(rightColumn);
    }

    public EmulatorConfiguration getConfig() {
        return config;
    }
    
    private void updateDisplayedBindings() {
        Map<Integer, Integer> invertedBindings = MapUtils.invert(config.getInput().getBindings());

        for (int i = 0; i < keyBindBoxes.size(); i++) {
            NamedKey key;

            if (invertedBindings.containsKey(i)) {
                key = NamedKey.fromCode(invertedBindings.get(i)).get();
            } else {
                key = NamedKey.none();
            }
            
            keyBindBoxes.get(i).setSelectedItem(key);
        }
    }
    
    private void updateConfigBindings() {
        config.getInput().unbindAll();
        
        for (int i = 0; i < keyBindings.length; i++) {
            if (keyBindings[i] != null) {
                config.getInput().bind(keyBindings[i], i);
            } else {
                config.getInput().unbindDeviceKey(i);
            }
        }
    }
    
    private void updateConfigBindings(EmulatorConfiguration.InputConfiguration config) {
        Arrays.fill(keyBindings, null);
        
        Map<Integer, Integer> invertedBindings = MapUtils.invert(config.getBindings());
        for (int i = 0; i < keyBindings.length; i++) {
            if (invertedBindings.containsKey(i)) {
                keyBindings[i] = invertedBindings.get(i);
            } else {
                keyBindings[i] = null;
            }
        }
        
        updateConfigBindings();
    }
}
