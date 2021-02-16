package com.github.droibit.firebase_todo.ui.main.task.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface EditableTaskViewModel {
    val title: MutableLiveData<String>
    val description: MutableLiveData<String>
    val isEditCompleted: LiveData<Boolean>
}
