object Deps {
    object Plugins {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21"
        const val android = "com.android.tools.build:gradle:4.2.0-beta03"
        const val googleServices = "com.google.gms:google-services:4.3.4"
        const val daggerHilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
        const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:5.9.0"

        object Spotless {
            const val id = "com.diffplug.spotless"
            const val version = "5.9.0"
        }
    }

    object Coroutines {
        private const val version = "1.4.2-native-mt"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Stately {
        const val common = "co.touchlab:stately-common:1.1.1"
        const val isolate = "co.touchlab:stately-isolate:1.1.1-a1"
    }

    object Androidx {
        const val activity = "androidx.activity:activity-ktx:1.2.0-rc01"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.0-rc01"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-beta01"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    }

    object Dagger {
        internal const val version = "2.31-alpha"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
    }

    object Firebase {
        object MPP {
            private const val version = "1.2.0"
            const val auth = "dev.gitlive:firebase-auth:$version"
            const val firestore = "dev.gitlive:firebase-firestore:$version"
            const val functions = "dev.gitlive:firebase-functions:$version"
        }

        const val analytics = "com.google.firebase:firebase-analytics-ktx:17.3.0"
    }

    const val materialDesign = "com.google.android.material:material:1.3.0-rc01"

    object Test {
        const val junit = "junit:junit:4.13.1"
    }

    object Version {
        const val ktlint = "0.40.0"
    }
}