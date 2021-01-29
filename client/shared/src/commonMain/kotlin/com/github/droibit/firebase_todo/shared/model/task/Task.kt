package com.github.droibit.firebase_todo.shared.model.task

// TODO: Review properties.
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean
) {
    val isActive: Boolean get() = !isCompleted
}