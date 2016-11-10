package com.gluonhq.plugin.down;

import java.util.Optional;
import java.util.stream.Stream;

public enum Plugin {
    
    // TODO: Add proper description to each plugin
    
    ACCELEROMETER("accelerometer", "Accelerometer Service. Requires Lifecycle service"),
    BARCODE_SCANER("barcode-scan", "Barcode Scan Service"),
    BATTERY("battery", "Battery Service. Requires Lifecycle service"),
    BLE("ble", "Bluetooth Low Energy Service"),
    BROWSER("browser", "Browser Service"),
    CACHE("cache", "Cache Service"),
    COMPASS("compass", "Compass Service. Requires Magnetometer service"),
    CONNECTIVITY("connectivity", "Connectivity Service. Requires Lifecycle service"),
    DEVICE("device", "Device Service"),
    DIALER("dialer", "Dialer Service"),
    DISPLAY("display", "Display Service. Required by Gluon Charm"),
    LIFECYCLE("lifecycle", "Lifecycle Service. Required by Gluon Charm"),
    LOCAL_NOTIFICATIONS("local-notifications", "Local Notifications Service. Requires Runtime Args service"),
    MAGNETOMETER("magnetometer", "Magnetometer Service. Requires Lifecycle service"),
    ORIENTATION("orientation", "Orientation Service. Requires Lifecycle service"),
    PICTURES("picture", "Pictures Service"),
    POSITION("position", "Position Service. Requires Lifecycle service"),
    RUNTIME_ARGS("runtime-args", "Runtime Arguments Service"),
    SETTINGS("settings", "Settings Service"),
    STATUSBAR("statusbar", "StatusBar Service. Required by Gluon Charm"),
    STORAGE("storage", "Storage Service. Required by Gluon Charm"),
    VIBRATION("vibration", "Vibration Service");
    
    private final String name;
    private final String description;

    Plugin(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public static Optional<Plugin> byName(String name) {
        return Stream.of(values())
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }
}
