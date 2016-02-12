package paavohuh.sourcream.configuration;

/**
 * Configuration for the emulated device.
 */
public class DeviceConfiguration {
    public final int ramSize;
    public final int resolutionX;
    public final int resolutionY;

    public DeviceConfiguration(int ramSize, int resolutionX, int resolutionY) {
        this.ramSize = ramSize;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }
    
    public static DeviceConfiguration getDefault() {
        return new DeviceConfiguration(4096, 64, 32);
    }
}
