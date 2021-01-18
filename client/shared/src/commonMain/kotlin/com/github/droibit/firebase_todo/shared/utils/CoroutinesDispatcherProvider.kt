package com.github.droibit.firebase_todo.shared.utils

import kotlinx.coroutines.CoroutineDispatcher

expect class CoroutinesDispatcherProvider {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
}