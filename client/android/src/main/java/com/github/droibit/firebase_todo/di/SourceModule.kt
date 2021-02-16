package com.github.droibit.firebase_todo.di

import android.content.Context
import androidx.datastore.preferences.createDataStore
import com.github.droibit.firebase_todo.shared.data.source.settings.UserSettingsDataSource
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {

    @Singleton
    @Provides
    fun provideUserSettingsDataSource(settings: FlowSettings): UserSettingsDataSource {
        return UserSettingsDataSource(settings)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
}

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Singleton
    @Provides
    fun provideFlowSettings(@ApplicationContext context: Context): FlowSettings {
        return DataStoreSettings(datastore = context.createDataStore("user_settings"))
    }
}