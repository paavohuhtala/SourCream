
package paavohuh.sourcream.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.function.Consumer;
import javax.swing.JButton;
import paavohuh.sourcream.utils.ColorUtils;

public final class ColorChooserButton extends JButton {
    private Color color;

    public ColorChooserButton(Dialog parent, String label, Color initialColor) {
        this.setPreferredSize(new Dimension(64, 64));
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setText(label);

        setColor(initialColor);
        
       this.addActionListener(event -> {
           ColorChooserDialog chooser = new ColorChooserDialog(parent, color);
           setColor(chooser.showAsDialog());
       });
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setBackground(color);
        this.setForeground(ColorUtils.invert(color));
    }
    
    public void onChangeColor(Consumer<Color> func) {
        addChangeListener(event -> {func.accept(getColor());});
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
    }
}
