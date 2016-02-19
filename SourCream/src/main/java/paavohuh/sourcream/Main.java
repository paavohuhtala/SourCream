
package paavohuh.sourcream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        ScreenBuffer logoBuffer = Resource.getLogo();
        
        byte[] rom = Files.readAllBytes(Paths.get("../", "roms", "LOGO"));
        
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
