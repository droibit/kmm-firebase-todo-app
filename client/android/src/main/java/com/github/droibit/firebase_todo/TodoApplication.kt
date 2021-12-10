package com.github.droibit.firebase_todo

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application() {
    @Inject
    fun bootstrap(antilog: Antilog) {
        Firebase.initialize(this)

        DynamicColors.applyToActivitiesIfAvailable(this)

        Napier.base(antilog)
        Napier.d("Bootstrapped!!")
    }
}
