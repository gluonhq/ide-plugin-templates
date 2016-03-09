buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.javafxports:jfxmobile-plugin:${mobilePlugin}'
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
    compile 'com.gluonhq:charm:${mobileVersion}'

    androidRuntime 'com.gluonhq:charm-android:${mobileVersion}'
    iosRuntime 'com.gluonhq:charm-ios:${mobileVersion}'
    desktopRuntime 'com.gluonhq:charm-desktop:${mobileVersion}'
    <#if embeddedEnabled>
    embeddedRuntime 'com.gluonhq:charm-desktop:${mobileVersion}'
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
