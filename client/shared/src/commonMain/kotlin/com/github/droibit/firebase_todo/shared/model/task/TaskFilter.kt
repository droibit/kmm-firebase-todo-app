package com.github.droibit.firebase_todo.shared.model.task

import kotlinx.serialization.Serializable

@Serializable
enum class TaskFilter(val id: Int) {
    ALL(id = 0),
    ACTIVE(id = 1),
    COMPLETED(id = 2);

    companion object {
        operator fun invoke(id: Int): TaskFilter = values().first { it.id == id }

        val DEFAULT: TaskFilter = ALL
    }
}