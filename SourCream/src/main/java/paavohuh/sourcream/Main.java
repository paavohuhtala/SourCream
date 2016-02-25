
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
     * The entry point
     * @param args Not used.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        DeviceConfiguration deviceConfig = ConfigurationManager.loadOrCreateDeviceConfig();
        EmulatorConfiguration emulatorConfig = ConfigurationManager.loadOrCreateEmulatorConfig();
        
        ScreenBuffer logoBuffer = Resource.getLogo();
        
        byte[] testRom = Files.readAllBytes(Paths.get("../roms/TANK"));
        
        Device device = new Device(deviceConfig);
        device.setState(device.getState().withProgram(testRom));
        
        MainWindow window = new MainWindow(emulatorConfig, deviceConfig);
        window.getEmulatorPanel().updateScreenBuffer(logoBuffer);
        
        InputMapper mapper = new InputMapper(device, emulatorConfig.getInput());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(mapper);

        EmulatorPanel emulatorPanel = window.getEmulatorPanel();
        
        device.onUpdateGraphics(emulatorPanel::updateScreenBuffer);
        device.start();
    }
}
