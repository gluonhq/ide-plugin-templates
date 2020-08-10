package com.gluonhq.plugin.templates;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ProjectConstants {

    // TODO: What is its usage. Only found in GluonOptInWizardStep.java
    public static final String PLUGIN_VERSION = "2.8.0";

    private static final String JAVAFX_VERSION = "11";
    private static final String JAVAFX_MAVEN_PLUGIN = "0.0.4";
    private static final String JAVAFX_GRADLE_PLUGIN = "0.0.9";

    private static final String GLUON_DESKTOP_VERSION = "1.1.3";
    private static final String GLUON_MOBILE_VERSION = "6.0.5";
    private static final String GLUON_ATTACH_VERSION = "4.0.8";
    private static final String GLUON_CLIENT_MAVEN_PLUGIN = "0.1.30";
    private static final String GLUON_CLIENT_GRADLE_PLUGIN = "0.1.30";
    private static final String GLUON_GLISTEN_AFTERBURNER_VERSION = "2.0.5";

    public static final String DEFAULT_PROJECT_NAME = "GluonApplication";
    public static final String DEFAULT_PACKAGE_NAME = "com.gluonapplication";
    public static final String DEFAULT_CLOUDLINK_HOST = "https://cloud.gluonhq.com";

    // Option
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
    public static final String PARAM_BUILD_TOOL = "buildTool";

    // Views
    public static final String PARAM_PRIMARY_VIEW = "primaryViewName";
    public static final String PARAM_SECONDARY_VIEW = "secondaryViewName";
    public static final String PARAM_PRIMARY_CSS = "primaryCSSName";
    public static final String PARAM_SECONDARY_CSS = "secondaryCSSName";
    public static final String PARAM_PROJECT_CSS_ENABLED = "cssProjectEnabled";
    public static final String PARAM_PRIMARY_CSS_ENABLED = "cssPrimaryViewEnabled";
    public static final String PARAM_SECONDARY_CSS_ENABLED = "cssSecondaryViewEnabled";
    public static final String PARAM_REMOTE_FUNCTIONS = "functions";
    public static final String PARAM_VIEWS = "views";

    // Afterburner
    public static final String PARAM_AFTERBURNER_ENABLED = "afterburnerEnabled";

    // Gluon CloudLink
    public static final String PARAM_GLUON_CLOUDLINK_HOST = "gluonCloudLinkHost";
    public static final String PARAM_GLUON_CLOUDLINK_APPLICATION_KEY = "gluonCloudLinkApplicationKey";
    public static final String PARAM_GLUON_CLOUDLINK_APPLICATION_SECRET = "gluonCloudLinkApplicationSecret";
    public static final String PARAM_GLUON_CLOUDLINK_USER_KEY = "gluonCloudLinkUserKey";
    public static final String PARAM_GLUON_CLOUDLINK_IDE_KEY = "gluonCloudLinkIdeKey";

    public static final String PARAM_GLUON_DESKTOP_VERSION = "desktopVersion";
    public static final String PARAM_GLUON_MOBILE_VERSION = "mobileVersion";
    public static final String PARAM_GLUON_ATTACH_VERSION = "attachVersion";
    public static final String PARAM_GLUON_CLIENT_MAVEN_PLUGIN = "clientMavenPlugin";
    public static final String PARAM_GLUON_CLIENT_GRADLE_PLUGIN = "clientGradlePlugin";
    public static final String PARAM_GLUON_GLISTEN_AFTERBURNER_VERSION = "glistenAfterburnerVersion";

    // Function
    public static final String PARAM_GLUON_FUNCTION_NAME = "functionName";
    public static final String PARAM_GLUON_FUNCTION_METHOD_NAME = "functionMethodName";
    public static final String PARAM_GLUON_FUNCTION_PROJECT_NAME = "projectNameFn";
    
    // OpenJFX
    public static final String PARAM_JAVAFX_VERSION = "javafxVersion";
    public static final String PARAM_JAVAFX_MAVEN_PLUGIN = "javafxMavenPlugin";
    public static final String PARAM_JAVAFX_GRADLE_PLUGIN = "javafxGradlePlugin";
    
    private static Properties properties;
    
    public static final Properties retrieveRemoteProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                URL url = new URL("http://download.gluonhq.com/ideplugins/settings-2.10.properties");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setUseCaches(false);
                conn.connect();
                properties.load(conn.getInputStream());
            } catch (IOException ex) {
            }
        }
        return properties;
    }
    
    public static final String getDesktopVersion() {
        return retrieveRemoteProperties().getProperty("desktop", GLUON_DESKTOP_VERSION);
    }

    public static final String getMobileVersion() {
        return retrieveRemoteProperties().getProperty("mobile", GLUON_MOBILE_VERSION);
    }

    public static final String getAttachVersion() {
        return retrieveRemoteProperties().getProperty("attach", GLUON_ATTACH_VERSION);
    }

    public static final String getGlistenAfterburnerVersion() {
        return retrieveRemoteProperties().getProperty("glistenAfterburner", GLUON_GLISTEN_AFTERBURNER_VERSION);
    }

    public static final String getClientMavenPluginVersion() {
        return retrieveRemoteProperties().getProperty("clientMavenPlugin", GLUON_CLIENT_MAVEN_PLUGIN);
    }

    public static final String getClientGradlePluginVersion() {
        return retrieveRemoteProperties().getProperty("clientGradlePlugin", GLUON_CLIENT_GRADLE_PLUGIN);
    }

    public static final String getJavaFXVersion() {
        return retrieveRemoteProperties().getProperty("javafx", JAVAFX_VERSION);
    }

    public static final String getJavaFXMavenPluginVersion() {
        return retrieveRemoteProperties().getProperty("javafxMavenPlugin", JAVAFX_MAVEN_PLUGIN);
    }

    public static final String getJavaFXGradlePluginVersion() {
        return retrieveRemoteProperties().getProperty("javafxGradlePlugin", JAVAFX_GRADLE_PLUGIN);
    }

}