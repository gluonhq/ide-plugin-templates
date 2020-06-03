plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '${javafxGradlePlugin}'
    id 'com.gluonhq.client-gradle-plugin' version '${clientGradlePlugin}'
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
}

javafx {
    version = '${javafxVersion}'
    modules = [ 'javafx.controls' ]
}

gluonClient {
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