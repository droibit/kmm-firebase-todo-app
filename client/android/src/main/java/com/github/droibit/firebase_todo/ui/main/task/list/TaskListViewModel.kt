package com.github.droibit.firebase_todo.ui.main.task.list

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    init {
        // TODO: Must Remove the test code.
        viewModelScope.launch {
            emitUiState(inProgress = true)
            delay(1000)

            val tasks = List(30) {
                Task(
                    id = "id-$it",
                    title = "Title-$it",
                    description = if (it % 3 == 0) "Description-$it" else "",
                    isCompleted = it % 2 == 0,
                    createdAt = System.currentTimeMillis()
                )
            }
            emitUiState(
                success = TaskListUiModel(
                    sourceTasks = tasks,
                    taskFilter = TaskFilter.DEFAULT,
                    taskSorting = TaskSorting.DEFAULT
                )
            )
        }
    }

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

    @UiThread
    fun onTaskFilterChanged(newTaskFilter: TaskFilter) {
        val ui = uiModelSink.value?.success ?: return
        emitUiState(success = ui.filtered(newTaskFilter))
    }

    @UiThread
    fun onTaskSortingChange(newTaskSorting: TaskSorting) {
        val ui = uiModelSink.value?.success ?: return
        emitUiState(success = ui.sorted(newTaskSorting))
    }

    @UiThread
    private fun emitUiState(
        inProgress: Boolean = false,
        success: TaskListUiModel? = null,
        error: MessageUiModel? = null
    ) {
        uiModelSink.value = GetTaskListUiModel(
            inProgress = inProgress,
            success = success,
            error = error
        )
    }
}