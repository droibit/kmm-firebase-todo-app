plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    // id("com.google.gms.google-services")
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)

    defaultConfig {
        applicationId = "com.github.droibit.firebase_todo"
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    implementation(project(":shared"))

    implementation(Deps.Coroutines.core)

    implementation(Deps.Androidx.appCompat)
    implementation(Deps.Androidx.constraintLayout)
    implementation(Deps.materialDesign)

    implementation(Deps.Dagger.hilt)
    "kapt"(Deps.Dagger.compiler)
}
