package com.github.droibit.firebase_todo.shared.data.repository

import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider

object UserRepositoryFactory {
    fun make(dataSource: UserDataSource): UserRepository {
        return UserRepository(dataSource, CoroutinesDispatcherProvider.sharedInstance)
    }
}
