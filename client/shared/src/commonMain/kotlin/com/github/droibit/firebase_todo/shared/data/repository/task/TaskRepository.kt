package com.github.droibit.firebase_todo.shared.data.repository.task

import com.github.droibit.firebase_todo.shared.data.source.settings.UserSettingsDataSource
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.utils.CFlow
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import com.github.droibit.firebase_todo.shared.utils.wrap

class TaskRepository(
    private val settingsDataSource: UserSettingsDataSource,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    val taskFilter: CFlow<TaskFilter>
        get() = settingsDataSource.taskFilter.wrap()

    val taskSorting: CFlow<TaskSorting>
        get() = settingsDataSource.taskSorting.wrap()

    suspend fun setTaskFilter(taskFilter: TaskFilter) {
        settingsDataSource.setTaskFilter(taskFilter)
    }

    suspend fun setTaskSorting(taskSorting: TaskSorting) {
        settingsDataSource.setTaskSorting(taskSorting)
    }
}