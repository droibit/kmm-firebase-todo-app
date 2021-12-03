plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("kotlin-parcelize")

    // When using `native.cocoapods` and` dagger.hilt.android.plugin` plugin at the same time,
    // following error occurs, so the dagger plugin is disabled.
    // - The Hilt Android Gradle plugin is applied but no com.google.dagger:hilt-compiler dependency was found.
    // id("dagger.hilt.android.plugin")
    // kotlin("kapt")
}

version = "1.0"

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        resourceConfigurations + listOf("en", "ja")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

kotlin {
    android()
    ios()

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Shared module for Firebase TODO app."
        homepage = "https://github.com/droibit/kmm-firebase-todo-app"
        authors = "Shinya Kumagai"
        license = "Apache License, Version 2.0"
        ios.deploymentTarget = "14.0"
        podfile = project.file("../ios/Podfile")

        framework {
            baseName = "Shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Coroutines.core)

                implementation(Deps.Stately.common)
                
                api(Deps.Firebase.MPP.auth)
                api(Deps.Firebase.MPP.firestore)
                // implementation(Deps.Firebase.MPP.functions)

                implementation(Deps.Settings.core)
                implementation(Deps.Settings.coroutines)
                implementation(Deps.serialization)
                implementation(Deps.napier)
                implementation(Deps.inject)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting 
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Deps.Test.junit)
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

// // ref. https://www.reddit.com/r/Kotlin/comments/ack2r6/problem_using_kapt_in_a_multiplatform_project/
// dependencies {
//     "kapt"(Deps.Dagger.compiler)
// }

// kapt {
//     correctErrorTypes = true
// }