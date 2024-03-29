package com.github.droibit.firebase_todo

import com.github.droibit.firebase_todo.shared.model.app.AppVersion
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Singleton

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
                override fun isEnable(priority: LogLevel, tag: String?): Boolean = false
                override fun performLog(
                    priority: LogLevel,
                    tag: String?,
                    throwable: Throwable?,
                    message: String?
                ) = Unit
            }
        }
    }

    @Provides
    fun provideAppVersion(): AppVersion {
        return AppVersion(
            name = BuildConfig.VERSION_NAME,
            code = "${BuildConfig.VERSION_CODE}"
        )
    }

    @Named("applicationScope")
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        // ref. https://medium.com/androiddevelopers/coroutines-patterns-for-work-that-shouldnt-be-cancelled-e26c40f142ad
        return CoroutineScope(SupervisorJob())
    }
}
