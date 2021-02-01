package com.github.droibit.firebase_todo.ui.main.task.list

import android.content.Context
import androidx.annotation.DrawableRes
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting

fun TaskFilter.getString(context: Context): String {
    val resId = when (this) {
        TaskFilter.ALL -> R.string.task_list_header_all
        TaskFilter.ACTIVE -> R.string.task_list_header_active
        TaskFilter.COMPLETED -> R.string.task_list_header_completed
    }
    return context.getString(resId)
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