package com.github.droibit.firebase_todo.ui.main.task.edit.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.ui.main.task.edit.EditableTaskViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel(
    private val taskRepository: TaskRepository,
    override val title: MutableLiveData<String>,
    override val description: MutableLiveData<String>
) : ViewModel(), EditableTaskViewModel {

    override val isEditCompleted: LiveData<Boolean> = title.map {
        it.also { Napier.d("title: $it") }.isNotBlank()
    }

    @Inject
    constructor(
        taskRepository: TaskRepository
    ) : this(
        taskRepository,
        title = MutableLiveData(""),
        description = MutableLiveData("")
    )
}
