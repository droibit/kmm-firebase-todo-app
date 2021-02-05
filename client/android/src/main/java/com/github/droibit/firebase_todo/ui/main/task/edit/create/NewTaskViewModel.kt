package com.github.droibit.firebase_todo.ui.main.task.edit.create

import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

}