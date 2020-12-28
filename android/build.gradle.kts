plugins {
    id("com.android.application")
    kotlin("android")
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

    implementation(Deps.Androidx.appCompat)
    implementation(Deps.Androidx.constraintLayout)
    implementation(Deps.materialDesign)
}