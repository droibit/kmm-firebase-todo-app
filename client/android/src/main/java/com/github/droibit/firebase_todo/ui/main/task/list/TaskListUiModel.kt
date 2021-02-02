package com.github.droibit.firebase_todo.ui.main.task.list

import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

// TODO: Need review.
data class GetTaskListUiModel(
    val inProgress: Boolean,
    val success: TaskListUiModel?,
    val error: MessageUiModel?
)

data class TaskListUiModel(
    val tasks: List<Task>,
    val taskFilter: TaskFilter,
    val taskSorting: TaskSorting
) {
    val isEmptyTask: Boolean get() = tasks.isEmpty()
}