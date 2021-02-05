package com.github.droibit.firebase_todo.ui.main.task.list

import android.content.Context
import androidx.annotation.DrawableRes
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.shared.model.task.transform
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

// TODO: Need review.
data class GetTaskListUiModel(
    val inProgress: Boolean,
    val success: TaskListUiModel?,
    val error: MessageUiModel?
) {
    val showsTaskList: Boolean
        get() = !inProgress && success?.isEmptyTask == false

    val showsEmptyView: Boolean
        get() = !inProgress && success?.isEmptyTask == true
}

class TaskListUiModel(
    private val sourceTasks: List<Task>,
    val tasks: List<Task>,
    val taskFilter: TaskFilter,
    val taskSorting: TaskSorting
) {
    constructor(
        sourceTasks: List<Task>,
        taskFilter: TaskFilter,
        taskSorting: TaskSorting
    ) : this(
        sourceTasks,
        sourceTasks.transform(taskFilter, taskSorting),
        taskFilter,
        taskSorting
    )

    val isEmptyTask: Boolean get() = tasks.isEmpty()

    fun filtered(newTaskFilter: TaskFilter): TaskListUiModel {
        return TaskListUiModel(
            sourceTasks,
            newTaskFilter,
            taskSorting
        )
    }

    fun sorted(newTaskSorting: TaskSorting): TaskListUiModel {
        return TaskListUiModel(
            sourceTasks,
            taskFilter,
            newTaskSorting
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskListUiModel

        if (sourceTasks != other.sourceTasks) return false
        if (taskFilter != other.taskFilter) return false
        if (taskSorting != other.taskSorting) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceTasks.hashCode()
        result = 31 * result + taskFilter.hashCode()
        result = 31 * result + taskSorting.hashCode()
        return result
    }

    override fun toString(): String {
        return "TaskListUiModel(tasks=$tasks, taskFilter=$taskFilter, taskSorting=$taskSorting)"
    }
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