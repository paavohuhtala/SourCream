
package paavohuh.sourcream.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.configuration.EmulatorConfiguration;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.utils.ColorUtils;
import paavohuh.sourcream.utils.MathUtils;

/**
 * A panel that can display a screen buffer.
 */
public class EmulatorPanel extends JPanel {
    
    private final EmulatedLcdBuffer screen;
    private final EmulatorConfiguration config;
    
    public EmulatorPanel(EmulatorConfiguration config) {
        super(true);
        setPreferredSize(new Dimension(64 * config.screen.scaleFactor, 32 * config.screen.scaleFactor));
        
        this.config = config;
        this.screen = new EmulatedLcdBuffer(config, new ScreenBuffer(DeviceConfiguration.getDefault()));
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(config.screen.backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                float level = screen.buffer[y][x];
                
                g.setColor(ColorUtils.lerp(config.screen.foregroundColor, config.screen.backgroundColor, level));
                g.fillRect(x * config.screen.scaleFactor, y * config.screen.scaleFactor, config.screen.scaleFactor, config.screen.scaleFactor);
            }
        }
    }
    
    public void updateScreenBuffer(ScreenBuffer buffer) {
        screen.updateWith(buffer);
        repaint();
    }
    
}
