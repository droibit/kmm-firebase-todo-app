plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.github.droibit.firebase_todo"
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        resourceConfigurations + listOf("en", "ja")
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("keystore/debug.keystore")
        }
    }

    packagingOptions {
        exclude("kotlin/**")
        exclude("**/*.kotlin_metadata")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
    }
}

repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(Deps.Coroutines.core)
    implementation(Deps.Coroutines.android)

    implementation(Deps.Androidx.core)
    implementation(Deps.Androidx.activity)
    implementation(Deps.Androidx.appCompat)
    implementation(Deps.Androidx.fragment)
    implementation(Deps.Androidx.Navigation.runtime)
    implementation(Deps.Androidx.Navigation.fragment)
    implementation(Deps.Androidx.Navigation.ui)
    implementation(Deps.Androidx.Lifecycle.runtime)
    implementation(Deps.Androidx.Lifecycle.liveData)
    implementation(Deps.Androidx.Lifecycle.viewModel)
    implementation(Deps.Androidx.constraintLayout)
    implementation(Deps.Androidx.recyclerView)
    implementation(Deps.Androidx.dataStore)
    implementation(Deps.Androidx.preference)

    implementation(Deps.materialDesign)
    implementation(Deps.insetter)
    implementation(Deps.coil)

    implementation(Deps.PlayServices.auth)
    implementation(Deps.Firebase.analytics)

    implementation(Deps.Dagger.hilt)
    "kapt"(Deps.Dagger.compiler)

    implementation(Deps.Settings.coroutines)
    implementation(Deps.Settings.datastore) {
        exclude(module = "multiplatform-settings-coroutines")
    }
    implementation(Deps.napier)
    implementation(Deps.Chopsticks.preference)
}
