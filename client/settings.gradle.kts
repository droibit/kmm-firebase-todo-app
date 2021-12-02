pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    ":android",
    ":shared"
)

rootProject.name = "KMM-Firebase-TODO"