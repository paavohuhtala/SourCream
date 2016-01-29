package paavohuh.sourcream.configuration;

public class VMConfiguration {
    public final int ramSize;
    public final int resolutionX;
    public final int resolutionY;

    public VMConfiguration(int ramSize, int resolutionX, int resolutionY) {
        this.ramSize = ramSize;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }
    
    public static VMConfiguration getDefault() {
        return new VMConfiguration(4096, 64, 32);
    }
}
