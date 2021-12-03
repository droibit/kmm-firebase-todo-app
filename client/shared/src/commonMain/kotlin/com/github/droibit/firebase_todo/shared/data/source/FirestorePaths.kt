package com.github.droibit.firebase_todo.shared.data.source

internal object FirestorePaths {

    fun user(userId: String) = "users/$userId"

    fun statistics(userId: String) = "${user(userId)}/statistics/task"

    fun tasks(userId: String) = "${user(userId)}/tasks"

    fun task(userId: String, taskId: String) = "${tasks(userId)}/$taskId"
}
