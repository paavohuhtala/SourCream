package paavohuh.sourcream.configuration;

import paavohuh.sourcream.utils.DeepCloneable;

/**
 * Configuration for the emulated device. These settings can affect emulation.
 */
public class DeviceConfiguration implements DeepCloneable<DeviceConfiguration> {
    private int clockSpeed;
    private int ramSize;
    private int resolutionX;
    private int resolutionY;

    /**
     * Creates a new device config.
     * @param clockSpeed The clock speed.
     * @param ramSize The ram size in bytes.
     * @param resolutionX The width of the display buffer.
     * @param resolutionY The height of the display buffer.
     */
    public DeviceConfiguration(int clockSpeed, int ramSize, int resolutionX, int resolutionY) {
        this.clockSpeed = clockSpeed;
        this.ramSize = ramSize;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }
    
    public static DeviceConfiguration getDefault() {
        return new DeviceConfiguration(500, 4096, 64, 32);
    }

    public int getClockSpeed() {
        return clockSpeed;
    }

    public int getRamSize() {
        return ramSize;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setClockSpeed(int clockSpeed) {
        this.clockSpeed = clockSpeed;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    @Override
    public DeviceConfiguration cloned() {
        return new DeviceConfiguration(clockSpeed, ramSize, resolutionX, resolutionY);
    }
}
