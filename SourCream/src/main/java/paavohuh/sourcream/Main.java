
package paavohuh.sourcream;

import java.awt.KeyboardFocusManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import paavohuh.sourcream.configuration.*;
import paavohuh.sourcream.emulation.*;
import paavohuh.sourcream.ui.*;

/**
 * The entry point for SourCream.
 */
public class Main {
    /**
     * The entry point.
     * @param args Not used.
     * @throws IOException if loading a ROM fails.
     */
    public static void main(String[] args) throws IOException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        DeviceConfiguration deviceConfig = ConfigurationManager.loadOrCreateDeviceConfig();
        EmulatorConfiguration emulatorConfig = ConfigurationManager.loadOrCreateEmulatorConfig();
        
        byte[] testRom = Files.readAllBytes(Paths.get("../roms/TANK"));
        
        MainWindow window = new MainWindow(emulatorConfig, deviceConfig);
        window.loadProgram(testRom);
        window.start();
    }
}
