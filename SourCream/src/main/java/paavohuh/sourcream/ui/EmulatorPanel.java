
package paavohuh.sourcream.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.configuration.EmulatorConfiguration;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.utils.ColorUtils;

/**
 * A panel that can display a screen buffer.
 */
public class EmulatorPanel extends JPanel {
    
    private final EmulatedLcdBuffer screen;
    private final EmulatorConfiguration config;
    
    /**
     * Creates a new emulator panel.
     * @param emulatorConfig The emulator config.
     * @param deviceConfig The device config.
     */
    public EmulatorPanel(EmulatorConfiguration emulatorConfig, DeviceConfiguration deviceConfig) {
        super(true);
        
        int scale = emulatorConfig.getScreen().getDisplayScale();
        int width = deviceConfig.getResolutionX() * scale;
        int height = deviceConfig.getResolutionY() * scale;
        
        setPreferredSize(new Dimension(width, height));
        
        this.config = emulatorConfig;
        this.screen = new EmulatedLcdBuffer(emulatorConfig, new ScreenBuffer(deviceConfig));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Color bg = config.getScreen().getColors().getBackground();
        Color fg = config.getScreen().getColors().getForeground();
        int scale = config.getScreen().getDisplayScale();
        
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
    
}
