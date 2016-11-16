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
// TODO: Remove mavenLocal()
    mavenLocal()
    jcenter()
    maven {
        url 'http://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

dependencies {
// TODO: Use Afterburner version without retrolambda and remove this workaround:
    compile ('com.gluonhq:glisten-afterburner:${glistenAfterburnerVersion}') {
         exclude group: 'com.airhacks', module: 'afterburner.mfx'
    }
    compileNoRetrolambda 'com.airhacks:afterburner.mfx:1.6.2'
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
