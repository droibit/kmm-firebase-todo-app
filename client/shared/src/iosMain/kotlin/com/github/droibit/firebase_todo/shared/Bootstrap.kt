package com.github.droibit.firebase_todo.shared

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

fun bootstrap(debuggable: Boolean) {
    Firebase.initialize()

    if (debuggable) {
        Napier.base(DebugAntilog())

        // Workaround: Logging from another thread on iOS
        // https://github.com/AAkira/Napier/issues/56
        CoroutineScope(Dispatchers.Default + SupervisorJob())
            .launch {
                Napier.base(DebugAntilog())
            }
    }

    Napier.d("Bootstrapped!!")
}