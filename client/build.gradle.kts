import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.Plugins.kotlin)
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
        jcenter()
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
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xinline-classes")
        }
    }
}