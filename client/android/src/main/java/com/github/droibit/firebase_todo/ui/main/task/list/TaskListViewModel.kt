package com.github.droibit.firebase_todo.ui.main.task.list

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel(
    private val taskRepository: TaskRepository,
    private val uiModelSink: MutableLiveData<GetTaskListUiModel>,
    private val filterTaskNavSink: MutableLiveData<Event<TaskFilter>>,
    private val sortTaskNavSink: MutableLiveData<Event<TaskSorting>>,
) : ViewModel() {

    val uiModel: LiveData<GetTaskListUiModel> get() = uiModelSink

    val filterTaskNavigation: LiveData<Event<TaskFilter>> get() = filterTaskNavSink

    val sortTaskNavigation: LiveData<Event<TaskSorting>> get() = sortTaskNavSink

    @Inject
    constructor(
        taskRepository: TaskRepository
    ) : this(
        taskRepository,
        MutableLiveData(),
        MutableLiveData(),
        MutableLiveData()
    )

    @UiThread
    fun onFilterTaskClick() {
        val ui = uiModelSink.value?.success ?: return
        filterTaskNavSink.value = Event(ui.taskFilter)
    }

    @UiThread
    fun onChangeSortKeyClick() {
        val ui = uiModelSink.value?.success ?: return
        sortTaskNavSink.value = Event(ui.taskSorting)
    }
}