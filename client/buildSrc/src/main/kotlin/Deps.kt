object Deps {
    object Plugins {
        object Kotlin {
            private const val version = "1.4.31"
            const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
            const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"
        }
        const val android = "com.android.tools.build:gradle:4.2.0-beta06"
        const val googleServices = "com.google.gms:google-services:4.3.4"
        const val daggerHilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
        const val navSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Androidx.Navigation.version}"

        object Spotless {
            const val id = "com.diffplug.spotless"
            const val version = "5.10.1"
        }
    }

    object Coroutines {
        // Strictly requires `native-mt` version.
        private const val version = "1.4.3-native-mt!!"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${version}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Stately {
        const val common = "co.touchlab:stately-common:1.1.4"
    }

    object Androidx {
        const val core = "androidx.core:core-ktx:1.6.0-alpha01"
        const val activity = "androidx.activity:activity-ktx:1.3.0-alpha05"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-rc01"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-rc01"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0-alpha08"
        const val preference = "androidx.preference:preference-ktx:1.1.1"

        object Navigation {
            internal const val version = "2.3.4"
            const val runtime = "androidx.navigation:navigation-runtime-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.4.0-alpha01"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
    }

    object Dagger {
        internal const val version = "2.33-beta"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
    }

    object Firebase {
        object MPP {
            private const val version = "1.2.0"
            const val app = "dev.gitlive:firebase-app:$version"
            const val auth = "dev.gitlive:firebase-auth:$version"
            const val firestore = "dev.gitlive:firebase-firestore:$version"
            const val functions = "dev.gitlive:firebase-functions:$version"
        }

        const val analytics = "com.google.firebase:firebase-analytics-ktx:18.0.2"
    }

    object PlayServices {
        const val auth = "com.google.android.gms:play-services-auth:19.0.0"
    }

    object Settings {
        private const val version = "0.7.4"
        const val core = "com.russhwolf:multiplatform-settings:$version"
        const val coroutines = "com.russhwolf:multiplatform-settings-coroutines-native-mt:$version"
        const val datastore = "com.russhwolf:multiplatform-settings-datastore:$version"
    }

    object Chopsticks {
        const val preference = "com.github.droibit.chopsticks:chopstick-preference:1.0.1"
    }

    object Test {
        const val junit = "junit:junit:4.13.1"
    }

    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0"
    const val materialDesign = "com.google.android.material:material:1.4.0-alpha01"
    const val coil = "io.coil-kt:coil:1.1.1"
    const val napier = "com.github.aakira:napier:1.4.1"
    const val inject = "com.chrynan.inject:inject-common:0.7.0"

    object Version {
        const val ktlint = "0.40.0"
    }
}