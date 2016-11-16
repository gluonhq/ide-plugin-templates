package com.gluonhq.plugin.down;

import java.util.Optional;
import java.util.stream.Stream;

public enum Plugin {
    
    // TODO: Add proper description to each plugin
    // TODO: Enable RAS when available in Down
    
    ACCELEROMETER("accelerometer", "Accelerometer Service. Requires Lifecycle service", "AccelerometerService.html"),
    BARCODE_SCANER("barcode-scan", "Barcode Scan Service", "BarcodeScanService.html"),
    BATTERY("battery", "Battery Service. Requires Lifecycle service", "BatteryService.html"),
    BLE("ble", "Bluetooth Low Energy Service", "BleService.html"),
    BROWSER("browser", "Browser Service", "BrowserService.html"),
    CACHE("cache", "Cache Service", "CacheService.html"),
    COMPASS("compass", "Compass Service. Requires Magnetometer service", "CompassService.html"),
    CONNECTIVITY("connectivity", "Connectivity Service. Requires Lifecycle service", "ConnectivityService.html"),
    DEVICE("device", "Device Service", "DeviceService.html"),
    DIALER("dialer", "Dialer Service", "DialerService.html"),
    DISPLAY("display", "Display Service. Required by Gluon Charm", "DisplayService.html"),
    LIFECYCLE("lifecycle", "Lifecycle Service. Required by Gluon Charm", "LifecycleService.html"),
    LOCAL_NOTIFICATIONS("local-notifications", "Local Notifications service", "LocalNotificationsService.html"),
//    LOCAL_NOTIFICATIONS("local-notifications", "Local Notifications Service. Requires Runtime Args service", "LocalNotificationsService.html"),
    MAGNETOMETER("magnetometer", "Magnetometer Service. Requires Lifecycle service", "MagnetometerService.html"),
    ORIENTATION("orientation", "Orientation Service. Requires Lifecycle service", "OrientationService.html"),
    PICTURES("picture", "Pictures Service", "PicturesService.html"),
    POSITION("position", "Position Service. Requires Lifecycle service", "PositionService.html"),
//    RUNTIME_ARGS("runtime-args", "Runtime Arguments Service", "RuntimeArgsService.html"),
    SETTINGS("settings", "Settings Service", "SettingsService.html"),
    STATUSBAR("statusbar", "StatusBar Service. Required by Gluon Charm", "StatusBarService.html"),
    STORAGE("storage", "Storage Service. Required by Gluon Charm", "StorageService.html"),
    VIBRATION("vibration", "Vibration Service", "VibrationService.html");
    
    private final String name;
    private final String description;
    private final String url;
    
    Plugin(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
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
    
    public static Optional<Plugin> byName(String name) {
        return Stream.of(values())
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }
}
