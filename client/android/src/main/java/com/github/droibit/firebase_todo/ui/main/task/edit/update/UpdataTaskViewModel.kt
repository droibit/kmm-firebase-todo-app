package com.github.droibit.firebase_todo.ui.main.task.edit.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.ui.main.task.edit.EditableTaskViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UpdateTaskViewModel(
    private val taskRepository: TaskRepository,
    override val title: MutableLiveData<String>,
    override val description: MutableLiveData<String>
) : ViewModel(), EditableTaskViewModel {

    override val isEditCompleted: LiveData<Boolean> = title.map { it.isNotBlank() }

    override val isInProgress: LiveData<Boolean>
        get() = TODO("Not implemeted yet.")

    @Inject
    constructor(
        taskRepository: TaskRepository,
        @Named("update") task: Task
    ) : this(
        taskRepository,
        title = MutableLiveData(task.title),
        description = MutableLiveData(task.description)
    )
}
