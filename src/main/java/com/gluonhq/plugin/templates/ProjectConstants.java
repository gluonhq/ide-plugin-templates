/*
 * Copyright (c) 2016, 2022, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.plugin.templates;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ProjectConstants {

    // TODO: What is its usage. Only found in GluonOptInWizardStep.java
    public static final String PLUGIN_VERSION = "2.8.0";
    public static final String IDE_ECLIPSE = "eclipse";
    public static final String IDE_INTELLIJ = "intellij";
    public static final String IDE_NETBEANS = "netbeans";

    private static final String JAVAFX_VERSION = "11";
    private static final String JAVAFX_MAVEN_PLUGIN = "0.0.8";
    private static final String JAVAFX_GRADLE_PLUGIN = "0.0.10";
    private static final String GLUONFX_MAVEN_PLUGIN = "1.0.7";
    private static final String GLUONFX_GRADLE_PLUGIN = "1.0.7";

    private static final String GLUON_GLISTEN_VERSION = "6.1.0";
    private static final String GLUON_ATTACH_VERSION = "4.0.13";
    private static final String GLUON_GLISTEN_AFTERBURNER_VERSION = "2.1.0";

    public static final String DEFAULT_PROJECT_NAME = "GluonApplication";
    public static final String DEFAULT_PACKAGE_NAME = "com.gluonapplication";
    public static final String DEFAULT_CLOUDLINK_HOST = "https://cloud.gluonhq.com";

    // Option
    public static final String PARAM_USER_IDE_OPTIN = "gluon_ide_optin";
    public static final String PARAM_USER_EMAIL = "gluon_user_email";
    public static final String PARAM_USER_UPTODATE = "gluon_user_uptodate";
    public static final String PARAM_USER_LICENSE = "gluon_user_license";
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
    public static final String PARAM_IDE = "ide";

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

    public static final String PARAM_GLUON_GLISTEN_VERSION = "glistenVersion";
    public static final String PARAM_GLUON_ATTACH_VERSION = "attachVersion";
    public static final String PARAM_GLUONFX_MAVEN_PLUGIN = "gluonfxMavenPlugin";
    public static final String PARAM_GLUONFX_GRADLE_PLUGIN = "gluonfxGradlePlugin";
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
                URL url = new URL("http://download.gluonhq.com/ideplugins/settings-2.10.4.properties");

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

    public static final String getGlistenVersion() {
        return retrieveRemoteProperties().getProperty("glisten", GLUON_GLISTEN_VERSION);
    }

    public static final String getAttachVersion() {
        return retrieveRemoteProperties().getProperty("attach", GLUON_ATTACH_VERSION);
    }

    public static final String getGlistenAfterburnerVersion() {
        return retrieveRemoteProperties().getProperty("glistenAfterburner", GLUON_GLISTEN_AFTERBURNER_VERSION);
    }

    public static final String getGluonFXMavenPluginVersion() {
        return retrieveRemoteProperties().getProperty("gluonfxMavenPlugin", GLUONFX_MAVEN_PLUGIN);
    }

    public static final String getGluonFXGradlePluginVersion() {
        return retrieveRemoteProperties().getProperty("gluonfxGradlePlugin", GLUONFX_GRADLE_PLUGIN);
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