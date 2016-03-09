apply plugin: 'java'
apply plugin: 'application'

repositories {
    jcenter()
    maven {
        url 'http://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

dependencies {
    compile 'com.gluonhq:particle:${desktopVersion}'
}
