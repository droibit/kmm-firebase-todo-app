package com.github.droibit.firebase_todo.utils

import androidx.lifecycle.LiveData

fun <T>LiveData<T>.requireValue(): T {
    return checkNotNull(value)
}