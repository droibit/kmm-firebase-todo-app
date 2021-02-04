package com.github.droibit.firebase_todo.ui.main.task.list

import android.content.Context
import androidx.annotation.DrawableRes
import com.github.droibit.firebase_todo.R
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

fun TaskSorting.Key.getString(context: Context): String {
    val resId = when (this) {
        TaskSorting.Key.TITLE -> R.string.task_list_sort_by_title
        TaskSorting.Key.CREATED_DATE -> R.string.task_list_sort_by_created_date
    }
    return context.getString(resId)
}

@get:DrawableRes
val TaskSorting.Order.drawableResId: Int
    get() {
        return when (this) {
            TaskSorting.Order.ASC -> R.drawable.ic_round_arrow_upward
            TaskSorting.Order.DESC -> R.drawable.ic_round_arrow_downward
        }
    }