import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
    id("com.android.library")
    id("dagger.hilt.android.plugin")
}

kotlin {
    android()
    ios {
        binaries {
            // ref. https://github.com/GitLiveApp/firebase-kotlin-sdk/issues/111#issuecomment-738772372
            framework {
                baseName = "Shared"
                transitiveExport = true

                linkerOpts("-F${rootProject.projectDir}/ios/Carthage/Build/iOS/")
                linkerOpts("-ObjC")

                // https://kotlinlang.org/docs/reference/mpp-build-native-binaries.html#export-dependencies-to-binaries
                export(Deps.napier)
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Coroutines.core)

                implementation(Deps.Stately.common)
                implementation(Deps.Stately.isolate)
                
                api(Deps.Firebase.MPP.auth)
                implementation(Deps.Firebase.MPP.firestore)
                implementation(Deps.Firebase.MPP.functions)

                api(Deps.napier)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Dagger.hilt)
            }
        }
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

// ref. https://www.reddit.com/r/Kotlin/comments/ack2r6/problem_using_kapt_in_a_multiplatform_project/
dependencies {
    "kapt"(Deps.Dagger.compiler)
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)

    defaultConfig {
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        resConfigs("en", "ja")
        consumerProguardFiles("consumer-rules.pro")
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

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

kapt {
    correctErrorTypes = true
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)