package com.github.droibit.firebase_todo.ui.main.task.edit.update

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditTaskContentUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditableTaskContentViewModel
import com.github.droibit.firebase_todo.utils.requireValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UpdateTaskViewModel(
    private val taskRepository: TaskRepository,
    override val title: MutableLiveData<String>,
    override val description: MutableLiveData<String>,
    private val updateTaskUiModelSink: MutableLiveData<EditTaskContentUiModel>,
    private val taskId: String
) : ViewModel(), EditableTaskContentViewModel {

    val updateTaskUiModel: LiveData<EditTaskContentUiModel>
        get() = updateTaskUiModelSink

    override val isEditCompleted: LiveData<Boolean> = title.map { it.isNotBlank() }

    override val isInProgress: LiveData<Boolean> = updateTaskUiModelSink.map { it.inProgress }

    @Inject
    constructor(
        taskRepository: TaskRepository,
        @Named("update") task: Task
    ) : this(
        taskRepository,
        title = MutableLiveData(task.title),
        description = MutableLiveData(task.description),
        updateTaskUiModelSink = MutableLiveData(),
        taskId = task.id
    )

    init {
        emitUiModel()
    }

    @UiThread
    fun updateTask() {
        if (updateTaskUiModelSink.requireValue().inProgress) {
            return
        }

        viewModelScope.launch {
            emitUiModel(inProgress = true)
            try {
                // TODO: Consider a specification when existing task is unchanged.
                taskRepository.updateTask(
                    taskId,
                    title = title.requireValue(),
                    description = description.requireValue()
                )
                emitUiModel(success = Event(MessageUiModel(R.string.update_task_update_successful)))
            } catch (e: TaskException) {
                emitUiModel(error = Event(MessageUiModel(R.string.update_task_update_failed)))
            }
        }
    }

    private fun emitUiModel(
        inProgress: Boolean = false,
        success: Event<MessageUiModel>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        updateTaskUiModelSink.value = EditTaskContentUiModel(inProgress, success, error)
    }
}
