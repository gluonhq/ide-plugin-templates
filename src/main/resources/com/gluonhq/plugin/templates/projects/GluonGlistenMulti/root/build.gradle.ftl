buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.javafxports:jfxmobile-plugin:1.0.7'
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

dependencies {
    compile 'com.gluonhq:charm:2.0.0'

    androidRuntime 'com.gluonhq:charm-android:2.0.0'
    iosRuntime 'com.gluonhq:charm-ios:2.0.0'
    desktopRuntime 'com.gluonhq:charm-desktop:2.0.0'
    <#if embeddedEnabled>
    embeddedRuntime 'com.gluonhq:charm-desktop:2.0.0'
    </#if>
}

<#if androidEnabled || iosEnabled>
jfxmobile {
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
                'io.datafx.**.*',
                'javax.annotations.**.*',
                'javax.inject.**.*',
                'javax.json.**.*',
                'org.glassfish.json.**.*'
        ]
    }
    </#if>
}
</#if>
