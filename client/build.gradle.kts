import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.Plugins.Kotlin.gradle)
        classpath(Deps.Plugins.Kotlin.serialization)
        classpath(Deps.Plugins.android)
        classpath(Deps.Plugins.daggerHilt)
        classpath(Deps.Plugins.navSafeArgs)
        classpath(Deps.Plugins.googleServices)
    }
}

plugins {
    id(Deps.Plugins.Spotless.id) version Deps.Plugins.Spotless.version
}

subprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://repo.repsy.io/mvn/chrynan/public")
        }
    }

    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            ktlint(Deps.Version.ktlint)
        }
    }

    plugins.whenPluginAdded {
        if (this is AppPlugin || this is LibraryPlugin || this is DynamicFeaturePlugin) {
            project.extensions.getByType<BaseExtension>().apply {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
            }
        }
    }

    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions {
            useIR = true
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xinline-classes",
                "-Xopt-in=kotlin.ExperimentalMultiplatform",
                "-Xopt-in=com.russhwolf.settings.ExperimentalSettingsApi",
                "-Xopt-in=com.russhwolf.settings.ExperimentalSettingsImplementation"
            )
        }
    }
}