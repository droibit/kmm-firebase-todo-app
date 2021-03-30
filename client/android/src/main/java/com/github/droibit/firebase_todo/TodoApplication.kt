package com.github.droibit.firebase_todo

import android.app.Application
import com.github.aakira.napier.Antilog
import com.github.aakira.napier.Napier
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application() {
    @Inject
    fun bootstrap(antilog: Antilog) {
        Firebase.initialize(this)

        Napier.base(antilog)
        Napier.d("Bootstrapped!!")
    }
}
