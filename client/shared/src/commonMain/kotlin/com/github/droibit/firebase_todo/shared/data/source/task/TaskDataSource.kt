package com.github.droibit.firebase_todo.shared.data.source.task

import com.chrynan.inject.Inject
import com.chrynan.inject.Singleton
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.code

@Singleton
class TaskDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createTask(userId: String, title: String, description: String) {
        try {
            val tasksRef = firestore.collection(Paths.tasks(userId))
            val newTask = Task(
                title = title,
                description = description,
                isCompleted = false,
                createdAt = FieldValue.serverTimestamp,
                updatedAt = FieldValue.serverTimestamp
            )

            val result = tasksRef.add(
                Task.serializer(),
                newTask,
                encodeDefaults = false
            )
            Napier.d("Created task: ${result.id}")
        } catch (e: FirebaseFirestoreException) {
            Napier.e("Create task error(${e.code}):", e)
            throw TaskException(cause = e)
        }
    }

    object Paths {
        fun tasks(userId: String) = "users/$userId/tasks"
    }
}