buildscript {
    repositories {
        jcenter()
        google()
        maven {
            url 'http://nexus.gluonhq.com/nexus/content/repositories/releases'
        }
    }
    dependencies {
        classpath 'org.javafxports:jfxmobile-plugin:${mobileGvmPlugin}'
    }
}

apply plugin: 'org.javafxports.jfxmobile'

repositories {
    jcenter()
    maven {
        url 'http://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    if (JavaVersion.current() == JavaVersion.VERSION_1_9) {
        compile 'com.gluonhq:charm:${mobileGvmVersion}'
        androidRuntime 'com.gluonhq:charm:${mobileVersion}'
    } else {
        compile 'com.gluonhq:charm:${mobileVersion}'
    }
}

jfxmobile {
    downConfig {
        version = '${downVersion}'
        // Do not edit the line below. Use Gluon Mobile Settings in your project context menu instead
        plugins 'display', 'lifecycle', 'statusbar', 'storage'
    }
    <#if androidEnabled>
    android {
        manifest = 'src/android/AndroidManifest.xml'
    }
    </#if>
    <#if iosEnabled>
    ios {
        infoPList = file('src/ios/Default-Info.plist')
        forceLinkClasses = [
                'com.gluonhq.**.*',
                'javax.annotations.**.*',
                'javax.inject.**.*',
                'javax.json.**.*',
                'org.glassfish.json.**.*'
        ]
    }
    </#if>
}
