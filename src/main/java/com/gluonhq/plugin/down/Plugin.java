package com.gluonhq.plugin.down;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum Plugin {
    
    // TODO: Add proper description to each plugin
    // TODO: Enable RAS when available in Down
    
    ACCELEROMETER("accelerometer", "Accelerometer Service. Requires Lifecycle service", "AccelerometerService.html", "lifecycle"),
    BARCODE_SCANER("barcode-scan", "Barcode Scan Service", "BarcodeScanService.html"),
    BATTERY("battery", "Battery Service. Requires Lifecycle service", "BatteryService.html", "lifecycle"),
    BLE("ble", "Bluetooth Low Energy Service", "BleService.html"),
    BROWSER("browser", "Browser Service", "BrowserService.html"),
    CACHE("cache", "Cache Service", "CacheService.html"),
    COMPASS("compass", "Compass Service. Requires Magnetometer service", "CompassService.html", "magnetometer", "lifecycle"),
    CONNECTIVITY("connectivity", "Connectivity Service. Requires Lifecycle service", "ConnectivityService.html", "lifecycle"),
    DEVICE("device", "Device Service", "DeviceService.html"),
    DIALER("dialer", "Dialer Service", "DialerService.html"),
    DISPLAY("display", "Display Service. Required by Gluon Charm", "DisplayService.html"),
    LIFECYCLE("lifecycle", "Lifecycle Service. Required by Gluon Charm", "LifecycleService.html"),
    LOCAL_NOTIFICATIONS("local-notifications", "Local Notifications Service. Requires Runtime Args service", "LocalNotificationsService.html", "runtime-args"),
    MAGNETOMETER("magnetometer", "Magnetometer Service. Requires Lifecycle service", "MagnetometerService.html", "lifecycle"),
    ORIENTATION("orientation", "Orientation Service. Requires Lifecycle service", "OrientationService.html", "lifecycle"),
    PICTURES("picture", "Pictures Service", "PicturesService.html"),
    POSITION("position", "Position Service. Requires Lifecycle service", "PositionService.html", "lifecycle"),
    RUNTIME_ARGS("runtime-args", "Runtime Arguments Service", "RuntimeArgsService.html"),
    SETTINGS("settings", "Settings Service", "SettingsService.html"),
    STATUSBAR("statusbar", "StatusBar Service. Required by Gluon Charm", "StatusBarService.html"),
    STORAGE("storage", "Storage Service. Required by Gluon Charm", "StorageService.html"),
    VIBRATION("vibration", "Vibration Service", "VibrationService.html");
    
    private final String name;
    private final String description;
    private final String url;
    private final List<String> dependencies;
    
    Plugin(String name, String description, String url, String... dependencies) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.dependencies = Arrays.asList(dependencies);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public String getUrl() {
        return url;
    }

    public List<String> getDependencies() {
        return dependencies;
    }
    
    public static Optional<Plugin> byName(String name) {
        return Stream.of(values())
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }
}
