package com.github.droibit.firebase_todo.ui.main.task.list

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.github.droibit.firebase_todo.ui.main.task.edit.EditTaskCompletionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@HiltViewModel
class TaskListViewModel(
    private val taskRepository: TaskRepository,
    private val taskListUiModelSink: MutableLiveData<GetTaskListUiModel>,
    private val filterTaskNavSink: MutableLiveData<Event<TaskFilter>>,
    private val sortTaskNavSink: MutableLiveData<Event<TaskSorting>>,
    private val updateTaskCompletionUiModelSink: MutableLiveData<EditTaskCompletionUiModel>
) : ViewModel() {

    val taskListUiModel: LiveData<GetTaskListUiModel> by lazy(NONE) {
        combine(
            taskRepository.taskList,
            taskRepository.taskFilter,
            taskRepository.taskSorting
        ) { tasks, filter, sorting ->
            TaskListUiModel(tasks, filter, sorting)
        }
            .onStart { emitTaskListUiModel(inProgress = true) }
            .onEach { emitTaskListUiModel(success = it) }
            .catch {
                // TODO: Error handling.
            }
            .launchIn(viewModelScope)
        taskListUiModelSink
    }

    val filterTaskNavigation: LiveData<Event<TaskFilter>> get() = filterTaskNavSink

    val sortTaskNavigation: LiveData<Event<TaskSorting>> get() = sortTaskNavSink

    @Inject
    constructor(
        taskRepository: TaskRepository
    ) : this(
        taskRepository,
        taskListUiModelSink = MutableLiveData(),
        filterTaskNavSink = MutableLiveData(),
        sortTaskNavSink = MutableLiveData(),
        updateTaskCompletionUiModelSink = MutableLiveData()
    )

    @UiThread
    fun onFilterTaskClick() {
        val ui = taskListUiModelSink.value?.success ?: return
        filterTaskNavSink.value = Event(ui.taskFilter)
    }

    @UiThread
    fun onChangeSortKeyClick() {
        val ui = taskListUiModelSink.value?.success ?: return
        sortTaskNavSink.value = Event(ui.taskSorting)
    }

    @UiThread
    fun onTaskFilterChanged(newTaskFilter: TaskFilter) {
        viewModelScope.launch {
            taskRepository.setTaskFilter(newTaskFilter)
        }
    }

    @UiThread
    fun onTaskSortingChange(newTaskSorting: TaskSorting) {
        viewModelScope.launch {
            taskRepository.setTaskSorting(newTaskSorting)
        }
    }

    @UiThread
    private fun emitTaskListUiModel(
        inProgress: Boolean = false,
        success: TaskListUiModel? = null,
        error: MessageUiModel? = null
    ) {
        taskListUiModelSink.value = GetTaskListUiModel(
            inProgress = inProgress,
            success = success,
            error = error
        )
    }
}
