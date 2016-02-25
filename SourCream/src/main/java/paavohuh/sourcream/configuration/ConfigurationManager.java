
package paavohuh.sourcream.configuration;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;

/**
 * Provides functionality for loading and saving configuration files.
 */
public class ConfigurationManager {
    
    private static final Gson GSON = new Gson();
    
    private static final String CONFIGURATION_DIRECTORY = "config/";
    private static final String DEVICE_CONFIGURATION_FILE = "device.json";
    private static final String EMULATOR_CONFIGURATION_FILE = "emulator.json";
    
    /**
     * Loads or creates the device config.
     * @return The config.
     * @throws IOException 
     */
    public static DeviceConfiguration loadOrCreateDeviceConfig() throws IOException {
        Path path = Paths.get(CONFIGURATION_DIRECTORY, DEVICE_CONFIGURATION_FILE);
        
        return loadOrCreateConfig(path, DeviceConfiguration::getDefault, DeviceConfiguration.class);
    }
    
    /**
     * Loads or creates the emulator config.
     * @return The config.
     * @throws IOException 
     */
    public static EmulatorConfiguration loadOrCreateEmulatorConfig() throws IOException {
        Path path = Paths.get(CONFIGURATION_DIRECTORY, EMULATOR_CONFIGURATION_FILE);
        
        return loadOrCreateConfig(path, EmulatorConfiguration::getDefault, EmulatorConfiguration.class);
    }
    
    private static <T> T loadOrCreateConfig(Path path, Supplier<T> defaulSupplier, Class<T> configType) throws IOException {
        T config;
        
        if (!Files.exists(path)) {
            config = defaulSupplier.get();
            serializeToFile(path.toFile(), config);
        } else {
            String json = FileUtils.readFileToString(path.toFile());
            config = GSON.fromJson(json, configType);
        }
        
        return config;
    }
    
    /**
     * Saves the device config.
     * @param config The confg.
     * @throws IOException 
     */
    public static void saveDeviceConfiguration(DeviceConfiguration config) throws IOException {
        File target = new File(CONFIGURATION_DIRECTORY, DEVICE_CONFIGURATION_FILE);
        serializeToFile(target, config);
    }
    
    /**
     * Saves the emulator config.
     * @param config The config.
     * @throws IOException 
     */
    public static void saveEmulatorConfiguration(EmulatorConfiguration config) throws IOException {
        File target = new File(CONFIGURATION_DIRECTORY, EMULATOR_CONFIGURATION_FILE);
        serializeToFile(target, config);
    }
    
    private static void serializeToFile(File file, Object o) throws IOException {
        FileUtils.writeStringToFile(file, GSON.toJson(o));
    }
}
