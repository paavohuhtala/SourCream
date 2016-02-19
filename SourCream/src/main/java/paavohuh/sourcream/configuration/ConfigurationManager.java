
package paavohuh.sourcream.configuration;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;

public class ConfigurationManager {
    
    private static final Gson GSON = new Gson();
    
    private static final String CONFIGURATION_DIRECTORY = "config/";
    private static final String DEVICE_CONFIGURATION_FILE = "device.json";
    private static final String EMULATOR_CONFIGURATION_FILE = "emulator.json";
    
    public static DeviceConfiguration loadOrCreateDeviceConfig() throws IOException {
        Path path = Paths.get(CONFIGURATION_DIRECTORY, DEVICE_CONFIGURATION_FILE);
        
        return loadOrCreateConfig(path, DeviceConfiguration::getDefault, DeviceConfiguration.class);
    }
    
    public static EmulatorConfiguration loadorCreateEmulatorConfig() throws IOException {
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
    
    public static void saveDeviceConfiguration(DeviceConfiguration config) throws IOException {
        File target = new File(CONFIGURATION_DIRECTORY, DEVICE_CONFIGURATION_FILE);
        serializeToFile(target, config);
    }
    
    public static void saveEmulatorConfiguration(EmulatorConfiguration config) throws IOException {
        File target = new File(CONFIGURATION_DIRECTORY, EMULATOR_CONFIGURATION_FILE);
        serializeToFile(target, config);
    }
    
    private static void serializeToFile(File file, Object o) throws IOException {
        FileUtils.writeStringToFile(file, GSON.toJson(o));
    }
}
