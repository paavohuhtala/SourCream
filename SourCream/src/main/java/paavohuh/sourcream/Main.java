
package paavohuh.sourcream;

import java.io.IOException;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import paavohuh.sourcream.configuration.*;
import paavohuh.sourcream.emulation.*;
import paavohuh.sourcream.ui.*;

/**
 * The entry point for SourCream.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        ScreenBuffer logoBuffer = Resource.getLogo();
        
        // JMP 0x200
        byte[] nopRom = new byte[] {0x12, 0x00};
        
        Device device = new Device(DeviceConfiguration.getDefault(), nopRom);
        
        MainWindow window = new MainWindow(EmulatorConfiguration.getDefault());
        window.getEmulatorPanel().updateScreenBuffer(logoBuffer);

        EmulatorPanel emulatorPanel = window.getEmulatorPanel();
        
        device.onUpdateGraphics(emulatorPanel::updateScreenBuffer);
        device.start();
    }
}
