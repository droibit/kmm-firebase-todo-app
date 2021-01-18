package com.github.droibit.firebase_todo.shared.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
actual class CoroutinesDispatcherProvider(
    actual val main: CoroutineDispatcher,
    actual val computation: CoroutineDispatcher,
    actual val io: CoroutineDispatcher
) {
    @Inject
    constructor() : this(Main, Default, IO)
}