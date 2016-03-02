
package paavohuh.sourcream.configuration;

import paavohuh.sourcream.utils.DeepCloneable;

/**
 * Contains both the Device and the Emulator configuration.
 */
public class Configuration implements DeepCloneable<Configuration> {
    private DeviceConfiguration deviceConfig;
    private EmulatorConfiguration emulatorConfig;

    /**
     * Createas a new configuration.
     * @param deviceConfig The device configuration.
     * @param emulatorConfig  The emulator configuration.
     */
    public Configuration(DeviceConfiguration deviceConfig, EmulatorConfiguration emulatorConfig) {
        this.deviceConfig = deviceConfig;
        this.emulatorConfig = emulatorConfig;
    }

    public DeviceConfiguration getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfiguration deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public EmulatorConfiguration getEmulatorConfig() {
        return emulatorConfig;
    }

    public void setEmulatorConfig(EmulatorConfiguration emulatorConfig) {
        this.emulatorConfig = emulatorConfig;
    }

    @Override
    public Configuration cloned() {
        return new Configuration(deviceConfig.cloned(), emulatorConfig.cloned());
    }
    
    /**
     * Gets the default configuration.
     * @return The default configuration.
     */
    public static Configuration getDefault() {
        return new Configuration(DeviceConfiguration.getDefault(), EmulatorConfiguration.getDefault());
    }
}
