
package paavohuh.sourcream.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import paavohuh.sourcream.configuration.Configuration;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.configuration.EmulatorConfiguration;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.utils.ColorUtils;

/**
 * A panel that can display a screen buffer.
 */
public class EmulatorPanel extends JPanel {
    
    private final EmulatedLcdBuffer screen;
    private final Configuration config;
    
    /**
     * Creates a new emulator panel.
     * @param config The configuration.
     */
    public EmulatorPanel(Configuration config) {
        super(true);
        this.config = config;
        updateBounds();
        
        this.screen = new EmulatedLcdBuffer(config, new ScreenBuffer(config));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        EmulatorConfiguration.ScreenConfiguration screenConfig = config.getEmulatorConfig().getScreen();
        
        Color bg = screenConfig.getColors().getBackground();
        Color fg =  screenConfig.getColors().getForeground();
        int scale =  screenConfig.getDisplayScale();
        
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                float level = screen.buffer[y][x];
                g.setColor(ColorUtils.lerp(fg, bg, level));
                g.fillRect(x * scale, y * scale, scale, scale);
            }
        }
    }
    
    /**
     * Updates the screen buffer with a new one.
     * @param buffer 
     */
    public void updateScreenBuffer(ScreenBuffer buffer) {
        screen.updateWith(buffer);
        repaint();
    }
    
    /**
     * Calculates the preferred size for the panel using the screen buffer size
     * and the display scale factor.
     */
    public final void updateBounds() {
        EmulatorConfiguration.ScreenConfiguration screenConfig = config.getEmulatorConfig().getScreen();
        int scale = screenConfig.getDisplayScale();
        int width  = config.getDeviceConfig().getResolutionX() * scale;
        int height = config.getDeviceConfig().getResolutionY() * scale;
        setPreferredSize(new Dimension(width, height));
    }
}
