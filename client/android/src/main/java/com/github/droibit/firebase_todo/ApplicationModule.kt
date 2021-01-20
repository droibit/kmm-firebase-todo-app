package com.github.droibit.firebase_todo

import com.github.aakira.napier.Antilog
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Named("debuggable")
    @Provides
    fun provideDebuggable(): Boolean = BuildConfig.DEBUG

    @Provides
    fun provideAntilog(@Named("debuggable") debuggable: Boolean): Antilog {
        return if (debuggable) DebugAntilog() else {
            object : Antilog() {
                override fun isEnable(priority: Napier.Level, tag: String?): Boolean = false
                override fun performLog(
                    priority: Napier.Level,
                    tag: String?,
                    throwable: Throwable?,
                    message: String?
                ) = Unit
            }
        }
    }
}