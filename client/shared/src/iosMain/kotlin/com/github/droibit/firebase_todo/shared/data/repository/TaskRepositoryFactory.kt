package com.github.droibit.firebase_todo.shared.data.repository

import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.data.source.settings.UserSettingsDataSource
import com.github.droibit.firebase_todo.shared.data.source.task.TaskDataSource
import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import kotlinx.coroutines.GlobalScope

object TaskRepositoryFactory {
    fun make(
        userDataSource: UserDataSource,
        taskDataSource: TaskDataSource,
        userSettingsDataSource: UserSettingsDataSource,
    ): TaskRepository {
        return TaskRepository(
            userDataSource,
            taskDataSource,
            userSettingsDataSource,
            CoroutinesDispatcherProvider.sharedInstance,
            // TODO: Consider make a CoroutineScope for this application lifecycle.
            externalScope = GlobalScope
        )
    }
}
