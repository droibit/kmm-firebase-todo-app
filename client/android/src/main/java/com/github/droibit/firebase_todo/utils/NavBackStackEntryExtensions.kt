package com.github.droibit.firebase_todo.utils

import androidx.navigation.NavBackStackEntry

fun <T> NavBackStackEntry?.setResult(key: String, value: T) {
    this?.savedStateHandle?.set(key, value)
}

fun <T> NavBackStackEntry.consumeResult(key: String): T? {
    return savedStateHandle.get<T>(key)?.also {
        savedStateHandle.remove<T>(key)
    }
}