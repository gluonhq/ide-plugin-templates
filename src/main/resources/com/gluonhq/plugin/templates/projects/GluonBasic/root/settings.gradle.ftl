pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url "https://nexus.gluonhq.com/nexus/content/repositories/releases"
        }
        gradlePluginPortal()
    }
}

rootProject.name = 'GluonBasic'