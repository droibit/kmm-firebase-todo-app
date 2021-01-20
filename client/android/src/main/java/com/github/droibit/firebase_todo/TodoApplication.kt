package com.github.droibit.firebase_todo

import android.app.Application
import com.github.aakira.napier.Antilog
import com.github.aakira.napier.Napier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application() {
    @Inject
    fun bootstrap(antilog: Antilog) {
        Napier.base(antilog)
        Napier.d("Bootstrapped!!")
    }
}