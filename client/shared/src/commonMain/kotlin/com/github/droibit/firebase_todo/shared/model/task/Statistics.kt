package com.github.droibit.firebase_todo.shared.model.task

import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    val numberOfActiveTasks: Int,
    val numberOfCompletedTasks: Int,
    val updatedAt: Double
) {
    val updatedAtMillis: Long get() = updatedAt.toLong()
}