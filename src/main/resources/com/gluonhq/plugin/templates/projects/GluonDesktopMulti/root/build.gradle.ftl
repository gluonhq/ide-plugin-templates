apply plugin: 'java'
apply plugin: 'application'

repositories {
    mavenLocal()
    jcenter()
    maven {
        url 'http://nexus.gluonhq.com/nexus/content/repositories/releases'
    }
}

mainClassName = '${mainClass}'

dependencies {
    compile 'com.gluonhq:particle:1.0.1-SNAPSHOT'
}
