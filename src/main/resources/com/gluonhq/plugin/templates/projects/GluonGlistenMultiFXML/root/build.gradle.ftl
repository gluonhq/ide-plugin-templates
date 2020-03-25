buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.javafxports:jfxmobile-plugin:1.3.10'
    }
}

apply plugin: 'org.javafxports.jfxmobile'

repositories {
    jcenter()
    maven {
        url 'https://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

dependencies {
    compile 'com.gluonhq:charm:5.0.0'
    <#if afterburnerEnabled>
    compile 'com.airhacks:afterburner.mfx:1.6.3'
    </#if>
}

jfxmobile {
    downConfig {
        version = '3.8.0'
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
                '${packageName}.**.*',
                'com.gluonhq.**.*',
                'javax.annotations.**.*',
                'javax.inject.**.*',
                'javax.json.**.*',
                'org.glassfish.json.**.*'
        ]
    }
    </#if>
}
