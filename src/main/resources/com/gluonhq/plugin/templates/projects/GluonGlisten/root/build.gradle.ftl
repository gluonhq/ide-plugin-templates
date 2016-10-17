buildscript {
    repositories {
        jcenter()
        maven { // TO BE REMOVED
            url "https://oss.sonatype.org/content/repositories/snapshots/"
        }
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
    maven { // TO BE REMOVED
        url 'http://nexus.gluonhq.com/nexus/content/repositories/snapshots'
        credentials {
            username gluonNexusUsername
            password gluonNexusPassword
        }
    }
    maven { // TO BE REMOVED
        url "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}

mainClassName = '${mainClass}'

dependencies {
    compile 'com.gluonhq:charm:${mobileVersion}'
}

jfxmobile {
    downConfig {
        version = '${downVersion}'
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
