package com.github.droibit.firebase_todo.shared.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * - ref. [CFlow](https://github.com/JetBrains/kotlinconf-app/blob/master/common/src/mobileMain/kotlin/org/jetbrains/kotlinconf/FlowUtils.kt)
 */
class CFlow<T>(
    private val origin: Flow<T>,
    private val dispatcher: CoroutineDispatcher
) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(dispatcher + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }

    fun watch(block: (T?, Throwable?) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it, null)
        }.catch { e ->
            block(null, e)
        }.launchIn(CoroutineScope(dispatcher + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}

fun <T> Flow<T>.wrap(dispatcher: CoroutineDispatcher = Dispatchers.Main): CFlow<T> = CFlow(this, dispatcher)