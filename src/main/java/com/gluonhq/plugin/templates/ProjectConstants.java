package com.gluonhq.plugin.templates;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class ProjectConstants {

    public static final String PLUGIN_VERSION = "2.4.0";
    private static final String GLUON_DESKTOP_VERSION = "1.1.0";
    private static final String GLUON_MOBILE_VERSION = "3.0.0";
    private static final String GLUON_DOWN_VERSION = "3.0.0";
    private static final String GLUON_MOBILE_PLUGIN = "1.0.9";

    public static final String DEFAULT_PROJECT_NAME = "GluonApplication";
    public static final String DEFAULT_PACKAGE_NAME = "com.gluonapplication";

    // Optin
    public static final String PARAM_USER_IDE_OPTIN = "gluon_ide_optin";
    public static final String PARAM_USER_EMAIL = "gluon_user_email";
    public static final String PARAM_USER_UPTODATE = "gluon_user_uptodate";
    public static final String PARAM_USER_LICENSE_MOBILE = "gluon_user_license_mobile";
    public static final String PARAM_USER_LICENSE_DESKTOP = "gluon_user_license_desktop";
    public static final String PARAM_USER_MAC_ADDRESS = "gluon_user_mac_address";
    public static final String PARAM_USER_PLUGIN_VERSION = "gluon_user_plugin_version";

    public static final String PARAM_PROJECT_NAME = "projectName";
    public static final String PARAM_PROJECT_DIR = "projectDir";
    public static final String PARAM_PACKAGE_NAME = "packageName";
    public static final String PARAM_PACKAGE_FOLDER = "packageFolder";
    public static final String PARAM_MAIN_CLASS = "mainClass";
    public static final String PARAM_MAIN_CLASS_NAME = "mainClassName";
    public static final String PARAM_ANDROID_ENABLED = "androidEnabled";
    public static final String PARAM_DESKTOP_ENABLED = "desktopEnabled";
    public static final String PARAM_EMBEDDED_ENABLED = "embeddedEnabled";
    public static final String PARAM_IOS_ENABLED = "iosEnabled";

    // Views
    public static final String PARAM_PRIMARY_VIEW = "primaryViewName";
    public static final String PARAM_SECONDARY_VIEW = "secondaryViewName";
    public static final String PARAM_PRIMARY_CSS = "primaryCSSName";
    public static final String PARAM_SECONDARY_CSS = "secondaryCSSName";
    public static final String PARAM_PROJECT_CSS_ENABLED = "cssProjectEnabled";
    public static final String PARAM_PRIMARY_CSS_ENABLED = "cssPrimaryViewEnabled";
    public static final String PARAM_SECONDARY_CSS_ENABLED = "cssSecondaryViewEnabled";

    // Afterburner
    public static final String PARAM_AFTERBURNER_ENABLED = "afterburnerEnabled";

    public static final String PARAM_GLUON_DESKTOP_VERSION = "desktopVersion";
    public static final String PARAM_GLUON_MOBILE_VERSION = "mobileVersion";
    public static final String PARAM_GLUON_DOWN_VERSION = "downVersion";
    public static final String PARAM_GLUON_MOBILE_PLUGIN = "mobilePlugin";

    public static final Properties retrieveRemoteProperties() {
        Properties properties = new Properties();
        try {
            // TODO: Fix url before Release to settings.properties
            URL url = new URL("http://download.gluonhq.com/ideplugins/settings-snapshot.properties");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.connect();
            properties.load(conn.getInputStream());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
        return properties;
    }
    
    public static final String getDesktopVersion() {
        return retrieveRemoteProperties().getProperty("desktop", GLUON_DESKTOP_VERSION);
    }
    
    public static final String getMobileVersion() {
        return retrieveRemoteProperties().getProperty("mobile", GLUON_MOBILE_VERSION);
    }
    
    public static final String getDownVersion() {
        return retrieveRemoteProperties().getProperty("down", GLUON_DOWN_VERSION);
    }

    public static final String getPluginVersion() {
        return retrieveRemoteProperties().getProperty("plugin", GLUON_MOBILE_PLUGIN);
    }

}
