package com.github.droibit.firebase_todo.shared.utils

import io.github.aakira.napier.Napier as Original

object Napier {
    fun d(message: String, throwable: Throwable? = null) {
        Original.d(message, throwable, tag = "")
    }

    fun e(message: String, throwable: Throwable? = null) {
        Original.e(message, throwable, tag = "")
    }
}
