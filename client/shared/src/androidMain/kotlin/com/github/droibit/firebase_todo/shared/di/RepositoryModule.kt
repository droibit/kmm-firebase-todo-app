package com.github.droibit.firebase_todo.shared.di

import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        dataSource: UserDataSource,
        dispatcherProvider: CoroutinesDispatcherProvider
    ): UserRepository {
        return UserRepository(dataSource, dispatcherProvider)
    }
}
