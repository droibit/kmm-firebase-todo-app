package com.github.droibit.firebase_todo.ui.main.task.edit.create

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditTaskUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditableTaskViewModel
import com.github.droibit.firebase_todo.utils.requireValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel(
    private val taskRepository: TaskRepository,
    override val title: MutableLiveData<String>,
    override val description: MutableLiveData<String>,
    private val createTaskUiModelSink: MutableLiveData<EditTaskUiModel>
) : ViewModel(), EditableTaskViewModel {

    val createTaskUiModel: LiveData<EditTaskUiModel>
        get() = createTaskUiModelSink

    override val isInProgress: LiveData<Boolean> = createTaskUiModelSink.map { it.inProgress }

    override val isEditCompleted: LiveData<Boolean> = title.map {
        it.also { Napier.d("title: $it") }.isNotBlank()
    }

    @Inject
    constructor(
        taskRepository: TaskRepository
    ) : this(
        taskRepository,
        title = MutableLiveData(""),
        description = MutableLiveData(""),
        createTaskUiModelSink = MutableLiveData()
    )

    init {
        emitUiModel()
    }

    @UiThread
    fun createTask() {
        if (createTaskUiModelSink.requireValue().inProgress) {
            return
        }

        viewModelScope.launch {
            emitUiModel(inProgress = true)
            try {
                taskRepository.createTask(
                    title = title.requireValue(),
                    description = description.requireValue()
                )
                emitUiModel(success = Event(MessageUiModel(R.string.new_task_create_successful)))
            } catch (e: TaskException) {
                emitUiModel(error = Event(MessageUiModel(R.string.new_task_create_failed)))
            }
        }
    }

    private fun emitUiModel(
        inProgress: Boolean = false,
        success: Event<MessageUiModel>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        createTaskUiModelSink.value = EditTaskUiModel(inProgress, success, error)
    }
}
