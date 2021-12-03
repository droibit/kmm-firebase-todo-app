package com.github.droibit.firebase_todo.ui.main.task.detail

import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditTaskCompletionUiModel
import com.github.droibit.firebase_todo.utils.requireValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.LazyThreadSafetyMode.NONE

@HiltViewModel
class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val taskSink: MutableLiveData<Task>,
    private val deleteTaskUiModelSink: MutableLiveData<DeleteTaskUiModel>,
    private val updateTaskCompletionUiModelSink: MutableLiveData<EditTaskCompletionUiModel>
) : ViewModel() {

    val task: LiveData<Task> by lazy(NONE) {
        taskRepository.getTask(taskSink.requireValue().id)
            .mapNotNull { it }
            .onEach {
                taskSink.value = it
            }
            .catch {
                // TODO: Error handling.
            }
            .launchIn(viewModelScope)
        taskSink
    }

    val deleteTaskUiModel: LiveData<DeleteTaskUiModel> get() = deleteTaskUiModelSink

    val updateTaskCompletionUiModel: LiveData<EditTaskCompletionUiModel>
        get() = updateTaskCompletionUiModelSink

    @Inject
    constructor(
        taskRepository: TaskRepository,
        @Named("detail") task: Task
    ) : this(
        taskRepository,
        taskSink = MutableLiveData(task),
        deleteTaskUiModelSink = MutableLiveData(),
        updateTaskCompletionUiModelSink = MutableLiveData()
    )

    init {
        emitDeleteUiModel()
        emitUpdateCompletionUiModel()
    }

    @MainThread
    fun deleteTask() {
        if (deleteTaskUiModelSink.requireValue().inProgress) {
            return
        }

        viewModelScope.launch {
            emitDeleteUiModel(inProgress = true)
            try {
                taskRepository.deleteTask(
                    taskSink.requireValue().id
                )
                emitDeleteUiModel(success = Event(Unit))
            } catch (e: TaskException) {
                emitDeleteUiModel(error = Event(MessageUiModel(R.string.task_detail_delete_failed)))
            }
        }
    }

    @UiThread
    fun toggleTaskCompletion() {
        if (updateTaskCompletionUiModelSink.requireValue().inProgress) {
            return
        }

        viewModelScope.launch {
            emitUpdateCompletionUiModel(inProgress = true)
            try {
                val task = taskSink.requireValue()
                taskRepository.updateTask(
                    taskId = task.id,
                    completed = !task.isCompleted
                )
                emitUpdateCompletionUiModel(success = Event(Unit))
            } catch (e: TaskException) {
                emitUpdateCompletionUiModel(
                    error = Event(MessageUiModel(R.string.update_task_update_failed))
                )
            }
        }
    }

    private fun emitDeleteUiModel(
        inProgress: Boolean = false,
        success: Event<Unit>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        deleteTaskUiModelSink.value = DeleteTaskUiModel(inProgress, success, error)
    }

    private fun emitUpdateCompletionUiModel(
        inProgress: Boolean = false,
        success: Event<Unit>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        updateTaskCompletionUiModelSink.value =
            EditTaskCompletionUiModel(inProgress, success, error)
    }
}
