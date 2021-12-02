package com.github.droibit.firebase_todo.shared.data.repository.task

import com.chrynan.inject.Inject
import com.chrynan.inject.Named
import com.chrynan.inject.Singleton
import com.github.droibit.firebase_todo.shared.data.source.settings.UserSettingsDataSource
import com.github.droibit.firebase_todo.shared.data.source.task.TaskDataSource
import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.model.task.Statistics
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.utils.CFlow
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import com.github.droibit.firebase_todo.shared.utils.wrap
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class TaskRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val taskDataSource: TaskDataSource,
    private val settingsDataSource: UserSettingsDataSource,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    @Named("applicationScope") private val externalScope: CoroutineScope
) {
    val taskFilter: CFlow<TaskFilter> = settingsDataSource.taskFilter
        .shareIn(externalScope, started = SharingStarted.Lazily, replay = 1)
        .wrap()

    val taskSorting: CFlow<TaskSorting> = settingsDataSource.taskSorting
        .shareIn(externalScope, started = SharingStarted.Lazily, replay = 1)
        .wrap()

    // TODO: Ensure that the stream is not disconnected when an error occurs.
    val taskList: CFlow<List<Task>> = combine(taskFilter, taskSorting) { filter, sorting ->
        filter to sorting
    }.flatMapLatest { (filter, sorting) ->
        val user = requireNotNull(userDataSource.currentUser)
        taskDataSource.getTaskList(user.uid, filter, sorting)
    }
        .onStart { Napier.d("Start creating a task list.") }
        .onCompletion { Napier.d("Finish creating a task list.") }
        .shareIn(externalScope, started = SharingStarted.WhileSubscribed(), replay = 1)
        .wrap()

    // TODO: Ensure that the stream is not disconnected when an error occurs.
    val statistics: CFlow<Statistics> = flow {
        val user = requireNotNull(userDataSource.currentUser)
        emit(user.uid)
    }.flatMapLatest {
        taskDataSource.getTaskStatistics(userId = it)
    }.shareIn(externalScope, started = SharingStarted.WhileSubscribed(), replay = 1)
        .wrap()

    suspend fun setTaskFilter(taskFilter: TaskFilter) {
        settingsDataSource.setTaskFilter(taskFilter)
    }

    suspend fun setTaskSorting(taskSorting: TaskSorting) {
        settingsDataSource.setTaskSorting(taskSorting)
    }

    fun getTask(taskId: String): CFlow<Task?> {
        val user = requireNotNull(userDataSource.currentUser)
        return taskDataSource.getTask(user.uid, taskId)
            .wrap()
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun createTask(title: String, description: String) {
        withContext(dispatcherProvider.io) {
            val user = requireNotNull(userDataSource.currentUser)
            taskDataSource.createTask(user.uid, title, description)
        }
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun updateTask(taskId: String, title: String, description: String) {
        withContext(dispatcherProvider.io) {
            val user = requireNotNull(userDataSource.currentUser)
            taskDataSource.updateTask(user.uid, taskId, title, description)
        }
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun updateTask(taskId: String, completed: Boolean) {
        withContext(dispatcherProvider.io) {
            val user = requireNotNull(userDataSource.currentUser)
            taskDataSource.updateTask(user.uid, taskId, completed)
        }
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun deleteTask(taskId: String) {
        withContext(dispatcherProvider.io) {
            val user = requireNotNull(userDataSource.currentUser)
            taskDataSource.deleteTask(user.uid, taskId)
        }
    }
}
