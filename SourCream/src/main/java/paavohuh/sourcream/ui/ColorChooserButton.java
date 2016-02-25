
package paavohuh.sourcream.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.function.Consumer;
import javax.swing.JButton;
import paavohuh.sourcream.utils.ColorUtils;

/**
 * A control for selecting colors. Creates a ColorChooserDialog on keypress.
 */
public final class ColorChooserButton extends JButton {
    private Color color;

    /**
     * Creates a new color chooser button.
     * @param parent The parent.
     * @param label The label of the button.
     * @param initialColor The initial color.
     */
    public ColorChooserButton(Dialog parent, String label, Color initialColor) {
        //this.setPreferredSize(new Dimension(64, 64));
        this.setMinimumSize(new Dimension(64, 32));
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

    /**
     * Sets the color.
     * @param color The color.
     */
    public void setColor(Color color) {
        this.color = color;
        this.setBackground(color);
        this.setForeground(ColorUtils.invert(color));
    }
    
    /**
     * Register a new callback for checking when the color is changed.
     * @param func 
     */
    public void onChangeColor(Consumer<Color> func) {
        addChangeListener(event -> {
            func.accept(getColor());
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
        g.setColor(getForeground());
        
        FontMetrics metrics = g.getFontMetrics();
        int textHeight = metrics.getHeight();
        int textWidth = metrics.stringWidth(getText());
        
        g.drawString(getText(), getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 2);
    }
}
