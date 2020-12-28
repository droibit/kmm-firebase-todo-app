import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Deps.Plugins.kotlin)
        classpath(Deps.Plugins.android)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
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