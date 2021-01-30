package com.github.droibit.firebase_todo.shared.data.repository

import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.data.source.settings.UserSettingsDataSource
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider

object TaskRepositoryFactory {
    fun make(
        userSettingsDataSource: UserSettingsDataSource
    ): TaskRepository {
        return TaskRepository(
            userSettingsDataSource,
            CoroutinesDispatcherProvider.sharedInstance
        )
    }
}