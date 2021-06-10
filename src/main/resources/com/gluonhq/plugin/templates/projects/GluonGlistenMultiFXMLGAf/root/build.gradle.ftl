plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '${javafxGradlePlugin}'
    id 'com.gluonhq.gluonfx-gradle-plugin' version '${clientGradlePlugin}'
}

repositories {
    mavenCentral()
    maven {
        url 'https://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

dependencies {
    compile 'com.gluonhq:charm-glisten:${mobileVersion}'
    compile 'com.gluonhq:glisten-afterburner:${glistenAfterburnerVersion}'
}

javafx {
    version = '${javafxVersion}'
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}

gluonfx {
    <#if iosEnabled>
    // target = 'ios' // Uncomment to enable iOS
    </#if>
    <#if androidEnabled>
    // target = 'android' // Uncomment to enable Android
    </#if>
    attachConfig {
        version = "${attachVersion}"
        services 'display', 'lifecycle', 'statusbar', 'storage'
    }
}