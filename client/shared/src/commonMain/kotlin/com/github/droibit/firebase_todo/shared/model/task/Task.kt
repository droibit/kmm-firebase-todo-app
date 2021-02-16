package com.github.droibit.firebase_todo.shared.model.task

import com.github.droibit.firebase_todo.shared.model.task.TaskSorting.Key.CREATED_DATE
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting.Key.TITLE
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting.Order.ASC
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting.Order.DESC
import com.github.droibit.firebase_todo.shared.utils.Parcelable
import com.github.droibit.firebase_todo.shared.utils.Parcelize

// TODO: Review properties.
@Parcelize
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Long
) : Parcelable {
    val isActive: Boolean get() = !isCompleted
}

fun List<Task>.transform(filter: TaskFilter, sorting: TaskSorting): List<Task> {
    return asSequence()
        .filter { task ->
            when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.ACTIVE -> task.isActive
                TaskFilter.COMPLETED -> task.isCompleted
            }
        }
        .sortedWith(
            when (sorting.key) {
                TITLE -> when (sorting.order) {
                    ASC -> compareBy<Task> { it.title.toLowerCase() }
                        .thenBy { it.createdAt }
                    DESC -> compareByDescending<Task> { it.title.toLowerCase() }
                        .thenByDescending { it.createdAt }
                }
                CREATED_DATE -> when (sorting.order) {
                    ASC -> compareBy<Task> { it.createdAt }
                        .thenBy { it.title.toLowerCase() }
                    DESC -> compareByDescending<Task> { it.createdAt }
                        .thenByDescending { it.title.toLowerCase() }
                }
            }
        )
        .toList()
}
